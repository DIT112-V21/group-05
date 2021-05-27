#include <Smartcar.h>
#include <MQTT.h>
#include <WiFi.h>
#include <vector>

#ifdef __SMCE__
#include <OV767X.h>
#endif

#ifndef __SMCE__
WiFiClient net;
#endif
MQTTClient mqtt;

//Camera
const auto oneSecond = 1000UL;
//Ultrasonic sensor
const int TRIGGER_PIN               = 6;
const int ECHO_PIN                  = 7;
const unsigned int MAX_DISTANCE     = 300;
//Infrared sensor
const int BACK_IR_PIN               = 3;
const int STOP_AT                   = 50;
//Directional booleans
bool forward = false;
bool back = false;
//Power simulator
bool powerON = true;
bool powerOFF = false;

ArduinoRuntime arduinoRuntime;

//Motors
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);

//Steering
DifferentialControl control(leftMotor, rightMotor);

//Ultrasonic sensor
SR04 frontUltraSonic(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

//Infrared sensor
GP2D120 backInfraRed(arduinoRuntime, BACK_IR_PIN);

SimpleCar car(control);

//Used for Camera
std::vector<char> frameBuffer;


void setup() {
    Serial.begin(9600);
#ifdef __SMCE__
    Camera.begin(QVGA, RGB888, 15);
    frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
    mqtt.begin("aerostun.dev", 1883, WiFi);
#else
    mqtt.begin(net);
    //mqtt.begin("aerostun.dev", 1883, WiFi);
#endif
    if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/group/05/control/#", 1);
    mqtt.onMessage([](String topic, String message) {
      if (topic == "/group/05/control/handleInput") {
        handleInput(message);
      }
      else {
        Serial.println(topic + " " + message);
      }
    });
  }
}

void loop() {
    if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
    avoidObstacle();
#ifdef __SMCE__
    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("/group/05/camera", frameBuffer.data(), frameBuffer.size(),
                   false, 0);
    }
#endif
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
    }
    avoidObstacle();
}



void handleInput(String input) {
        int inputChoice = input.substring(0).toInt();
        powerSwitch(inputChoice);
        if(powerON) {
            int throttle;
            int deg;
            if(input.length() > 1) {
              unsigned int throttleChoice = input.substring(1).toInt();
              throttle = throttleChoice;
              deg = throttleChoice;
            }
            
            switch(inputChoice) {
              case 2: //move forward
                forward = true;
                back = false;
                car.setSpeed(throttle);
                break;
              
              case 3: //reverse movement
                forward = false;
                back = true;
                car.setSpeed(-throttle);
                break;
            
              case 4: //turn right
                  car.setAngle(deg);
                break;
            
              case 5: //turn left
                  car.setAngle(-deg);
                break;
            
              case 6: //stop turning
                car.setAngle(0);
                break;
            
              case 7: //stop movement
                forward = false;
                back = false;
                car.setAngle(0);
                car.setSpeed(0);
                break;
                
              default:
                break;
            }
        }
}

//Used to simulate an ON/OFF switch
void powerSwitch(int inputChoice) {
  if(inputChoice == 1) {
    powerON = true;
    powerOFF = false;
  }
  else if(inputChoice == 0) {
    powerON = false;
    powerOFF = true;
    car.setSpeed(0);
  }
}

void avoidObstacle() 
{
    int frontDistance = frontUltraSonic.getDistance();
    int rearDistance = backInfraRed.getDistance();
    
    bool frontObstacle = forward && frontDistance > 0 && frontDistance < STOP_AT;
    bool rearObstacle = back && rearDistance > 0 && rearDistance < STOP_AT;
    
    if (frontObstacle || rearObstacle) {
         car.setSpeed(0);
         delay(500);
    }
}

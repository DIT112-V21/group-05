#include <Smartcar.h>

const int TRIGGER_PIN               = 6;
const int ECHO_PIN                  = 7;
const unsigned int MAX_DISTANCE     = 300;
const int BACK_IR_PIN               = 3;
const int SPEED_LIMIT               = 70;
const int REVERSE_SPEED_LIMIT       = 30;

bool forward = false;
bool back = false;
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

void setup() {
    Serial.begin(9600);
}

void loop()
{
   handleInput();
   avoidObstacle();
}

void handleInput() {
    if (Serial.available()) {
        String input = Serial.readStringUntil('\n');
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
                car.setSpeed(speedLimiter(throttle));
                break;
              
              case 3: //reverse movement
                forward = false;
                back = true;
                car.setSpeed(speedLimiter(-throttle));
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
                car.setSpeed(0);
                break;
                
              default:
                break;
            }
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
    int ultraSonicDistance = frontUltraSonic.getDistance();
    int infraRedDistance = backInfraRed.getDistance();
    //checks obstacle infront of car using ultrasonic sensor
    if (forward && ultraSonicDistance > 0 && ultraSonicDistance < 50)
    {
         car.setSpeed(0);
         delay(500);
         /*if (!(infraRedDistance > 0 && infraRedDistance < 100)){
         car.setSpeed(-50);
         delay(1500);
         car.setSpeed(0);
         }*/
    }
    //checks obstacle infront of car using infrared sensor
    else if(back && infraRedDistance > 0 && infraRedDistance < 50)
    {
      car.setSpeed(0);
         delay(500);
         /*if (!(ultraSonicDistance > 0 && ultraSonicDistance < 100)){
         car.setSpeed(50);
         delay(1500);
         car.setSpeed(0);
         }*/
    }
}

int speedLimiter(int throttle)
{
    if (throttle > SPEED_LIMIT)
    {
        throttle = SPEED_LIMIT;
    }
    return throttle;
}

int reverseSpeedLimiter(int throttle)
{
    if (throttle > REVERSE_SPEED_LIMIT)
    {
        throttle = REVERSE_SPEED_LIMIT;
    }
    return throttle;
}

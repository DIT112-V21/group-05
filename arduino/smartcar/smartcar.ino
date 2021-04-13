#include <Smartcar.h>

const int TRIGGER_PIN               = 6;
const int ECHO_PIN                  = 7;
const unsigned int MAX_DISTANCE     = 300;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

SimpleCar car(control);

void setup() {
  Serial.begin(9600);
}

void loop() {
  handleInput();
  avoidObstacle();
}
void handleInput()
{
    // handle serial input if there is any

    if (Serial.available())
    {
        String input = Serial.readStringUntil('\n');
        if (input.startsWith("m"))
        {
            int throttle = input.substring(1).toInt();
            car.setSpeed(throttle);
        }
        else if (input.startsWith("t"))
        {
            int deg = input.substring(1).toInt();
            car.setAngle(deg);
        }
    }
}
void avoidObstacle() {
  int distance = front.getDistance();
    if(distance > 0 && distance < 100) {
      car.setSpeed(0);
      delay(500);
      car.setSpeed(-50);
      delay(1500);
      car.setSpeed(0);
    }
}

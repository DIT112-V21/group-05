#include <Smartcar.h>

const int TRIGGER_PIN               = 6;
const int ECHO_PIN                  = 7;
const unsigned int MAX_DISTANCE     = 100;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

SimpleCar car(control);

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(200);
}

void loop() {
  handleInput();
  stop();
}

void handleInput()
{
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

void stop() {
    if(front.getDistance() > 20) {
      car.setSpeed(0);
      delay(500);
    }
}

#include <Smartcar.h>

const int TRIGGER_PIN               = 6;
const int ECHO_PIN                  = 7;
const unsigned int MAX_DISTANCE     = 300;
const int BACK_IR_PIN               = 3;


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
bool forward;
bool back;

void handleInput()
{
    if (Serial.available())
    {
        String input = Serial.readStringUntil('\n');
        //set forward speed to input value in kilometers per hour
        if (input.startsWith("f"))
        {
            forward = true;
            back = false;
            unsigned int throttle = input.substring(1).toInt();
            car.setSpeed(throttle);
        }
        //set reverse speed to input value in kilometers per hour
        else if (input.startsWith("r"))
        {
            forward = false;
            back = true;
            unsigned int throttle = input.substring(1).toInt();
            car.setSpeed((int) -throttle);
        }
        else if (input.startsWith("tr"))
        {
            int deg = input.substring(2).toInt();
            car.setAngle(deg);
        }
         else if (input.startsWith("tl"))
        {
            int deg = input.substring(2).toInt();
            car.setAngle(-deg);
        }
         else if (input.startsWith("ts"))
        {
            car.setAngle(0);
        }
        else if (input.startsWith("s"))
        {
            forward = false;
            back = false;
            car.setSpeed(0);
        }
    }
}

void avoidObstacle() 
{
    int ultraSonicDistance = frontUltraSonic.getDistance();
    int infraRedDistance = backInfraRed.getDistance();
    //checks obstacle infront of car using ultrasonic sensor
    if (forward && ultraSonicDistance > 0 && ultraSonicDistance < 100)
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
    else if(back && infraRedDistance > 0 && infraRedDistance < 100)
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

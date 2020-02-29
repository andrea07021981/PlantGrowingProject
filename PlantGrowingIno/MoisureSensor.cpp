#include <Arduino.h>
#include "MoisureSensor.h" // Only required if you defined types or classes in A.h that you need here

/*
 * Class for reading humidity
 */

MoisureSensor::MoisureSensor(int pin)
{
  pinMode(pin, OUTPUT);
  _pin = pin;
  
}

int MoisureSensor::getDataSensor()
{
  int sensorValue = analogRead(_pin);
  return sensorValue;
}

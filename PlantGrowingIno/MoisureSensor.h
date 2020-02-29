#pragma once // Important if you include header files from other header files
#ifndef MoisureSensor_h
#define MoisureSensor_h

#include "Arduino.h"

class MoisureSensor
{
  public:
    MoisureSensor(int pin);
    int getDataSensor();
  private:
    int _pin;
};

#endif

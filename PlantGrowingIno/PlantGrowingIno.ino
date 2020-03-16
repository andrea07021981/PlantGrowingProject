/*
  Web client

 This sketch connects to a website (http://www.google.com)
 using the WiFi module.

 This example is written for a network using WPA encryption. For
 WEP or WPA, change the Wifi.begin() call accordingly.

 This example is written for a network using WPA encryption. For
 WEP or WPA, change the Wifi.begin() call accordingly.

 Circuit:
 * Board with NINA module (Arduino MKR WiFi 1010, MKR VIDOR 4000 and UNO WiFi Rev.2)

 created 13 July 2010
 by dlf (Metodo2 srl)
 modified 31 May 2012
 by Tom Igoe
 */

#include "ArduinoJson.h"
#include "MoisureSensor.h"
#include <SPI.h>
#include <WiFiNINA.h>

#include "arduino_secrets.h" 
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
int status = WL_IDLE_STATUS;
char server[] = "192.168.0.12";    // name address for Backend

// Initialize the Ethernet client library
// with the IP address and port of the server
// that you want to connect to (port 80 is default for HTTP):
WiFiClient client;

//PIN and BOARD CONFIG
int sensorPin = A0; 

//plant id, it will be configured remotely in the second step
String plantId = "9";
String waterCommand("1");

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // check for the WiFi module:
  if (WiFi.status() == WL_NO_MODULE) {
    Serial.println("Communication with WiFi module failed!");
    // don't continue
    while (true);
  }

  String fv = WiFi.firmwareVersion();
  if (fv < WIFI_FIRMWARE_LATEST_VERSION) {
    Serial.println("Please upgrade the firmware");
  }

  // attempt to connect to Wifi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, pass);

    // wait 10 seconds for connection:
    delay(5000);
  }
  Serial.println("Connected to wifi");
  printWifiStatus();

  

  //Create the post request
  //JsonArray data = doc.createNestedArray("data");

//  "temperature": 97.9,
//    "humidity": 2.8,
//    "lastWatering": "2020-02-02T17:28:31.165Z"
//  doc["temperature"] = "12.2";
//  doc["humidity"] = "2.2";
//  doc["lastWatering"] = "12.2";

 

}

void loop() {

  Serial.println("\nStarting connection to server...");

  postInfoWs();
  
  String commandId = getCommandWaterWs();
  if (commandId != "") {
    //TODO water plant command
  }
  deleteCommandsWs(commandId);
  
  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    Serial.println();
    Serial.println("disconnecting from server.");
    client.stop();

    // do nothing forevermore: I'll change it to some minutes
    while(true);
  }
}

/**
 * Retrieve humidity and temp and POST the values
 */
void postInfoWs() {
  //Get data from sensor and temperature
  MoisureSensor moisureSensor = MoisureSensor(sensorPin);
  Serial.print("Analog Value : ");
  Serial.println(moisureSensor.getDataSensor());

  if (client.connect(server, 3000)) {
    //Prepare data
    DynamicJsonDocument doc(1024);
    doc["plantId"] = plantId;
    doc["temperature"] = 22.2;
    doc["humidity"] = 22.2;
    doc["execTime"] = "";
    String json = "";
    serializeJson(doc, json);
    Serial.println("");
    Serial.println("-------------------");
    
    Serial.println("connected to server for POST");
    client.println("POST /data HTTP/1.1");//192.168.0.14
    client.println("Host: http://192.168.0.12:3000");
    client.println("Content-Type: application/json; charset=utf-8");
    client.print("Content-Length: ");
    client.println(json.length());
    client.println("Connection: close");
    client.println();
    client.println(json);
    Serial.println(json);
  }

  while (!client.available()) {
    ; // wait for connection
  }

  // if there are incoming bytes available
  // from the server, read them and print them:
  while (client.available()) {
    char c = client.read();
    Serial.write(c);
  }
}

/**
 * One command every call, in the second step I'll manage multiple requests
*/
String getCommandWaterWs() {
  if (client.connect(server, 3000)) {
    Serial.println("connected to server  for GET");
    // Make a HTTP request: Check command
    client.println("GET /command-water/?plantId="+plantId+"&commandType="+waterCommand+" HTTP/1.1");//192.168.0.12
    client.println("Host: http://192.168.0.12:3000");
    client.println("Connection: close");
    client.println();
  }
  while (!client.available()) {
    ; // wait for connection
  }
  
  // if there are incoming bytes available
  // from the server, read them and print them:
  String c;
  while (client.available()) {
    c = client.readStringUntil('\r');
  }
  delay(3000);
  return c;
}

void deleteCommandsWs(String commandId) {
  DynamicJsonDocument doc(1024);
  if (client.connect(server, 3000)) {
    //Prepare data
    DynamicJsonDocument doc(1024);
    doc["commandId"] = commandId;
    String json = "";
    serializeJson(doc, json);
    Serial.println("");
    Serial.println("-------------------");
    
    Serial.println("connected to server for PUT");
    client.println("PUT /command HTTP/1.1");//192.168.0.14
    client.println("Host: http://192.168.0.12:3000");
    client.println("Content-Type: application/json; charset=utf-8");
    client.print("Content-Length: ");
    client.println(json.length());
    client.println("Connection: close");
    client.println();
    client.println(json);
    Serial.println(json);
  }
  while (!client.available()) {
    ; // wait for connection
  }
  
  // if there are incoming bytes available
  // from the server, read them and print them:
  while (client.available()) {
    char c = client.read();
    Serial.write(c);
  }
  
}

void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your board's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

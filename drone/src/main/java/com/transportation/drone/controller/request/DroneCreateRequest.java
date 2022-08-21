package com.transportation.drone.controller.request;

public record DroneCreateRequest(String serialNumber, String Model, int weightLimit, int batteryCapacity) {}

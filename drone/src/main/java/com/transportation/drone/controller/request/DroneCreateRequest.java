package com.transportation.drone.controller.request;

public record DroneCreateRequest(String serialNumber, String model, int weightLimit, int batteryCapacity) {}

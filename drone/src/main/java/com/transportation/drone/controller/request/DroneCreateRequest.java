package com.transportation.drone.controller.request;

import com.transportation.drone.model.DroneState;

public record DroneCreateRequest(String serialNumber, String model, int weightLimit, int batteryCapacity, DroneState droneState) {}

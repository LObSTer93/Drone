package com.transportation.drone.service;

import com.transportation.drone.model.Drone;
import com.transportation.drone.model.Medication;

import java.util.List;
import java.util.Set;

public interface DroneService {
    Drone create(String serialNumber, String Model, int weightLimit, int batteryCapacity);

    Drone updateLoad(String serialNumber, List<Medication> medicationList);

    List<Medication> getLoad(String serialNumber);

    Set<Drone> getAvailableForLoading();

    int getBatteryLevel(String serialNumber);
}

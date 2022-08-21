package com.transportation.drone.data;

import com.transportation.drone.model.Drone;

import java.util.Optional;
import java.util.Set;

public interface DroneRepository {
    Drone create(String serialNumber, String Model, int weightLimit, int batteryCapacity);

    Optional<Drone> getBySerialNumber(String serialNumber);

    Set<Drone> getAll();
}

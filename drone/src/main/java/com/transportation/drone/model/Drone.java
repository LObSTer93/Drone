package com.transportation.drone.model;

import java.util.List;
import java.util.Objects;

public record Drone(String serialNumber, String model, int weightLimit, int batteryCapacity, DroneState droneState,
                    List<Medication> medicationList) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drone drone = (Drone) o;
        return serialNumber.equals(drone.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }
}

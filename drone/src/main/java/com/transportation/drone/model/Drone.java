package com.transportation.drone.model;

import java.util.List;
import java.util.Objects;

public record Drone(String serialNumber, String Model, int weightLimit, int batteryCapacity, List<Medication> medicationList) {

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

package com.transportation.drone.data.no_db_impl;

import com.transportation.drone.data.DroneRepository;
import com.transportation.drone.model.Drone;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class NoDbDroneRepositoryImpl implements DroneRepository {
    @Override
    public Drone create(String serialNumber, String Model, int weightLimit, int batteryCapacity) {
        return null;
    }

    @Override
    public Optional<Drone> getBySerialNumber(String serialNumber) {
        return Optional.empty();
    }

    @Override
    public Set<Drone> getAll() {
        return null;
    }
}

package com.transportation.drone.data.dao;

import com.transportation.drone.model.DroneState;
import com.transportation.drone.model.Medication;
import org.springframework.data.annotation.Id;

import java.util.List;

public record DroneDao(@Id String serialNumber, String model, int weightLimit, int batteryCapacity, DroneState droneState,
                       List<Medication> medicationList) {}

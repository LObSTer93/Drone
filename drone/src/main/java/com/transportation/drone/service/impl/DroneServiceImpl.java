package com.transportation.drone.service.impl;

import com.transportation.drone.data.DroneRepository;
import com.transportation.drone.model.Drone;
import com.transportation.drone.model.Medication;
import com.transportation.drone.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    @Override
    public Drone create(String serialNumber, String Model, int weightLimit, int batteryCapacity) {
        return droneRepository.create(serialNumber, Model, weightLimit, batteryCapacity);
    }

    @Override
    public Drone updateLoad(String serialNumber, List<Medication> medicationList) {
        Drone drone = droneRepository.getBySerialNumber(serialNumber).orElseThrow();
        int currentWeight = drone.medicationList().stream()
                .mapToInt(Medication::weight)
                .sum();
        int addingWeight = medicationList.stream()
                .mapToInt(Medication::weight)
                .sum();
        if(drone.weightLimit() - currentWeight < addingWeight) {
            throw new RuntimeException();
        }
        drone.medicationList().addAll(medicationList);
        return drone;
    }

    @Override
    public List<Medication> getLoad(String serialNumber) {
        Drone drone = droneRepository.getBySerialNumber(serialNumber).orElseThrow();
        return drone.medicationList();
    }

    @Override
    public Set<Drone> getAvailableForLoading() {
        Set<Drone> droneList = droneRepository.getAll();
        Map<String, Integer> currentWeightBySerialNumberMap = droneList.stream()
                .collect(Collectors.toMap(
                        Drone::serialNumber,
                        drone -> drone.medicationList().stream()
                                .mapToInt(Medication::weight)
                                .sum()
                ));
        return droneList.stream()
                .filter(drone -> {
                    Integer currentWeight = currentWeightBySerialNumberMap.get(drone.serialNumber());
                    return currentWeight < drone.weightLimit();
                })
                .collect(Collectors.toSet());
    }

    @Override
    public int getBatteryLevel(String serialNumber) {
        Drone drone = droneRepository.getBySerialNumber(serialNumber).orElseThrow();
        return drone.batteryCapacity();
    }
}

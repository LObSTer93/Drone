package com.transportation.drone.service.impl;

import com.transportation.drone.data.DroneRepository;
import com.transportation.drone.data.dao.DroneDao;
import com.transportation.drone.model.Drone;
import com.transportation.drone.model.DroneState;
import com.transportation.drone.model.Medication;
import com.transportation.drone.service.DroneService;
import com.transportation.drone.util.DroneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.transportation.drone.model.DroneState.LOADING;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;

    public static final String LOAD_ERROR = "The drone with serial number %s can not carry the added load";
    public static final String INIT_ERROR = "The drone with serial number %s having LOADING state and battery level bellow 25 can not be initialized";

    @Override
    public Drone create(String serialNumber, String model, int weightLimit, int batteryCapacity, DroneState droneState) {
        if(LOADING == droneState && batteryCapacity < 25) {
            throw new RuntimeException(String.format(INIT_ERROR, serialNumber));
        }
        DroneDao droneDao = new DroneDao(serialNumber, model, weightLimit, batteryCapacity, droneState, new LinkedList<>());
        droneDao = droneRepository.insert(droneDao);
        return droneMapper.daoToModel(droneDao);
    }

    @Override
    public Drone updateLoad(String serialNumber, List<Medication> medicationList) {
        DroneDao droneDao = droneRepository.findBySerialNumber(serialNumber).orElseThrow();
        int currentWeight = droneDao.medicationList().stream()
                .mapToInt(Medication::weight)
                .sum();
        int addingWeight = medicationList.stream()
                .mapToInt(Medication::weight)
                .sum();
        if (droneDao.weightLimit() - currentWeight < addingWeight) {
            throw new RuntimeException(String.format(LOAD_ERROR, serialNumber));
        }
        droneDao.medicationList().addAll(medicationList);
        droneDao = droneRepository.save(droneDao);
        return droneMapper.daoToModel(droneDao);
    }

    @Override
    public List<Medication> getLoad(String serialNumber) {
        DroneDao droneDao = droneRepository.findBySerialNumber(serialNumber).orElseThrow();
        return droneDao.medicationList();
    }

    @Override
    public Set<Drone> getAvailableForLoading() {
        List<DroneDao> droneDaoList = droneRepository.findAll();
        Map<String, Integer> currentWeightBySerialNumberMap = droneDaoList.stream()
                .collect(Collectors.toMap(
                        DroneDao::serialNumber,
                        droneDao -> droneDao.medicationList().stream()
                                .mapToInt(Medication::weight)
                                .sum()
                ));
        return droneDaoList.stream()
                .filter(droneDao -> {
                    Integer currentWeight = currentWeightBySerialNumberMap.get(droneDao.serialNumber());
                    return currentWeight < droneDao.weightLimit();
                })
                .map(droneMapper::daoToModel)
                .collect(Collectors.toSet());
    }

    @Override
    public int getBatteryLevel(String serialNumber) {
        DroneDao droneDao = droneRepository.findBySerialNumber(serialNumber).orElseThrow();
        return droneDao.batteryCapacity();
    }
}

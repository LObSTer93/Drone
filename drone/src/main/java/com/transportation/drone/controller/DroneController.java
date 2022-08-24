package com.transportation.drone.controller;

import com.transportation.drone.controller.request.DroneCreateRequest;
import com.transportation.drone.model.Drone;
import com.transportation.drone.model.Medication;
import com.transportation.drone.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping()
    public Drone register(@RequestBody DroneCreateRequest droneCreateRequest) {
        return droneService.create(
                droneCreateRequest.serialNumber(), droneCreateRequest.model(), droneCreateRequest.weightLimit(),
                droneCreateRequest.batteryCapacity(), droneCreateRequest.droneState()
        );
    }

    @PatchMapping("/{serialNumber}/load")
    public Drone load(@PathVariable("serialNumber") String serialNumber, @RequestBody List<Medication> medications) {
        return droneService.updateLoad(serialNumber, medications);
    }

    @GetMapping("/{serialNumber}/load")
    public List<Medication> getLoad(@PathVariable("serialNumber") String serialNumber) {
        return droneService.getLoad(serialNumber);
    }

    @GetMapping("/available")
    public Set<Drone> getAvailableForLoading() {
        return droneService.getAvailableForLoading();
    }

    @GetMapping("/{serialNumber}/battery")
    public int getBatteryLevel(@PathVariable("serialNumber") String serialNumber) {
        return droneService.getBatteryLevel(serialNumber);
    }
}

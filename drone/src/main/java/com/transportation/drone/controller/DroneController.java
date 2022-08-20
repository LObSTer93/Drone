package com.transportation.drone.controller;

import com.transportation.drone.controller.request.DroneCreateRequest;
import com.transportation.drone.controller.request.DroneLoadRequest;
import com.transportation.drone.controller.response.LoadResponse;
import com.transportation.drone.model.Drone;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/drones")
public class DroneController {

    @PostMapping()
    public Drone register(@RequestBody DroneCreateRequest droneCreateRequest) {
        return new Drone();
    }

    @PatchMapping("/{id}/load")
    public Drone load(@PathVariable("id") Long id, @RequestBody DroneLoadRequest droneLoadRequest) {
        return new Drone();
    }

    @GetMapping("/{id}/load")
    public LoadResponse getLoad(@PathVariable("id") Long id){
        return new LoadResponse();
    }

    @GetMapping("/available")
    public Set<Drone> getAvailable() {
        return Set.of(new Drone(), new Drone());
    }

    @GetMapping("/{id}/battery")
    public String getBatteryLevel(@PathVariable("id") Long id){
        return "LOADING";
    }
}

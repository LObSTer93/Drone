package com.transportation.drone.util;

import com.transportation.drone.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScheduledLogger {

    private static final Logger log = LoggerFactory.getLogger(ScheduledLogger.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private final DroneService droneService;

    @Scheduled(fixedRateString = "${battery_checker_period}")
    public void reportCurrentTime() {
        log.info("======= Battery checker is started {}", dateFormat.format(new Date()));
        droneService.getAll().forEach(
                drone -> log.info("The drone {} has battery {} {}",
                        drone.serialNumber(), drone.batteryCapacity(), dateFormat.format(new Date())
                )
        );
        log.info("======= Battery checker is finished {}", dateFormat.format(new Date()));
    }
}

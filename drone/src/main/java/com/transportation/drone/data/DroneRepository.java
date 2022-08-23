package com.transportation.drone.data;

import com.transportation.drone.data.dao.DroneDao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DroneRepository extends MongoRepository<DroneDao, String> {
    Optional<DroneDao> findBySerialNumber(String serialNumber);
}

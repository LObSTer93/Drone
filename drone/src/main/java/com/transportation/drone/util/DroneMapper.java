package com.transportation.drone.util;

import com.transportation.drone.data.dao.DroneDao;
import com.transportation.drone.model.Drone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    Drone daoToModel(DroneDao droneDao);
    DroneDao modelToDao(Drone drone);
}

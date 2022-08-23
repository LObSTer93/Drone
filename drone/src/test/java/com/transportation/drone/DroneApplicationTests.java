package com.transportation.drone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transportation.drone.controller.request.DroneCreateRequest;
import com.transportation.drone.data.DroneRepository;
import com.transportation.drone.model.Drone;
import com.transportation.drone.model.Medication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DroneApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DroneRepository droneRepository;

    private static final String SERIAL_NUMBER = "serialNumber";
    private static final String MODEL = "model";
    private static final int WEIGHT_LIMIT = 500;
    private static final int BATTERY_CAPACITY = 100;

    @BeforeEach
    public void cleanDB() {
        droneRepository.deleteAll();
    }


    @Test
    public void registerSuccess() throws Exception {
        Drone drone = registerDrone();

        assertEquals(SERIAL_NUMBER, drone.serialNumber());
        assertEquals(MODEL, drone.model());
        assertEquals(WEIGHT_LIMIT, drone.weightLimit());
        assertEquals(BATTERY_CAPACITY, drone.batteryCapacity());
    }

    private Drone registerDrone() throws Exception {
        DroneCreateRequest droneCreateRequest = new DroneCreateRequest(SERIAL_NUMBER, MODEL, WEIGHT_LIMIT, BATTERY_CAPACITY);
        return registerDrone(droneCreateRequest);
    }

    private Drone registerDrone(DroneCreateRequest droneCreateRequest) throws Exception {
        String droneCreateRequestJSON = objectMapper.writeValueAsString(droneCreateRequest);

        MvcResult mvcResult = mockMvc.perform(post("/drones").contentType(MediaType.APPLICATION_JSON).content(droneCreateRequestJSON)).andExpect(status().isOk()).andReturn();
        String droneString = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(droneString, Drone.class);
    }

    @Test
    public void loadSuccess() throws Exception {
        registerDrone();

        Drone drone = loadDrone();
        assertEquals(2, drone.medicationList().size());
    }

    private Drone loadDrone() throws Exception {
        return loadDrone(SERIAL_NUMBER, 200, 300);
    }

    private Drone loadDrone(String serialNumber, int... weights) throws Exception {
        List<Medication> medicationList = Arrays.stream(weights).mapToObj(weight -> new Medication("medicationName" + weight, weight, "medicationCode" + weight)).collect(Collectors.toList());
        String medicationListJSON = objectMapper.writeValueAsString(medicationList);

        MvcResult mvcResult = mockMvc.perform(patch("/drones/" + serialNumber + "/load").contentType(MediaType.APPLICATION_JSON).content(medicationListJSON)).andExpect(status().isOk()).andReturn();

        String droneString = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(droneString, Drone.class);
    }

    @Test
    public void getLoadSuccess() throws Exception {
        registerDrone();
        loadDrone();

        MvcResult mvcResult = mockMvc.perform(get("/drones/" + SERIAL_NUMBER + "/load")).andExpect(status().isOk()).andReturn();

        String medicationListString = mvcResult.getResponse().getContentAsString();
        List<Medication> medicationList = objectMapper.readValue(medicationListString, new TypeReference<>() {
        });
        assertEquals(2, medicationList.size());
    }

    @Test
    public void getAvailableForLoading() throws Exception {
        DroneCreateRequest droneCreateRequest1 = new DroneCreateRequest(SERIAL_NUMBER + 1, MODEL, WEIGHT_LIMIT, BATTERY_CAPACITY);
        registerDrone(droneCreateRequest1);
        loadDrone(SERIAL_NUMBER + 1, 200, 300);

        DroneCreateRequest droneCreateRequest2 = new DroneCreateRequest(SERIAL_NUMBER + 2, MODEL, WEIGHT_LIMIT, BATTERY_CAPACITY);
        registerDrone(droneCreateRequest2);
        loadDrone(SERIAL_NUMBER + 2, 200, 100);

        MvcResult mvcResult = mockMvc.perform(get("/drones/available")).andExpect(status().isOk()).andReturn();

        String droneSetString = mvcResult.getResponse().getContentAsString();
        List<Drone> droneList = objectMapper.readValue(droneSetString, new TypeReference<>() {
        });
        assertEquals(1, droneList.size());
        Drone drone = droneList.get(0);
        assertEquals(SERIAL_NUMBER + 2, drone.serialNumber());
    }

    @Test
    public void getBatteryLevel() throws Exception {
        registerDrone();

        mockMvc.perform(get("/drones/" + SERIAL_NUMBER + "/battery")).andExpect(status().isOk()).andExpect(content().string(containsString("" + BATTERY_CAPACITY)));
    }
}

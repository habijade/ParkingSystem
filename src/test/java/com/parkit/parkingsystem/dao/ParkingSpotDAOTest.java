package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotDAOTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;

    @BeforeAll
    public static void setUpClass() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

    }

    @AfterAll
    public static void tearDownClass() {

    }

    @Test
    public void testNextAvailableSlotForCar() {
        // GIVEN
        ParkingType parkingType = ParkingType.CAR;

        // WHEN
        int resultSlotAvailable = parkingSpotDAO.getNextAvailableSlot(parkingType);

        // THEN
        assertEquals(2, resultSlotAvailable );
    }

    @Test
    public void testNextAvailableSlotForBike() {
        // GIVEN
        ParkingType parkingType = ParkingType.BIKE;

        // WHEN
        int resultSlotAvailable = parkingSpotDAO.getNextAvailableSlot(parkingType);

        // THEN
        assertEquals(4, resultSlotAvailable );
    }

    @Test
    public void testUpdateParkingCar() {
        // GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        // WHEN
        boolean resultUpdateParking = parkingSpotDAO.updateParking(parkingSpot);

        // THEN
        assertEquals(resultUpdateParking, true);

    }

    @Test
    public void testUpdateParkingBike() {
        // GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        // WHEN
        boolean resultUpdateParking = parkingSpotDAO.updateParking(parkingSpot);

        // THEN
        assertEquals(resultUpdateParking, true);

    }
}

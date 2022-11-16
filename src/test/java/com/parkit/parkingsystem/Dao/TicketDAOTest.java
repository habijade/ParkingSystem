package com.parkit.parkingsystem.Dao;


import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.parkit.parkingsystem.model.Ticket;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static TicketDAO ticketDao;

    @BeforeAll
    public static void setUpClass() throws Exception {
        ticketDao = new TicketDAO();
        ticketDao.dataBaseConfig = dataBaseTestConfig;
    }


    @AfterAll
    public static void tearDownClass() {


    }

    @Test
    public void testSaveTicket() {
        // GIVEN
        Ticket ticket = new Ticket();
        long now = System.currentTimeMillis();
        Date inTime = new Date(now - (30 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        // WHEN
        ticketDao.saveTicket(ticket);
        Ticket ticketResult = ticketDao.getTicket("ABCDEF");
        // THEN
        assertEquals(ticket.getPrice(), ticketResult.getPrice());
        assertEquals(ticket.getVehicleRegNumber(), ticketResult.getVehicleRegNumber());
        assertEquals(ticket.getOutTime(), ticketResult.getOutTime());
//        assertEquals(ticket.getInTime(), ticketResult.getInTime());
        assertEquals(ticket.getParkingSpot(), ticketResult.getParkingSpot());

    }

    //test echou si ticket null
    @Test
    public void testFailSaveTicketWithANullTicket() throws SQLException {
        Ticket ticket = null;
        assertThrows(
                NullPointerException.class,
                () -> ticketDao.saveTicket(null));

    }

    @Test
    public void testGetTicket() throws Exception {
        // GIVEN
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = null;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(2);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        ticketDao.getTicket(vehicleRegNumber);
        Ticket ticketResult = ticketDao.getTicket("ABCDEF");
        // THEN
        assertEquals(ticket.getPrice(), ticketResult.getPrice());
        assertEquals(ticket.getVehicleRegNumber(), ticketResult.getVehicleRegNumber());
        assertEquals(ticket.getOutTime(), ticketResult.getOutTime());
//        assertEquals(ticket.getInTime(), ticketResult.getInTime());
        assertEquals(ticket.getParkingSpot(), ticketResult.getParkingSpot());

    }


    @Test
    public void testUpdateTicket() throws SQLException, ClassNotFoundException, IOException {
        // GIVEN


        Ticket ticket = new Ticket();
        ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
        Date outTime = new Date();
        ticket.setOutTime(outTime);

        // WHEN
        boolean resultUpdateTicket = ticketDao.updateTicket(ticket);

        // THEN
        assertEquals(resultUpdateTicket, true);
    }

    @Test
    public void testCheckVehicleIsNotReg() throws SQLException, ClassNotFoundException, IOException {
        // GIVEN
        String vehicleRegNumber = "ABCDEF";
        Date outTime = new Date();
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setOutTime(outTime);
        //WHEN
        boolean userRecurrent = ticketDao.checkVehicleIsReg(vehicleRegNumber);
        // THEN
        assertEquals(userRecurrent, false);
    }


    @Test
    public void testCheckVehicleIsReg() throws SQLException, ClassNotFoundException, IOException {
        // GIVEN
        String vehicleRegNumber = "ABCDEF";
        Date outTime = new Date();
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setOutTime(outTime);
        //WHEN
        boolean userRecurrent = ticketDao.checkVehicleIsReg(vehicleRegNumber);
        // THEN
        assertNotNull(ticket.getOutTime());
        assertEquals(userRecurrent, false);
    }


    @Test
    public void testCheckVehicleInParking() throws SQLException, ClassNotFoundException, IOException {
        // GIVEN
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setOutTime(null);
        //WHEN
        boolean vehicleInParking = ticketDao.checkVehicleInParking(vehicleRegNumber);
        // THEN
        assertEquals(vehicleInParking, true);
    }

}

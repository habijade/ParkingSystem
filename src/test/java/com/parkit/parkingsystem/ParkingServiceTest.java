package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private ParkingService parkingService;

    @Mock
    private InputReaderUtil inputReaderUtil;
    @Mock
    private ParkingSpotDAO parkingSpotDAO;
    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private FareCalculatorService fareCalculatorService;


    @BeforeEach
    private void setUpPerTest() {
        try {
            //given obligatoire centraliser dans le setup
//            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
//            Ticket ticket = new Ticket();
//            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
//            ticket.setParkingSpot(parkingSpot);
//            ticket.setVehicleRegNumber("ABCDEF");
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    public void processExitingVehicleTest() throws Exception {

        // GIVEN
        String vehicleRegNumber = "ABCDEF";
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(ticketDAO.checkVehicleInParking(vehicleRegNumber)).thenReturn(true);
        when(ticketDAO.checkVehicleIsReg(vehicleRegNumber)).thenReturn(true);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setId(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(ticket);
        boolean isRegularUser = true;
        doNothing().when(fareCalculatorService).calculateFare(anyObject(), anyBoolean());
        fareCalculatorService.calculateFare(ticket, isRegularUser);

        // WHEN
        parkingService.processExitingVehicle();

        // THEN
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
        verify(ticketDAO, Mockito.times(1)).checkVehicleInParking(vehicleRegNumber);
        verify(ticketDAO, Mockito.times(1)).checkVehicleIsReg(vehicleRegNumber);
        verify(ticketDAO, Mockito.times(1)).getTicket(vehicleRegNumber);
        verify(fareCalculatorService, Mockito.times(1)).calculateFare(ticket, isRegularUser);
        verify(ticketDAO, Mockito.times(1)).updateTicket(ticket);
//        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);
    }

    @Test
    public void processInComingVehicleTest() throws Exception {

        // GIVEN
        String vehicleRegNumber = "ABCDEF";
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(ticketDAO.checkVehicleInParking(vehicleRegNumber)).thenReturn(false);
        when(ticketDAO.checkVehicleIsReg(vehicleRegNumber)).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(false);
        Ticket ticket = new Ticket();
//        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
//        ticket.setOutTime(new Date(System.currentTimeMillis()));
//        ticket.setVehicleRegNumber("ABCDEF");
//        ticket.setId(1);
        when(ticketDAO.saveTicket(ticket)).thenReturn(true);

        //WHEN
        parkingService.processIncomingVehicle();

        //THEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(ticketDAO, Mockito.times(1)).checkVehicleInParking(vehicleRegNumber);
        verify(ticketDAO, Mockito.times(1)).checkVehicleIsReg(vehicleRegNumber);
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);

    }


}
package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
        private TicketDAO ticketDAO;


    public void calculateFare(Ticket ticket, boolean isRegularUser) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inMillisecond = ticket.getInTime().getTime();
        long outMillisecond = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = outMillisecond - inMillisecond;
        duration = duration / 3600000;
        duration = Math.round(duration * 100.0) / 100.0;
        if (duration <= 0.5) {
            ticket.setPrice(0.0);
        } else {

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
            calculDiscount(ticket.getPrice(), ticket, isRegularUser);
        }

    }

    //methode remise de 5%
    public void calculDiscount(double price, Ticket ticket, boolean isRegularUser){
        if(isRegularUser){
                double priceDiscount = (price * 5)/100;
                priceDiscount = Math.round(priceDiscount * 10.0) / 10.0;
                ticket.setPrice(priceDiscount);
                System.out.println("5% discount has been applied to ticket price");
        }
        ticket.setPrice(price);
    }
}
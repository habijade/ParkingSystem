package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    /*
    Calculates the price of a ticket, takes a ticket as a parameter and returns the price of the ticket.
     */
    public void calculateFare(Ticket ticket, boolean isRegularUser) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inMillisecond = ticket.getInTime().getTime();
        long outMillisecond = ticket.getOutTime().getTime();

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

   /*
   Calculates the reduction percentage for a regular user, takes the price, the ticket and a boolean as parameters, returns the price of the ticket.
    */
    public void calculDiscount(double price, Ticket ticket, boolean isRegularUser) {
        if (isRegularUser) {
            double priceDiscount = (price * 5) / 100;
            priceDiscount = Math.round(priceDiscount * 10.0) / 10.0;
            ticket.setPrice(priceDiscount);
            System.out.println("5% discount has been applied to ticket price");
        }
        ticket.setPrice(price);
    }
}
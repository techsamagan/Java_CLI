package com.Samagan.booking;

import com.Samagan.car.CarService;
import java.util.UUID;

public class CarBookingService {
    private final CarBookingDao carBookingDao;
    private final CarService carService;

    public CarBookingService(CarBookingDao carBookingDao, CarService carService) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
    }

    public CarBooking[] getBookings() {
        return carBookingDao.getBookings();
    }

    public CarBooking getBookingById(UUID bookingId) {
        return carBookingDao.findBookingById(bookingId);
    }

    public void bookCar(CarBooking booking) {
        carBookingDao.saveBooking(booking);
    }

    public void cancelBooking(UUID bookingId) {
        carBookingDao.deleteBooking(bookingId);
    }
}
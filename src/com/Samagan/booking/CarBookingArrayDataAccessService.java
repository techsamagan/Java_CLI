package com.Samagan.booking;

import java.util.Arrays;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {
    private static final CarBooking[] bookings = new CarBooking[100];
    private static int nextIndex = 0;

    @Override
    public CarBooking[] getBookings() {
        return Arrays.copyOf(bookings, nextIndex);
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        for (int i = 0; i < nextIndex; i++) {
            if (bookings[i].getBookingId().equals(bookingId)) {
                return bookings[i];
            }
        }
        return null;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        if (nextIndex >= bookings.length) {
            throw new IllegalStateException("Booking storage capacity reached");
        }
        bookings[nextIndex++] = booking;
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        for (int i = 0; i < nextIndex; i++) {
            if (bookings[i].getBookingId().equals(bookingId)) {
                bookings[i] = bookings[nextIndex - 1];
                bookings[nextIndex - 1] = null;
                nextIndex--;
                return;
            }
        }
    }
}
package com.Samagan.booking;

import java.util.UUID;

public class BookingDao {

    private static final int CAPACITY = 100;
    private static final Booking[] BOOKINGS = new Booking[CAPACITY];
    private static int bookingCount = 0;

    public Booking[] getAllBookings() {
        return BOOKINGS;
    }

    public boolean saveBooking(Booking booking) {
        if (bookingCount >= CAPACITY) {
            return false;
        }
        BOOKINGS[bookingCount++] = booking;
        return true;
    }

    public Booking getBookingById(UUID bookingId) {
        if (bookingId == null) {
            return null;
        }
        for (int i = 0; i < bookingCount; i++) {
            if (BOOKINGS[i] != null && BOOKINGS[i].getBookingId().equals(bookingId)) {
                return BOOKINGS[i];
            }
        }
        return null;
    }
}

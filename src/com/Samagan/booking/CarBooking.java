package com.Samagan.booking;

import com.Samagan.car.Car;
import com.Samagan.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class CarBooking implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID bookingId;
    private final User user;
    private final Car car;
    private final LocalDateTime bookingTime;
    private boolean isCanceled;

    public CarBooking(UUID bookingId, User user, Car car, LocalDateTime bookingTime) {
        this.bookingId = bookingId;
        this.user = user;
        this.car = car;
        this.bookingTime = bookingTime;
        this.isCanceled = false;
    }

    public UUID getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Car getCar() { return car; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public boolean isCanceled() { return isCanceled; }
    public void setCanceled(boolean canceled) { isCanceled = canceled; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarBooking that)) return false;
        return Objects.equals(bookingId, that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    @Override
    public String toString() {
        return "CarBooking{" +
                "bookingId=" + bookingId +
                ", user=" + user +
                ", car=" + car +
                ", bookingTime=" + bookingTime +
                ", isCanceled=" + isCanceled +
                '}';
    }
}
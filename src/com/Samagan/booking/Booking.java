package com.Samagan.booking;

import com.Samagan.car.Car;
import com.Samagan.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Booking {
    private final UUID bookingId;
    private final User user;
    private final Car car;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal priceCharged;
    private BookingStatus status;
    private final LocalDateTime createdAt;

    public Booking(UUID bookingId, User user, Car car, LocalDate startDate, LocalDate endDate, BigDecimal priceCharged, BookingStatus status, LocalDateTime createdAt) {
            this.bookingId = bookingId;
            this.user = user;
            this.car = car;
            this.startDate = startDate;
            this.endDate = endDate;
            this.priceCharged = priceCharged;
            this.status = status;
            this.createdAt = createdAt;
        }

        public UUID getBookingId() {
            return bookingId;
        }

        public User getUser() {
            return user;
        }

        public Car getCar() {
            return car;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public BigDecimal getPriceCharged() {
            return priceCharged;
        }

        public BookingStatus getStatus() {
            return status;
        }

        public void setStatus(BookingStatus status) {
            this.status = status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public String toString() {
            return "CarBooking{" +
                    "bookingId=" + bookingId +
                    ", user=" + user.getName() +
                    ", car=" + car.getBrand() + " (" + car.getRegNumber() + ")" +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", priceCharged=" + priceCharged +
                    ", status=" + status +
                    ", createdAt=" + createdAt +
                    '}';
        }
}

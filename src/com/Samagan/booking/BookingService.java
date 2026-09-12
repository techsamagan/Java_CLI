package com.Samagan.booking;

import com.Samagan.car.Car;
import com.Samagan.car.CarService;
import com.Samagan.user.User;
import com.Samagan.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class BookingService {

    private final BookingDao bookingDao;
    private final UserService userService;
    private final CarService carService;

    public BookingService(BookingDao bookingDao, UserService userService, CarService carService) {
        this.bookingDao = bookingDao;
        this.userService = userService;
        this.carService = carService;
    }

    public Booking[] getAllBooking() {
        return bookingDao.getAllBookings();
    }

    public Booking getBookingById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        return bookingDao.getBookingById(id);
    }

    public boolean isCarAvailable(UUID carId, LocalDate startDate, LocalDate endDate) {
        if (carId == null || startDate == null || endDate == null) {
            return false;
        }

        Booking[] bookings = bookingDao.getAllBookings();
        if (bookings == null) {
            return true;
        }

        for (Booking b : bookings) {
            if (b != null && b.getCar() != null && b.getCar().getId().equals(carId) && b.getStatus() == BookingStatus.ACTIVE) {

                boolean overlaps = !startDate.isAfter(b.getEndDate()) && !endDate.isBefore(b.getStartDate());
                if (overlaps) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) {
        if (userId == null || carId == null || startDate == null || endDate == null) {
            System.out.println("Error: Missing required booking parameters.");
            return false;
        }

        // Requirement: "Start date in the past -> Rejected"
        if (startDate.isBefore(LocalDate.now())) {
            System.out.println("Error: Start date cannot be in the past.");
            return false;
        }

        // Requirement: "End date not after start date -> Rejected"
        // End date must be strictly after start date (same day is rejected)
        if (!endDate.isAfter(startDate)) {
            System.out.println("Error: End date must be after start date.");
            return false;
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("Error: User not found with ID: " + userId);
            return false;
        }

        Car car = carService.getCarById(carId);
        if (car == null) {
            System.out.println("Error: Car not found with ID: " + carId);
            return false;
        }

        // Requirement: "Car already booked -> Rejected"
        if (!isCarAvailable(carId, startDate, endDate)) {
            System.out.println("Error: Car " + car.getRegNumber() + " is already booked for the selected dates.");
            return false;
        }

        // Calculate days and exact total price using BigDecimal
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalPrice = car.getRentalPricePerDay().multiply(BigDecimal.valueOf(days));

        Booking newBooking = new Booking(
                UUID.randomUUID(),
                user,
                car,
                startDate,
                endDate,
                totalPrice,
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );

        boolean saved = bookingDao.saveBooking(newBooking);
        if (saved) {
            System.out.println("Booking confirmed! ID: " + newBooking.getBookingId() + " | Total: $" + totalPrice);
            return true;
        } else {
            System.out.println("Error: Booking storage is full.");
            return false;
        }
    }

    public Booking[] getUserBookings(UUID userId) {
        if (userId == null) {
            return new Booking[0];
        }

        Booking[] all = bookingDao.getAllBookings();
        if (all == null) {
            return new Booking[0];
        }

        int count = 0;
        for (Booking b : all) {
            if (b != null && b.getUser() != null && b.getUser().getId().equals(userId)) {
                count++;
            }
        }

        Booking[] userBookings = new Booking[count];
        int idx = 0;
        for (Booking b : all) {
            if (b != null && b.getUser() != null && b.getUser().getId().equals(userId)) {
                userBookings[idx++] = b;
            }
        }
        return userBookings;
    }

    public boolean cancelBooking(UUID bookingId) {
        if (bookingId == null) {
            System.out.println("Error: Booking ID cannot be null.");
            return false;
        }

        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            System.out.println("Error: Booking not found.");
            return false;
        }
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            System.out.println("Error: Only active bookings can be cancelled.");
            return false;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        System.out.println("Booking " + bookingId + " has been cancelled.");
        return true;
    }

    // FR-05: All cars not currently booked (for today)
    public Car[] getAvailableCars() {
        Car[] allCars = carService.getAllCars();
        if (allCars == null) {
            return new Car[0];
        }

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        int count = 0;
        for (Car car : allCars) {
            if (car != null && isCarAvailable(car.getId(), today, tomorrow)) {
                count++;
            }
        }

        Car[] availableCars = new Car[count];
        int idx = 0;
        for (Car car : allCars) {
            if (car != null && isCarAvailable(car.getId(), today, tomorrow)) {
                availableCars[idx++] = car;
            }
        }
        return availableCars;
    }

    // FR-06: Available cars that are electric
    public Car[] getAvailableElectricCars() {
        Car[] availableCars = getAvailableCars();
        int count = 0;
        for (Car car : availableCars) {
            if (car != null && car.isElectric()) {
                count++;
            }
        }

        Car[] electricCars = new Car[count];
        int idx = 0;
        for (Car car : availableCars) {
            if (car != null && car.isElectric()) {
                electricCars[idx++] = car;
            }
        }
        return electricCars;
    }
}
package com.Samagan;

import com.Samagan.booking.*;
import com.Samagan.car.*;
import com.Samagan.user.*;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Swap between file storage and array storage here:
        CarBookingDao carBookingDao = new CarBookingFileDataAccessService("bookings.dat");
        // CarBookingDao carBookingDao = new CarBookingArrayDataAccessService();

        CarDao carDao = new CarArrayDataAccessService();
        CarService carService = new CarService(carDao);

        UserDao userDao = new UserArrayDataAccessService();
        UserService userService = new UserService(userDao);

        CarBookingService carBookingService = new CarBookingService(carBookingDao, carService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1 - Book Car");
            System.out.println("2 - Delete Booking");
            System.out.println("3 - View All User Booked Cars");
            System.out.println("4 - View All Bookings");
            System.out.println("5 - View Available Cars");
            System.out.println("6 - View Available Electric Cars");
            System.out.println("7 - View All Users");
            System.out.println("8 - Exit");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> bookCar(scanner, userService, carService, carBookingService);
                case "2" -> deleteBooking(scanner, carBookingService);
                case "3" -> viewUserBookedCars(scanner, carBookingService);
                case "4" -> viewAllBookings(carBookingService);
                case "5" -> viewAvailableCars(carService, carBookingService, false);
                case "6" -> viewAvailableCars(carService, carBookingService, true);
                case "7" -> viewAllUsers(userService);
                case "8" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 8.");
            }
        }
    }

    private static void bookCar(Scanner scanner, UserService userService, CarService carService, CarBookingService carBookingService) {
        // 1. Display and select user
        viewAllUsers(userService);
        System.out.print("Select User ID: ");
        String userIdStr = scanner.nextLine().trim();
        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        User user = userService.getUser(userId);
        if (user == null) {
            System.out.println("No user found with ID: " + userId);
            return;
        }

        // 2. Display and select available car
        viewAvailableCars(carService, carBookingService, false);
        System.out.print("Select Car Reg Number: ");
        String carIdStr = scanner.nextLine().trim();
        UUID carId;
        try {
            carId = UUID.fromString(carIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        Car car = carService.getCar(carId);
        if (car == null) {
            System.out.println("No car found with ID: " + carId);
            return;
        }

        // Ensure the car is not already booked
        if (isCarBooked(carId, carBookingService.getBookings())) {
            System.out.println("Car is already booked.");
            return;
        }

        // 3. Save booking
        UUID bookingId = UUID.randomUUID();
        CarBooking booking = new CarBooking(bookingId, user, car, LocalDateTime.now());
        carBookingService.bookCar(booking);
        System.out.println("Successfully booked car with Booking ID: " + bookingId);
    }

    private static void deleteBooking(Scanner scanner, CarBookingService carBookingService) {
        System.out.print("Enter Booking ID to delete: ");
        String bookingIdStr = scanner.nextLine().trim();
        UUID bookingId;
        try {
            bookingId = UUID.fromString(bookingIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        CarBooking booking = carBookingService.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("No booking found with ID: " + bookingId);
            return;
        }

        carBookingService.cancelBooking(bookingId);
        System.out.println("Booking deleted successfully.");
    }

    private static void viewUserBookedCars(Scanner scanner, CarBookingService carBookingService) {
        System.out.print("Enter User ID: ");
        String userIdStr = scanner.nextLine().trim();
        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        CarBooking[] bookings = carBookingService.getBookings();
        int count = 0;
        for (CarBooking booking : bookings) {
            if (booking.getUser().getId().equals(userId)) {
                System.out.println(booking.getCar());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No cars booked for user: " + userId);
        }
    }

    private static void viewAllBookings(CarBookingService carBookingService) {
        CarBooking[] bookings = carBookingService.getBookings();
        if (bookings.length == 0) {
            System.out.println("No bookings found.");
            return;
        }

        for (CarBooking booking : bookings) {
            System.out.println(booking);
        }
    }

    private static void viewAvailableCars(CarService carService, CarBookingService carBookingService, boolean electricOnly) {
        Car[] allCars = carService.getAllCars();
        CarBooking[] bookings = carBookingService.getBookings();
        int count = 0;

        for (Car car : allCars) {
            if (electricOnly && !car.isElectric()) {
                continue;
            }

            if (!isCarBooked(car.getRegNumber(), bookings)) {
                System.out.println(car);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No available " + (electricOnly ? "electric " : "") + "cars found.");
        }
    }

    private static void viewAllUsers(UserService userService) {
        User[] users = userService.getUsers();
        if (users.length == 0) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static boolean isCarBooked(UUID carRegNumber, CarBooking[] bookings) {
        for (CarBooking booking : bookings) {
            if (booking.getCar().getRegNumber().equals(carRegNumber) && !booking.isCanceled()) {
                return true;
            }
        }
        return false;
    }
}
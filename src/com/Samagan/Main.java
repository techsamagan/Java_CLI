package com.Samagan;

import com.Samagan.booking.Booking;

import com.Samagan.booking.BookingDao;
import com.Samagan.booking.BookingService;
import com.Samagan.car.Car;
import com.Samagan.car.CarDao;
import com.Samagan.car.CarService;
import com.Samagan.user.User;
import com.Samagan.user.UserDao;
import com.Samagan.user.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        //Dao
        UserDao userDao = new UserDao();
        CarDao carDao = new CarDao();
        BookingDao bookingDao = new BookingDao();

        UserService userService = new UserService(userDao);
        CarService carService = new CarService(carDao);
        BookingService bookingService = new BookingService(bookingDao, userService, carService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Enter your choice (1-8): ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    bookCar(scanner, bookingService, carService, userService);
                    break;
                case "2":
                    deleteBooking(scanner, bookingService);
                    break;
                case "3":
                    viewAllUserBookedCars(scanner, bookingService);
                    break;
                case "4":
                    viewAllBookings(bookingService);
                    break;
                case "5":
                    viewAvailableCars(bookingService);
                    break;
                case "6":
                    viewAvailableElectricCars(bookingService);
                    break;
                case "7":
                    viewAllUsers(userService);
                    break;
                case "8":
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 8.\n");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=================================");
        System.out.println("   CAR RENTAL MANAGEMENT SYSTEM  ");
        System.out.println("=================================");
        System.out.println("1 - Book Car");
        System.out.println("2 - Delete Booking");
        System.out.println("3 - View All User Booked Cars");
        System.out.println("4 - View All Bookings");
        System.out.println("5 - View Available Cars");
        System.out.println("6 - View Available Electric Cars");
        System.out.println("7 - View All Users");
        System.out.println("8 - Exit");
        System.out.println("=================================");
    }

    private static void bookCar(Scanner scanner, BookingService bookingService, CarService carService, UserService userService) {
        System.out.println("\n--- [Action] Book a Car ---");

        viewAllUsers(userService);
        UUID userId = promptForUUID(scanner, "Enter User ID: ");
        if (userId == null) return;

        viewAvailableCars(bookingService);
        UUID carId = promptForUUID(scanner, "Enter Car ID: ");
        if (carId == null) return;

        LocalDate startDate = promptForDate(scanner, "Enter Start Date (YYYY-MM-DD): ");
        if (startDate == null) return;

        LocalDate endDate = promptForDate(scanner, "Enter End Date (YYYY-MM-DD): ");
        if (endDate == null) return;

        bookingService.bookCar(userId, carId, startDate, endDate);
    }

    private static void deleteBooking(Scanner scanner, BookingService bookingService) {
        System.out.println("\n--- [Action] Delete Booking ---");
        UUID bookingId = promptForUUID(scanner, "Enter Booking ID to cancel: ");
        if (bookingId != null) {
            bookingService.cancelBooking(bookingId);
        }
    }

    private static void viewAllUserBookedCars(Scanner scanner, BookingService bookingService) {
        System.out.println("\n--- [Action] View All User Booked Cars ---");
        UUID userId = promptForUUID(scanner, "Enter User ID: ");
        if (userId == null) return;

        Booking[] userBookings = bookingService.getUserBookings(userId);
        if (userBookings.length == 0) {
            System.out.println("No bookings found for user: " + userId);
            return;
        }

        for (Booking booking : userBookings) {
            if (booking != null) {
                System.out.println("- Car: " + booking.getCar() + " | Status: " + booking.getStatus()
                        + " | From: " + booking.getStartDate() + " to " + booking.getEndDate());
            }
        }
    }

    private static void viewAllBookings(BookingService bookingService) {
        System.out.println("\n--- [Action] View All Bookings ---");
        Booking[] bookings = bookingService.getAllBooking();
        boolean hasBookings = false;

        if (bookings != null) {
            for (Booking b : bookings) {
                if (b != null) {
                    System.out.println(b);
                    hasBookings = true;
                }
            }
        }
        if (!hasBookings) {
            System.out.println("No bookings currently in the system.");
        }
    }

    private static void viewAvailableCars(BookingService carService) {
        System.out.println("\n--- [Action] View Available Cars ---");
        Car[] cars = carService.getAvailableCars();
        displayCars(cars);
    }

    private static void viewAvailableElectricCars(BookingService carService) {
        System.out.println("\n--- [Action] View Available Electric Cars ---");
        Car[] cars = carService.getAvailableElectricCars();
        displayCars(cars);
    }

    private static void viewAllUsers(UserService userService) {
        System.out.println("\n--- [Action] View All Users ---");
        User[] users = userService.getAllUser();
        boolean hasUsers = false;

        if (users != null) {
            for (User u : users) {
                if (u != null) {
                    System.out.println(u);
                    hasUsers = true;
                }
            }
        }
        if (!hasUsers) {
            System.out.println("No users found in the system.");
        }
    }

    private static void displayCars(Car[] cars) {
        boolean hasCars = false;
        if (cars != null) {
            for (Car c : cars) {
                if (c != null) {
                    System.out.println(c);
                    hasCars = true;
                }
            }
        }
        if (!hasCars) {
            System.out.println("No cars found.");
        }
    }

    private static UUID promptForUUID(Scanner scanner, String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid UUID format.");
            return null;
        }
    }

    private static LocalDate promptForDate(Scanner scanner, String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }
}
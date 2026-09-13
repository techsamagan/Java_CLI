package com.Samagan.booking;

import java.io.*;
import java.util.Arrays;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {
    private final File file;

    public CarBookingFileDataAccessService(String filePath) {
        this.file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeToFile(new CarBooking[0]);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create file: " + filePath, e);
            }
        }
    }

    private CarBooking[] readFromFile() {
        if (file.length() == 0) {
            return new CarBooking[0];
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (CarBooking[]) ois.readObject();
        } catch (EOFException e) {
            return new CarBooking[0];
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to read bookings from file", e);
        }
    }

    private void writeToFile(CarBooking[] bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write bookings to file", e);
        }
    }

    @Override
    public CarBooking[] getBookings() {
        return readFromFile();
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        CarBooking[] bookings = readFromFile();
        for (CarBooking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        return null;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        CarBooking[] current = readFromFile();
        CarBooking[] updated = Arrays.copyOf(current, current.length + 1);
        updated[updated.length - 1] = booking;
        writeToFile(updated);
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        CarBooking[] current = readFromFile();
        int targetIndex = -1;

        for (int i = 0; i < current.length; i++) {
            if (current[i].getBookingId().equals(bookingId)) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            return;
        }

        CarBooking[] updated = new CarBooking[current.length - 1];
        int count = 0;
        for (int i = 0; i < current.length; i++) {
            if (i != targetIndex) {
                updated[count++] = current[i];
            }
        }

        writeToFile(updated);
    }
}
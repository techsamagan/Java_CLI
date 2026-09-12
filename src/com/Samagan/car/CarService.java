package com.Samagan.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarService {

    private final CarDao carDao;

    public CarService(CarDao carDao) {
        if (carDao == null) {
            throw new IllegalArgumentException("CarDao cannot be null");
        }
        this.carDao = carDao;
    }

    public Car[] getAllCars() {
        return carDao.getAllCars();
    }

    public Car getCarById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Car ID cannot be null");
        }
        return carDao.getCarById(id);
    }

    public Car getCarByRegNumber(String regNumber) {
        if (regNumber == null || regNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        return carDao.getCarByRegNumber(regNumber.trim());
    }

    public boolean registerCar(String regNum, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric) {
        if (regNum == null || regNum.trim().isEmpty()) {
            System.out.println("Error: Registration number cannot be empty.");
            return false;
        }

        String cleanRegNum = regNum.trim();

        if (carDao.getCarByRegNumber(cleanRegNum) != null) {
            System.out.println("Error: Car with registration number " + cleanRegNum + " already exists.");
            return false;
        }

        if (rentalPricePerDay == null || rentalPricePerDay.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: Rental price must be greater than zero.");
            return false;
        }

        if (brand == null) {
            System.out.println("Error: Brand cannot be null.");
            return false;
        }

        Car newCar = new Car(UUID.randomUUID(), cleanRegNum, rentalPricePerDay, brand, isElectric);
        return carDao.addCar(newCar);
    }

    public Car[] getAllElectricCars() {
        Car[] allCars = carDao.getAllCars();
        if (allCars == null) {
            return new Car[0];
        }

        int electricCount = 0;
        for (Car car : allCars) {
            if (car != null && car.isElectric()) {
                electricCount++;
            }
        }

        Car[] electricCars = new Car[electricCount];
        int index = 0;
        for (Car car : allCars) {
            if (car != null && car.isElectric()) {
                electricCars[index++] = car;
            }
        }
        return electricCars;
    }
}
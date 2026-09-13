package com.Samagan.car;

import java.util.UUID;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public Car[] getAllCars() {
        return carDao.getCars();
    }

    public Car getCar(UUID carId) {
        return carDao.findCarById(carId);
    }
}
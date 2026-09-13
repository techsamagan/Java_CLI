package com.Samagan.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {
    private static final Car[] CARS = new Car[]{
            new Car(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), new BigDecimal("89.00"), Brand.TESLA, true),
            new Car(UUID.fromString("223e4567-e89b-12d3-a456-426614174000"), new BigDecimal("55.00"), Brand.MERCEDES, false),
            new Car(UUID.fromString("323e4567-e89b-12d3-a456-426614174000"), new BigDecimal("60.00"), Brand.AUDI, false)
    };

    @Override
    public Car[] getCars() {
        return CARS;
    }

    @Override
    public Car findCarById(UUID carId) {
        for (Car car : CARS) {
            if (car.getRegNumber().equals(carId)) {
                return car;
            }
        }
        return null;
    }
}
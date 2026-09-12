package com.Samagan.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarDao {

    private static final int CAPACITY = 100;
    private static final Car[] CARS = new Car[CAPACITY];
    private static int carCount = 0;


    static {
        CARS[carCount++] = new Car(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "ABC-1234",
                new BigDecimal("85.00"),
                Brand.TESLA,
                true
        );
        CARS[carCount++] = new Car(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "XYZ-5678",
                new BigDecimal("45.00"),
                Brand.TOYOTA,
                false
        );
        CARS[carCount++] = new Car(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "MNO-9012",
                new BigDecimal("55.00"),
                Brand.FORD,
                false
        );
    }

    public Car[] getAllCars() {
        return CARS;
    }

    public Car getCarById(UUID id){
        if (id == null) {
            return null;
        }
        for(int i=0; i<carCount; i++){
            if(CARS != null && CARS[i].getId().equals(id)){
                return CARS[i];
            }
        }
        return null;
    }

    public Car getCarByRegNumber(String regNumber) {
        if (regNumber == null || regNumber.trim().isEmpty()) {
            return null;
        }
        for (int i = 0; i < carCount; i++) {
            if (CARS[i] != null && CARS[i].getRegNumber().equalsIgnoreCase(regNumber.trim())) {
                return CARS[i];
            }
        }
        return null;
    }

    public boolean addCar(Car car) {
        if (carCount >= CAPACITY) {
            return false;
        }
        CARS[carCount++] = car;
        return true;
    }




}

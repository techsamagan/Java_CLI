package com.Samagan.car;

import java.util.UUID;

public interface CarDao {
    Car[] getCars();
    Car findCarById(UUID carId);
}

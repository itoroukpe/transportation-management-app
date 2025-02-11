package com.example.transportation.controller;

import com.example.transportation.model.Driver;
import com.example.transportation.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return driverService.saveDriver(driver);
    }
}


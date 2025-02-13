package com.example.transportation.controller;

import com.example.transportation.model.Vehicle;
import com.example.transportation.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}


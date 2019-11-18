package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {
    private final CarRepository repository;
    @Autowired
    private PriceClient pricing;
    @Autowired
    private MapsClient maps;

    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll().stream().peek(car -> {
            car.setPrice(pricing.getPrice(car.getId()));
            car.setLocation(maps.getAddress(car.getLocation()));
        }).collect(Collectors.toList());
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        final Optional<Car> optionalCar = repository.findById(id);
        if (!optionalCar.isPresent()) {
            throw new CarNotFoundException("Car with id " + id + " was not found");
        }
        final Car car = optionalCar.get();
        car.setPrice(pricing.getPrice(car.getId()));
        car.setLocation(maps.getAddress(car.getLocation()));
        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(maps.getAddress(car.getLocation()));
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        final Car savedCar = repository.save(car);
        savedCar.setPrice(pricing.getPrice(savedCar.getId()));
        savedCar.setLocation(maps.getAddress(savedCar.getLocation()));
        return repository.save(savedCar);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        final Car car = findById(id);
        repository.delete(car);
    }
}

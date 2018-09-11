package application.services;

import application.repositories.VehicleRepository;
import application.domain.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleService {
    private VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        Assert.notNull(vehicleRepository, "Vehicle repository must not be null.");
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new LinkedList<>();
        vehicleRepository.findAll().forEach(vehicleList::add);
        return vehicleList;
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        Assert.notNull(id, "ID cannot be null.");
        return vehicleRepository.findById(id);
    }

    public boolean addVehicle(Vehicle vehicle) {
        Assert.notNull(vehicle, "Vehicle must not be null");
        if (!vehicleRepository.existsById(vehicle.getId())) {
            vehicleRepository.save(vehicle);
            return true;
        } else {
            return false;
        }
    }
}

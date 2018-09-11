package application.services;

import application.repositories.DriverRepository;
import application.domain.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        Assert.notNull(driverRepository, "Driver repository must not be null.");
        this.driverRepository = driverRepository;
    }

    public List<Driver> getAllDrivers() {
        List<Driver> driversList = new LinkedList<>();
        driverRepository.findAll().forEach(driversList::add);
        return driversList;
    }

    public Optional<Driver> getDriverById(final long id) {
        return driverRepository.findById(id);
    }

    @Transactional
    public boolean addDriver(final Driver driver) {
        Assert.notNull(driver, "Driver must not be null");

        if (!driverRepository.existsById(driver.getId())) {
            driverRepository.save(driver);
            return true;
        } else {
            return false;
        }
    }
}

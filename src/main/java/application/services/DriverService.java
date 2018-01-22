package application.services;

import application.dao.DriverRepository;
import application.domain.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DriverService {
    private DriverRepository driverRepository;

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

    public Optional<Driver> getDriverById(Long id) {
        Assert.notNull(id, "ID cannot be null.");
        return driverRepository.findById(id);
    }

    public boolean addDriver(Driver driver) {
        Assert.notNull(driver, "Driver must not be null");
        if (!driverRepository.existsById(driver.getId())) {
            driverRepository.save(driver);
            return true;
        } else {
            return false;
        }
    }
}

package application.services;

import application.dao.DriverRepository;
import application.dao.TravelRepository;
import application.domain.Driver;
import application.domain.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TravelService {
    private TravelRepository travelRepository;
    private DriverRepository driverRepository;

    @Autowired
    public TravelService(@Autowired TravelRepository travelRepository, @Autowired DriverRepository driverRepository) {
        Assert.notNull(travelRepository, "TravelRepository cannot be null.");
        Assert.notNull(driverRepository, "DriverRepository cannot be null.");
        this.travelRepository = travelRepository;
        this.driverRepository = driverRepository;
    }

    public List<Travel> getAllTravels() {
        List<Travel> travels = new LinkedList<>();
        travelRepository.findAll().forEach(travels::add);
        return travels;
    }

    public Optional<Travel> getTravelById(Long id) {
        Assert.notNull(id, "ID must not be null.");
        return travelRepository.findById(id);
    }

    public List<Travel> getTravelsByDriverId(Long id) {
        Assert.notNull(id, "ID must not be null.");

        Optional<Driver> driver = driverRepository.findById(id);

        if (driver.isPresent()) {
            return getAllTravels().stream()
                    .filter(travel -> travel.getDriver() != null && travel.getDriver().equals(driver.get()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<Travel> getTravelsByMonth(Month month) {
        Assert.notNull(month, "Month must not be null.");
        return getAllTravels().stream()
                .filter(travel -> travel.getDepartureDate().getMonth().equals(month))
                .collect(Collectors.toList());
    }

    public List<Travel> getTravelsByDriverAndMonth(Long id, Month month) {
        Assert.notNull(id, "Id must not be null.");
        Assert.notNull(month, "Month must not be null.");

        Optional<Driver> driver = driverRepository.findById(id);

        if (driver.isPresent()) {
            return getAllTravels().stream()
                    .filter(
                            travel -> travel.getDriver() != null
                                    && travel.getDriver().equals(driver.get())
                                    && travel.getDepartureDate().getMonth().equals(month)
                    ).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public boolean addTravel(Travel travel) {
        Assert.notNull(travel, "Travel must not be null.");
        if (!travelRepository.existsById(travel.getId())) {
            travelRepository.save(travel);
            return true;
        } else {
            return false;
        }
    }

    public boolean setDriverForTravelWithId(Long travelId, Long driverId) {
        Assert.notNull(travelId, "Travel ID must not be null.");
        Assert.notNull(driverId, "Driver ID must not be null.");
        if (travelRepository.existsById(travelId) && driverRepository.existsById(driverId)) {
            travelRepository.findById(travelId)
                    .ifPresent(t -> t.setDriver(driverRepository.findById(driverId).get()));
            return true;
        } else {
            return false;
        }
    }
}

package application.services;

import application.dao.TravelRepository;
import application.domain.Driver;
import application.domain.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TravelService {
    private TravelRepository travelRepository;

    @Autowired
    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
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

    public List<Travel> getTravelsByDriver(Driver driver) {
        Assert.notNull(driver, "Driver must not be null.");
        return getAllTravels().stream()
                .filter(travel -> travel.getDriver() != null && travel.getDriver().equals(driver))
                .collect(Collectors.toList());
    }

    public List<Travel> getTravelsByMonth(Month month) {
        Assert.notNull(month, "Month must not be null.");
        return getAllTravels().stream()
                .filter(travel -> travel.getDepartureDate().getMonth().equals(month))
                .collect(Collectors.toList());
    }

    public List<Travel> getTravelsByDriverAndMonth(Driver driver, Month month) {
        Assert.notNull(driver, "Driver must not be null.");
        Assert.notNull(month, "Month must not be null.");
        return getAllTravels().stream()
                .filter(
                        travel -> travel.getDriver() != null
                                && travel.getDriver().equals(driver)
                                && travel.getDepartureDate().getMonth().equals(month)
                ).collect(Collectors.toList());
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

    public boolean setDriverForTravelWithId(Long id, Driver driver) {
        Assert.notNull(id, "ID must not be null.");
        Assert.notNull(driver, "Driver must not be null.");
        if (travelRepository.existsById(id)) {
            travelRepository.findById(id).ifPresent(t -> t.setDriver(driver));
            return true;
        } else {
            return false;
        }
    }
}

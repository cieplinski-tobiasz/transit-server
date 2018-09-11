package application.services;

import application.repositories.TravelRepository;
import application.domain.Driver;
import application.domain.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TravelService {
    private final TravelRepository travelRepository;

    @Autowired
    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public List<Travel> getAllTravels() {
        List<Travel> travels = new LinkedList<>();
        travelRepository.findAll().forEach(travels::add);
        return travels;
    }

    public Optional<Travel> getTravelById(final long id) {
        return travelRepository.findById(id);
    }

    public List<Travel> getTravelsByDriver(final Driver driver) {
        Assert.notNull(driver, "Driver must not be null.");

        return getAllTravels().stream()
                .filter(travel -> travel.hasDriver(driver))
                .collect(Collectors.toList());
    }

    public List<Travel> getTravelsByMonth(final Month month) {
        Assert.notNull(month, "Month must not be null.");

        return getAllTravels().stream()
                .filter(travel -> travel.isInMonth(month))
                .collect(Collectors.toList());
    }

    public List<Travel> getTravelsByDriverAndMonth(final Driver driver, final Month month) {
        Assert.notNull(driver, "Driver must not be null.");
        Assert.notNull(month, "Month must not be null.");

        return getAllTravels().stream()
                .filter(travel -> travel.hasDriver(driver))
                .filter(travel -> travel.isInMonth(month))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean addTravel(final Travel travel) {
        Assert.notNull(travel, "Travel must not be null.");

        if (!travelRepository.existsById(travel.getId())) {
            travelRepository.save(travel);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean setDriverForTravelWithId(final long travelId, final Driver driver) {
        Assert.notNull(driver, "Driver must not be null.");

        if (travelRepository.existsById(travelId)) {
            travelRepository.findById(travelId)
                    .ifPresent(t -> t.setDriver(driver));
            return true;
        } else {
            return false;
        }
    }
}

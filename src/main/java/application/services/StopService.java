package application.services;

import application.repositories.StopRepository;
import application.domain.Stop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class StopService {
    private final StopRepository stopRepository;

    @Autowired
    public StopService(StopRepository stopRepository) {
        Assert.notNull(stopRepository, "Stop repository cannot be null.");

        this.stopRepository = stopRepository;
    }

    public List<Stop> getAllStops() {
        List<Stop> stopList = new LinkedList<>();
        stopRepository.findAll().forEach(stopList::add);
        return stopList;
    }

    public Optional<Stop> getStopById(final long id) {
        return stopRepository.findById(id);
    }

    @Transactional
    public boolean addStop(final Stop stop) {
        Assert.notNull(stop, "Stop cannot be null.");

        if (!stopRepository.existsById(stop.getId())) {
            stopRepository.save(stop);
            return true;
        } else {
            return false;
        }
    }
}

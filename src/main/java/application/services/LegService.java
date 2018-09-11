package application.services;

import application.repositories.LegRepository;
import application.domain.Leg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class LegService {
    private final LegRepository legRepository;

    @Autowired
    public LegService(LegRepository legRepository) {
        this.legRepository = legRepository;
    }

    public List<Leg> getAllLegs() {
        List<Leg> legList = new LinkedList<>();
        legRepository.findAll().forEach(legList::add);
        return legList;
    }

    public Optional<Leg> getLegById(final long id) {
        return legRepository.findById(id);
    }

    @Transactional
    public boolean addLeg(final Leg leg) {
        Assert.notNull(leg, "Leg cannot be null.");

        if (!legRepository.existsById(leg.getId())) {
            legRepository.save(leg);
            return true;
        } else {
            return false;
        }
    }
}

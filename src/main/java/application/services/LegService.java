package application.services;

import application.dao.LegRepository;
import application.domain.Leg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LegService {
    private LegRepository legRepository;

    @Autowired
    public LegService(LegRepository legRepository) {
        this.legRepository = legRepository;
    }

    public List<Leg> getAllLegs() {
        List<Leg> legList = new LinkedList<>();
        legRepository.findAll().forEach(legList::add);
        return legList;
    }

    public Optional<Leg> getLegById(Long id) {
        Assert.notNull(id, "ID cannot be null.");
        return legRepository.findById(id);
    }

    public boolean addLeg(Leg leg) {
        Assert.notNull(leg, "Leg cannot be null.");
        if (!legRepository.existsById(leg.getId())) {
            legRepository.save(leg);
            return true;
        } else {
            return false;
        }
    }
}

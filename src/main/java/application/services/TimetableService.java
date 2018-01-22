package application.services;

import application.dao.TimetableRepository;
import application.domain.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TimetableService {
    private TimetableRepository timetableRepository;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        Assert.notNull(timetableRepository, "Timetable repository cannot be null.");
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> getAllTimetables() {
        List<Timetable> timetableList = new LinkedList<>();
        timetableRepository.findAll().forEach(timetableList::add);
        return timetableList;
    }

    public Optional<Timetable> getTimetableById(Long id) {
        Assert.notNull(id, "ID cannot be null.");
        return timetableRepository.findById(id);
    }

    public boolean addTimetable(Timetable timetable) {
        Assert.notNull(timetable, "Timetable cannot be null.");
        if (!timetableRepository.existsById(timetable.getId())) {
            timetableRepository.save(timetable);
            return true;
        } else {
            return false;
        }
    }
}

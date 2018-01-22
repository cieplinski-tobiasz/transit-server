package application.dao;

import application.Main;
import application.domain.Distance;
import application.domain.Leg;
import application.domain.Location;
import application.domain.Stop;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class LegRepositoryIntegrationTest {
    @Autowired
    private LegRepository legRepository;
    @Autowired
    private StopRepository stopRepository;

    @BeforeEach
    void setUp() {
        legRepository.deleteAll();
        stopRepository.deleteAll();
    }

    @Test
    @Transactional
    public void shouldSaveLegGivenStopsArePresentIntegrationTest() throws Exception {
        Stop from = new Stop("from", new Location("from-location"));
        Stop to = new Stop("to", new Location("to-location"));
        stopRepository.save(from);
        stopRepository.save(to);
        Leg leg = new Leg(from, to, new Distance(20));
        legRepository.save(leg);

        Optional<Leg> result = legRepository.findById(leg.getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(leg, result)
        );
    }

    @Test
    @Transactional
    public void shouldNotSaveLegGivenStopsNotPresentIntegrationTest() throws Exception {
        Stop from = new Stop("from", new Location("from-location"));
        Stop to = new Stop("to", new Location("to-location"));
        Leg leg = new Leg(from, to, new Distance(20));

        assertThrows(IllegalArgumentException.class, () -> legRepository.save(leg));
    }

    @Test
    @Transactional
    public void shouldDeleteLegAndNotDeleteStopsIntegrationTest() throws Exception {
        Stop from = new Stop("from", new Location("from-location"));
        Stop to = new Stop("to", new Location("to-location"));
        stopRepository.save(from);
        stopRepository.save(to);
        Leg leg = new Leg(from, to, new Distance(20));
        legRepository.save(leg);

        legRepository.delete(leg);

        assertAll(
                () -> assertFalse(legRepository.existsById(leg.getId())),
                () -> assertTrue(stopRepository.existsById(from.getId())),
                () -> assertEquals(from, stopRepository.findById(from.getId())),
                () -> assertTrue(stopRepository.existsById(to.getId())),
                () -> assertEquals(to, stopRepository.findById(to.getId()))
        );
    }

}
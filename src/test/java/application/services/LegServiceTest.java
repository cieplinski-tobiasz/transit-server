package application.services;

import application.Main;
import application.repositories.LegRepository;
import application.repositories.StopRepository;
import application.domain.Distance;
import application.domain.Leg;
import application.domain.Location;
import application.domain.Stop;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class LegServiceTest {
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LegRepository legRepository;
    private List<Leg> legs;

    @BeforeEach
    void setUp() {
        legRepository.deleteAll();
        stopRepository.deleteAll();

        List<Stop> stops = new LinkedList<Stop>() {{
            add(new Stop("name0", "desc0", new Location("loc0")));
            add(new Stop("name1", "desc1", new Location("loc1")));
            add(new Stop("name2", "desc2", new Location("loc2")));
        }};
        stopRepository.saveAll(stops);

        legs = new LinkedList<Leg>() {{
            add(new Leg(stops.get(0), stops.get(1), new Distance(10)));
            add(new Leg(stops.get(1), stops.get(2), new Distance(10)));
        }};
        legRepository.saveAll(legs);
    }

    @AfterEach
    void cleanUp() {
        legRepository.deleteAll();
        stopRepository.deleteAll();
    }

    @Test
    void getAllLegsTest() {
        LegService legService = new LegService(legRepository);

        List<Leg> result = legService.getAllLegs();

        Assert.assertThat(result, IsIterableContainingInAnyOrder.containsInAnyOrder(legs.toArray(new Leg[2])));
    }

    @Test
    void getLegByIdGivenLegPresentTest() {
        LegService legService = new LegService(legRepository);

        Optional<Leg> result = legService.getLegById(legs.get(0).getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(legs.get(0), result.get())
        );
    }

    @Test
    void getLegByIdGivenLegIsNotPresentTest() {
        LegService legService = new LegService(legRepository);
        Leg noSuchLeg = new Leg(
                new Stop("no such stop", new Location("loc")),
                new Stop("no such stop either", new Location("loc")),
                new Distance(10));

        Optional<Leg> result = legService.getLegById(noSuchLeg.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void addLegGivenLegIsNotPresentTest() {
        LegService legService = new LegService(legRepository);
        Stop from = new Stop("no such stop", new Location("loc"));
        Stop to = new Stop("no such stop either", new Location("loc"));
        stopRepository.save(from);
        stopRepository.save(to);
        Leg noSuchLeg = new Leg(from, to, new Distance(10));

        boolean result = legService.addLeg(noSuchLeg);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(noSuchLeg, legService.getLegById(noSuchLeg.getId()).get())
        );
    }

    @Test
    void addStopGivenStopIsPresentTest() {
        LegService legService = new LegService(legRepository);

        boolean result = legService.addLeg(legs.get(0));

        assertFalse(result);
    }

    @Test
    void addStopGivenStopIsNullTest() {
        LegService legService = new LegService(legRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> legService.addLeg(null)
        );
    }




}
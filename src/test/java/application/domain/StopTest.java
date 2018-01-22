package application.domain;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class StopTest {
    @Mock
    private Location location;

    @Test
    public void getDescriptionWhenDescriptionIsNullUsingThreeArgumentConstructorTest() throws Exception {
        Stop stop = new Stop("name", null, location);

        Optional<String> result = stop.getDescription();

        assertFalse(result.isPresent());
    }

    @Test
    public void getDescriptionWhenDescriptionIsNullUsingTwoArgumentConstructorTest() throws Exception {
        Stop stop = new Stop("name", location);

        Optional<String> result = stop.getDescription();

        assertFalse(result.isPresent());
    }

    @Test
    public void getDescriptionWhenDescriptionIsNotNullUsingThreeArgumentConstructorTest() throws Exception {
        Stop stop = new Stop("name", "description", location);

        Optional<String> result = stop.getDescription();

        assertTrue(result.isPresent());
    }
}
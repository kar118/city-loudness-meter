package pl.pk.project.pz.sound_intensity.manager;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.pk.project.pz.sound_intensity.dao.SoundAndLocationRepo;
import pl.pk.project.pz.sound_intensity.dao.entity.SoundAndLocation;
import pl.pk.project.pz.sound_intensity.pojo.FeatureCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.*;
import pl.pk.project.pz.sound_intensity.pojo.convert.convertToFeatureCollection;


@RunWith(MockitoJUnitRunner.class)
public class SoundAndLocationManagerTest {

    @Mock
    SoundAndLocationRepo soundAndLocationRepo;

    @InjectMocks
    SoundAndLocationManager soundAndLocationManager;

    @BeforeAll
    public void setUp(){
        System.out.println("SoundAndLocationManagerTest started");
    }

    @AfterAll
    public void tearDown(){
        System.out.println("SoundAndLocationManagerTest finished");
    }

    @Test
    public void findById() {

        final Long id = anyLong();

        final SoundAndLocation soundAndLocation = new SoundAndLocation();
        soundAndLocation.setId(id);

        //given
        given(soundAndLocationRepo.findById(id)).willReturn(java.util.Optional.of(soundAndLocation));
        //when
        Optional expectedResult =  soundAndLocationManager.findById(id);
        //then
        assertEquals(expectedResult,java.util.Optional.of(soundAndLocation));
    }

    @Test
    public void findAll() {

        double latitude = 10.0;
        double longitude = 20.0;
        double magnitude = 30.0;

        List<SoundAndLocation> soundAndLocationList = new ArrayList<>();

        soundAndLocationList.add(new SoundAndLocation(latitude,longitude,magnitude,LocalDateTime.now()));
        soundAndLocationList.add(new SoundAndLocation(latitude,longitude,magnitude,LocalDateTime.now()));
        soundAndLocationList.add(new SoundAndLocation(latitude,longitude,magnitude,LocalDateTime.now()));
        soundAndLocationList.add(new SoundAndLocation(latitude,longitude,magnitude,LocalDateTime.now()));

        FeatureCollection featureCollection = convertToFeatureCollection.convertToFeatureCollection(soundAndLocationList);

        //given
        given(soundAndLocationRepo.findAll()).willReturn(soundAndLocationList);
        //when
        FeatureCollection featureCollectionResult = soundAndLocationManager.findAll();
        //then
        assertThat(featureCollectionResult.equals(featureCollection));
    }

    @Test
    public void save() {

        double latitude = 10.0;
        double longitude = 20.0;
        double magnitude = 30.0;

        final SoundAndLocation soundAndLocation = new SoundAndLocation(latitude,longitude,magnitude,LocalDateTime.now());

        assertThat(soundAndLocation).isNotNull();
    }

    @Test
    public void deleteById() {

        final Long id = 1L;

        soundAndLocationManager.deleteById(id);
        soundAndLocationManager.deleteById(id);

        verify(soundAndLocationRepo,times(2)).deleteById(id);
    }

    @Test
    public void getLocation() {
    }

    @Test
    public void getPointsBetweenDate() {
    }
}
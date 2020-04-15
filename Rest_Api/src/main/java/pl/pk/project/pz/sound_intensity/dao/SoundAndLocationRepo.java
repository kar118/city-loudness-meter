package pl.pk.project.pz.sound_intensity.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pk.project.pz.sound_intensity.dao.entity.SoundAndLocation;


import javax.persistence.Query;
import java.time.LocalDateTime;


@Repository
public interface SoundAndLocationRepo extends CrudRepository<SoundAndLocation,Long> {

    Iterable<SoundAndLocation> findAllByDateTimeBetween(LocalDateTime first, LocalDateTime second);


}

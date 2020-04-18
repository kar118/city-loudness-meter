package pl.pk.project.pz.sound_intensity.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.pk.project.pz.sound_intensity.dao.entity.SoundAndLocation;
import pl.pk.project.pz.sound_intensity.manager.SoundAndLocationManager;
import pl.pk.project.pz.sound_intensity.pojo.FeatureCollection;

import javax.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api/location")
public class SoundAndLocationApi {

    private SoundAndLocationManager soundAndLocationManager;

    @Autowired
    public SoundAndLocationApi(SoundAndLocationManager soundAndLocationManager){
        this.soundAndLocationManager=soundAndLocationManager;
    }

    @GetMapping("/all")
    public FeatureCollection getAll(@RequestParam(name = "fromDate",required = false)Long fromDate,
                                    @RequestParam(name ="toDate",required = false)Long toDate)throws Exception{
        if(fromDate==null&&toDate==null){
            return soundAndLocationManager.findAll();
        }else{
            return soundAndLocationManager.getPointsBetweenDate(fromDate,toDate);
        }
    }

    @GetMapping
    public Optional<SoundAndLocation> getById(@RequestParam Long id){
        return soundAndLocationManager.findById(id);
    }

    @PostMapping(consumes = (MediaType.APPLICATION_JSON_VALUE))
    public SoundAndLocation addNewPoint(@Valid @RequestBody SoundAndLocation soundAndLocation){
        return soundAndLocationManager.save(soundAndLocation);
    }

    @DeleteMapping
    public void deletePoint(@RequestParam Long id){
        soundAndLocationManager.deleteById(id);
    }

    @GetMapping("/last")
    public List<FeatureCollection> getLocation(){
        return soundAndLocationManager.getLocation();
    }

    @GetMapping("/true")
    public boolean GetTrue(@RequestParam Long id){
        return true;
    }

}

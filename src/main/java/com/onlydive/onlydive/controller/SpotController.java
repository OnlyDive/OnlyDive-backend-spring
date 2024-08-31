package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.SpotRequest;
import com.onlydive.onlydive.dto.SpotResponse;
import com.onlydive.onlydive.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spot")
@Slf4j
public class SpotController {

    private final SpotService spotService;

    @PostMapping("/create")
    public ResponseEntity<SpotResponse> createSpot(@RequestBody SpotRequest spotDto){
        return status(HttpStatus.CREATED).body(spotService.createSpot(spotDto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpotResponse> getSpotById(@PathVariable Long id){
        return status(HttpStatus.OK).body(spotService.getSpotResponseById(id));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<SpotResponse>> getAllSpots(){
        return status(HttpStatus.OK).body(spotService.getAllSpots());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSpotById(@PathVariable Long id){
        spotService.deleteSpotByName(id);
        return status(HttpStatus.NO_CONTENT).body("Deleted spot");
    }

}

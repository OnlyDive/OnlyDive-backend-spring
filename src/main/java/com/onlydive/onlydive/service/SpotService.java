package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.SpotDto;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.mapper.SpotMapper;
import com.onlydive.onlydive.model.Spot;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.repository.SpotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final SpotMapper spotMapper;
    private final AuthService authService;

    public SpotDto createSpot(SpotDto spotDto) {
        User user =  authService.getCurrentUser();
        Spot spot = spotMapper.mapToSpot(spotDto,user);

        spotRepository.save(spot);

        return spotMapper.mapToResponse(spot);
    }

    public SpotDto getSpotByName(String name) {
        Spot spot = spotRepository.findByName(name).orElseThrow(
                () -> new SpringOnlyDiveException("Spot with name " + name + " not found")
        );
        return spotMapper.mapToResponse(spot);
    }

    public Spot getSpotById(Long id) {
        return spotRepository.findById(id).orElseThrow(
                () -> new SpringOnlyDiveException("Spot with id " + id + " not found")
        );
    }

    public List<SpotDto> getAllSpots() {
        return spotRepository.findAll()
                .stream()
                .map(spotMapper::mapToResponse)
                .toList();
    }


    public void deleteSpotByName(Long id) {
        Spot spot = spotRepository.findById(id).orElseThrow(
                () -> new SpringOnlyDiveException("Spot with id " + id + " not found")
        );

        spotRepository.deleteById(id);
    }
}

package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.SpotRequest;
import com.onlydive.onlydive.dto.SpotResponse;
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

    public SpotResponse createSpot(SpotRequest spotDto) {
        User user =  authService.getCurrentUser();
        Spot spot = spotMapper.mapToSpot(spotDto,user);

        spotRepository.save(spot);

        return spotMapper.mapToResponse(spot);
    }

    public SpotResponse getSpotResponseById(Long id) {
        Spot spot = spotRepository.findById(id).orElseThrow(
                () -> new SpringOnlyDiveException("Spot with id " + id + " not found")
        );
        return spotMapper.mapToResponse(spot);
    }

    public Spot getSpotById(Long id) {
        return spotRepository.findById(id).orElseThrow(
                () -> new SpringOnlyDiveException("Spot with id " + id + " not found")
        );
    }

    public List<SpotResponse> getAllSpots() {
        return spotRepository.findAll()
                .stream()
                .map(spotMapper::mapToResponse)
                .toList();
    }


    public void deleteSpotByName(Long id) {
        spotRepository.findById(id).orElseThrow(
                () -> new SpringOnlyDiveException("Spot with id " + id + " not found")
        );
        spotRepository.deleteById(id);
    }
}

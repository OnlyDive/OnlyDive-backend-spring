package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.SpotCommentRequest;
import com.onlydive.onlydive.dto.SpotCommentResponse;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.mapper.SpotCommentMapper;
import com.onlydive.onlydive.model.Spot;
import com.onlydive.onlydive.model.SpotComment;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.repository.SpotCommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SpotCommentService {
    private final SpotCommentMapper spotCommentMapper;
    private final AuthService authService;
    private final SpotService spotService;
    private final SpotCommentRepository spotCommentRepository;

    public SpotCommentResponse createSpotComment(SpotCommentRequest request) {
        User user = authService.getCurrentUser();
        Spot spot = spotService.getSpotById(request.SpotId());

        SpotComment comment = spotCommentMapper.mapToSpotComment(request,spot,user);

        spotCommentRepository.save(comment);

        return spotCommentMapper.mapToSpotCommentResponse(comment);
    }

    public void deleteSpotComment(Long id) {
        spotCommentRepository.findById(id).orElseThrow(
                () -> new SpringOnlyDiveException("Comment with id " + id + " not found")
        );
        spotCommentRepository.deleteById(id);
    }

    public SpotCommentResponse getSingleSpotComment(Long id) {
        return spotCommentMapper.mapToSpotCommentResponse(
                spotCommentRepository.findById(id).orElseThrow(
                        () -> new SpringOnlyDiveException("Comment with id " + id + " not found")
                )
        );
    }

    public List<SpotCommentResponse> getSpotCommentBySpotIdByAmount(Long spotId, Integer amount) {
        Spot spot = spotService.getSpotById(spotId);

        Pageable pageable = PageRequest.of(0,amount);

        return spotCommentRepository.findAllBySpot(spot,pageable).stream()
                .map(spotCommentMapper::mapToSpotCommentResponse)
                .toList();
    }
}

package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.SpotCommentDto;
import com.onlydive.onlydive.service.SpotCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spotComment")
@Slf4j
public class SpotCommentController {

    private final SpotCommentService spotCommentService;

    @PostMapping("/create")
    public ResponseEntity<SpotCommentDto> createSpotComment(@RequestBody SpotCommentDto request) {
        return new ResponseEntity<>(spotCommentService.createSpotComment(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSpotComment(@PathVariable Long id) {

        spotCommentService.deleteSpotComment(id);
        return new ResponseEntity<>("spot's comment has been deleted",HttpStatus.NO_CONTENT);
    }


    @GetMapping("/get/{spotId}/{pageNumber}")
    public ResponseEntity<List<SpotCommentDto>> getSpotCommentsBySpotIdByPageNumber
            (@PathVariable Long spotId,@PathVariable Integer pageNumber) {
        return new ResponseEntity<>(spotCommentService.getSpotCommentBySpotIdByPage(spotId,pageNumber),
                HttpStatus.OK);
    }

}

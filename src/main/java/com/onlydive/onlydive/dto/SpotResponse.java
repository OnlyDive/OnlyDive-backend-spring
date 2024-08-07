package com.onlydive.onlydive.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record SpotResponse(Long id , String name, Double longitude, Double latitude,
                           Long commentCount, String creatorUsername, String creationDate) {
}

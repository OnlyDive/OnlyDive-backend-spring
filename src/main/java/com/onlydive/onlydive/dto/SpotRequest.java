package com.onlydive.onlydive.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record SpotRequest(String name, Double longitude, Double latitude) {
}

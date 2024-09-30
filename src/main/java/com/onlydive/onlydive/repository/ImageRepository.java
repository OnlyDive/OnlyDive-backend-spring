package com.onlydive.onlydive.repository;

import com.onlydive.onlydive.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

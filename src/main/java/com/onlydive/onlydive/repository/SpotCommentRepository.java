package com.onlydive.onlydive.repository;

import com.onlydive.onlydive.model.Spot;
import com.onlydive.onlydive.model.SpotComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface SpotCommentRepository extends JpaRepository<SpotComment, Long> {

    List<SpotComment> findAllBySpot(Spot spot, Pageable pageable);
}

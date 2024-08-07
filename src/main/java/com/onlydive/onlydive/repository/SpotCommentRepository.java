package com.onlydive.onlydive.repository;

import com.onlydive.onlydive.model.SpotComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotCommentRepository extends JpaRepository<SpotComment, Long> {
}

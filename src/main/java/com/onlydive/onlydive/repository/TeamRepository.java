package com.onlydive.onlydive.repository;

import com.onlydive.onlydive.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}

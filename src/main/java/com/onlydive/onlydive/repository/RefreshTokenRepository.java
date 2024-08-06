package com.onlydive.onlydive.repository;

import com.onlydive.onlydive.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findOneByToken(String token);

    void deleteByToken(String token);
}

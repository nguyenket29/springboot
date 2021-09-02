package com.hau.ketnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hau.ketnguyen.entity.SecureTokenEntity;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureTokenEntity, Long> {
	SecureTokenEntity findByToken(String token);
	Long removeByToken(String token);
}

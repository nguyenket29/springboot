package com.hau.ketnguyen.service.impl;

import org.springframework.stereotype.Service;

import com.hau.ketnguyen.entity.SecureTokenEntity;
import com.hau.ketnguyen.repository.SecureTokenRepository;
import com.hau.ketnguyen.service.ISecureTokenService;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

@Service
public class SecureTokenServiceImpl implements ISecureTokenService {
	private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
	private static final Charset US_ASCII = Charset.forName("US-ASCII");

	@Value("${jdj.secure.token.validity}")
	private int tokenValidityInSeconds;

	@Autowired
	private SecureTokenRepository secureTokenRepository;

	@Override
	public SecureTokenEntity createSecureToken() {
		String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
		SecureTokenEntity secureToken = new SecureTokenEntity();
		secureToken.setToken(tokenValue);
		secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenValidityInSeconds()));
		this.saveSecureToken(secureToken);
		return secureToken;
	}

	public int getTokenValidityInSeconds() {
		return tokenValidityInSeconds;
	}

	@Override
	public void saveSecureToken(SecureTokenEntity secureTokenEntity) {
		secureTokenRepository.save(secureTokenEntity);
	}

	@Override
	public SecureTokenEntity findByToken(String token) {
		return secureTokenRepository.findByToken(token);
	}

	@Override
	public void removeToken(SecureTokenEntity token) {
		secureTokenRepository.delete(token);
	}

	@Override
	public void removeTokenByToken(String token) {
		secureTokenRepository.removeByToken(token);
	}

}

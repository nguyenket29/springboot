package com.hau.ketnguyen.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hau.ketnguyen.entity.CustomUser;
import com.hau.ketnguyen.entity.RoleEntity;
import com.hau.ketnguyen.entity.UserEntity;
import com.hau.ketnguyen.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		boolean enabled = !userEntity.isAccountVerified();

//		UserDetails user = User.withUsername(userEntity.getEmail()).password(userEntity.getPassword()).disabled(enabled)
//				.authorities(getAuthorities(userEntity)).build();
		CustomUser users = null;
		if (!enabled) {
			users = new CustomUser(userEntity.getEmail(), userEntity.getPassword(), getAuthorities(userEntity));
			users.setFirstName(userEntity.getFirstName());
			users.setLastName(userEntity.getLastName());
		}

		return users;
	}

	private Collection<GrantedAuthority> getAuthorities(UserEntity userEntity) {
		Set<RoleEntity> userRoles = userEntity.getRoles();
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(userRoles.size());
		for (RoleEntity item : userRoles) {
			authorities.add(new SimpleGrantedAuthority(item.getCode().toUpperCase()));
		}
		return authorities;
	}
}

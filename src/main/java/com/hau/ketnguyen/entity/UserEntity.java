package com.hau.ketnguyen.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String email;

	private String password;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private boolean accountVerified;

	@OneToMany(mappedBy = "user")
	private Set<SecureTokenEntity> tokens;

	private boolean loginDisabled;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.EAGER)
	@JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles = new HashSet<RoleEntity>();

	public void addUserRole(RoleEntity roleEntity) {
		roles.add(roleEntity);
		roleEntity.getUsers().add(this);
	}

	public void removeUserRole(RoleEntity roleEntity) {
		roles.remove(roleEntity);
		roleEntity.getUsers().remove(this);
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	public boolean isLoginDisabled() {
		return loginDisabled;
	}

	public void setLoginDisabled(boolean loginDisabled) {
		this.loginDisabled = loginDisabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public Set<SecureTokenEntity> getTokens() {
		return tokens;
	}

	public void setTokens(Set<SecureTokenEntity> tokens) {
		this.tokens = tokens;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserEntity() {
	}

	public boolean isAccountVerified() {
		return accountVerified;
	}

	public void setAccountVerified(boolean accountVerified) {
		this.accountVerified = accountVerified;
	}

}

package com.hau.ketnguyen.entity;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserData implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{registration.validation.firstName}")
	private String firstName;

	@NotEmpty(message = "{registration.validation.lastName}")
	private String lastName;
	
	@NotEmpty(message = "{registration.validation.email}")
	@Email(message = "{registration.validation.email}")
	private String email;

	@NotEmpty(message = "{registration.validation.password}")
	private String password;

	public String getFirstName() {
		return firstName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserData(@NotEmpty(message = "First name can not be empty") String firstName,
			@NotEmpty(message = "Last name can not be empty") String lastName,
			@NotEmpty(message = "Email can not be empty") @Email(message = "Please provide a valid email id") String email,
			@NotEmpty(message = "Password can not be empty") String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public UserData() {
		super();
	}

}

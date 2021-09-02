package com.hau.ketnguyen.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.hau.ketnguyen.security.CustomAccessDeniedHandler;
import com.hau.ketnguyen.security.CustomLogoutHandler;
import com.hau.ketnguyen.security.CustomSuccessHandler;
import com.hau.ketnguyen.security.LoginAuthenticationFailureHandler;
import com.hau.ketnguyen.service.impl.CustomUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
    private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/register", "/login").permitAll()
				.antMatchers("/account/**").hasAnyAuthority("USER","ADMIN")
				.and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
				.and()
				
				//remember-me configuration
				.rememberMe()
					.tokenRepository(persistentTokenRepository())
					.rememberMeCookieName("custom-remember-me-cookie")
					.userDetailsService(userDetailsService)
				.and()
				
				//Login configuration
				.formLogin()
					.defaultSuccessUrl("/")
					.loginPage("/login")
					.failureUrl("/login?error=true")
					.successHandler(successHandler())
					.failureHandler(failureHandler())
				.and()
				
				//Logout configuration
				.logout()
					.deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/login")
					.logoutSuccessHandler(logoutSuccessHandler());
		http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN");
	}

	@Bean
	public CustomSuccessHandler successHandler() {
		return new CustomSuccessHandler();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutHandler();
	}

	@Bean
	public LoginAuthenticationFailureHandler failureHandler() {
		LoginAuthenticationFailureHandler failureHandler = new LoginAuthenticationFailureHandler();
		failureHandler.setDefaultFailureUrl("/login?error=true");
		return failureHandler;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/resources/**", "/static/**");
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public CustomUserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}
	
	@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
}
package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경설정 파일임을 의미
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 사용가능 하도록
public class SecurityConfig {

	@Bean //스프링 시큐리티의 세부 설정은 SecurityFilterChain 빈을 생성하여 설정
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
		
		//스프링 시큐리티가 CSRF 처리시 H2 콘솔은 예외로 처리
		.and() //http 객체의 설정을 이어서 할 수 있게 하는 메서드
			.csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")) ///h2-console/로 시작하는 URL은 CSRF 검증을 하지 않는다는 설정
		
		// H2 콘솔의 화면 frame 구조로 작성됨, URL 요청시 X-Frame-Options 헤더값을 sameorigin으로 설정하여 오류가 발생하지 않도록 함
		.and()
			.headers()
			.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
		
		// 스프링 시큐리티에 로그인 URL을 등록	
		.and()
			.formLogin()
			.loginPage("/user/login")
			.defaultSuccessUrl("/")
			
		// 로그아웃 URL을 /user/logout으로 설정하고 로그아웃이 성공하면 루트(/) 페이지로 이동, 생성된 사용자 세션 삭제
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			;
			
		return http.build(); 
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	// AuthenticationManager는 스프링 시큐리티의 인증을 담당, 
	// AuthenticationManager 빈 생성시 스프링의 내부 동작으로 인해 위에서 작성한 UserSecurityService와 PasswordEncoder가 자동으로 설정
	@Bean
	AuthenticationManager authenSticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
}

package com.example.demo.user;

import lombok.Getter;

// 인증후에 사용자에게 부여할 권한, 열거 자료형(enum)으로 작성

@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	UserRole(String value){
		this.value = value;
	}
	
	private String value;
	
}

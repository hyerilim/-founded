package com.example.demo.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm
								, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}
		
		// 만약 2개의 값이 일치하지 않을 경우
		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			// bindingResult.rejectValue(필드명, 오류코드, 에러메시지)
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		
		// 사용자ID 또는 이메일 주소가 동일할 경우에는 DataIntegrityViolationException이 발생 // 예외처리
		try {
		userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			// bindingResult.reject(오류코드, 오류메시지)
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		
		return "redirect:/";
	}
	
	//로그인 화면
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
	
	
}

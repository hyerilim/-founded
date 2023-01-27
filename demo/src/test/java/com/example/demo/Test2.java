package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.answer.AnswerRepository;
import com.example.demo.question.QuestionRepository;
import com.example.demo.question.QuestionService;


@SpringBootTest
public class Test2 {
	 @Autowired
	    private QuestionRepository questionRepository;

	 @Autowired
	    private AnswerRepository answerRepository;
	 
	 @Autowired
	 	private QuestionService questionService;
	 
	    @Test
	    void testJpa() {
	    	for (int i=1; i <= 200; i++) {
	    		String subject = String.format("테스트 데이터입니다.[%03d]", i);
	    		String content ="내용 무";
	    		this.questionService.create(subject, content, null);
	    	}
	    }
}

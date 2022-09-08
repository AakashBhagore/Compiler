package com.codecompiler.entity;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TestCases {

	@Id 
	@GeneratedValue
	private int testId;

	private  String input;
	
	private String output;
	
	private String score;
	
	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name="question_id")
	@JsonIgnore
	private Question question;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public TestCases(int testId, String input, String output, String score, Question question) {
		super();
		this.testId = testId;
		this.input = input;
		this.output = output;
		this.score = score;
		this.question = question;
	}
	public TestCases() {
	}
	@Override
	public String toString() {
		return "TestCases [testId=" + testId + ", input=" + input + ", output=" + output + ", Score=" + score
				+ ", question=" + question + "]";
	}	
}

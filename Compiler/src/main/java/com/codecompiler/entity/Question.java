package com.codecompiler.entity;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Question {

	@Id
	@GeneratedValue
	private int questionId;
	
	private String question;
	
	private String constraints;
	
	private String input;
	
	private String output;
	
	@OneToMany(mappedBy = "question",fetch =FetchType.EAGER ,cascade = CascadeType.ALL)
	private List<TestCases> testCases;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="contest_id")
	@JsonIgnore
	private Contest contest;

	
	
	public Question(String question, String constraints, String input, String output, Contest contest) {
		this.question = question;
		this.constraints = constraints;
		this.input = input;
		this.output = output;
		this.contest = contest;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
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

	public List<TestCases> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCases> testCases) {
		this.testCases = testCases;
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", question=" + question + ", constraints=" + constraints
				+ ", input=" + input + ", output=" + output + ", testCases=" + testCases + ", contest=" + contest + "]";
	}

	public Question() {
	}
	
}

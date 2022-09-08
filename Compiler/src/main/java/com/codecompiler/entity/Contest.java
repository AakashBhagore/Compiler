package com.codecompiler.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Contest {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String contestId;
	private String contestName;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	@OneToMany(mappedBy="contest",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Question> questions;

	
	public Contest(String contestName, LocalDateTime startTime, LocalDateTime endTime,List<Question> questions) {
		this.contestName = contestName;
		this.startTime   = startTime;
		this.endTime     = endTime;
		this.questions = questions;
	}

	

	public String getContestId() {
		return contestId;
	}



	public void setContestId(String contestId) {
		this.contestId = contestId;
	}



	public String getContestName() {
		return contestName;
	}



	public void setContestName(String contestName) {
		this.contestName = contestName;
	}



	public LocalDateTime getStartTime() {
		return startTime;
	}



	public void setStartTime(LocalDateTime startTime2) {
		this.startTime = startTime2;
	}



	public LocalDateTime getEndTime() {
		return endTime;
	}



	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}



	public List<Question> getQuestions() {
		return questions;
	}



	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "Contest [contestId=" + contestId + ", contestName=" + contestName + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", questions=" + questions + "]";
	}

	public Contest() {
	}
	
}

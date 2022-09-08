package com.codecompiler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codecompiler.entity.Contest;

public interface ContestRepository extends JpaRepository<Contest, String>{

	public Contest findByContestId(String contestId);

}

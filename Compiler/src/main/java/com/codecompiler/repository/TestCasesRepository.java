package com.codecompiler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codecompiler.entity.Question;
import com.codecompiler.entity.TestCases;

@Repository
public interface TestCasesRepository extends JpaRepository<TestCases, Integer>{

	public List<TestCases> findByQuestion(Question question);

//	public List<TestCases> findByQuestionId(int questionId);

}

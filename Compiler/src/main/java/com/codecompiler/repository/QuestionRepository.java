package com.codecompiler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codecompiler.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer>{

	public Question findByQuestionId(int questionId);

}

package com.codecompiler.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.codecompiler.common.property.CommonProperty;
import com.codecompiler.entity.Contest;
import com.codecompiler.entity.Question;
import com.codecompiler.entity.Response;
import com.codecompiler.entity.TestCases;
import com.codecompiler.service.CommonService;

@Controller
public class JavaController {


	@Autowired private CommonService commonService;

//	@PostMapping("/add-questions")
//	public ResponseEntity<Question> addQuestion() {
//		Question question = new Question();
//		question.setQuestionId(1);
//		question.setQuestion("write a program to perform ascending order on array element.");
//		List<TestCases> test = Arrays.asList(new TestCases(question,"1,5,2,3,7,4","1,2,3,4,5,7"),new TestCases(question,"1,5,0,4,8,3","0,1,3,4,5,8"), new TestCases(question,"7,5,-1,8,0,9","-1,0,5,7,8,9"));
//		question.setTestCases(test);
//		Question q = commonService.addQuestion(question);
//		return ResponseEntity.ok(q);
//	}
	
	@GetMapping("/contest") 
	private String addContest() {
		return "contest";
	}
	
	@PostMapping("/add-contest-api") 
	private String addContest(@RequestParam("contestName") String contestName,
			                  @RequestParam("startTime")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			                  @RequestParam("endTime")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endTime,Model model) {
		Contest contest = new Contest();
		contest.setContestName(contestName);
		contest.setStartTime(startTime);
		contest.setEndTime(endTime);
		
		Contest con = commonService.saveContest(contest);
		model.addAttribute("contestId", con.getContestId());
		return "question";
	}

	@GetMapping("/questions") 
	public ResponseEntity<Question> getQuestion(@RequestBody Question question){
		int qId = question.getQuestionId();
		Question q = commonService.getQuestion(qId);
		return ResponseEntity.ok(q);
	}
	
	@PostMapping("/add-question")
	public String addQuestion(@RequestParam("question")     String question,
			                  @RequestParam("constraints")  String constraints,
			                  @RequestParam("output")       String output,
			                  @RequestParam("input")        String input,
			                  @RequestParam("contestId")    String contestId,Model model) {	
		Contest contest = commonService.getContest(contestId);
		Question questions = new Question();
		questions.setConstraints(constraints);
		questions.setInput(input);
		questions.setOutput(output);
		questions.setQuestion(question);
		questions.setContest(contest);
		Question questionRessult = commonService.addQuestion(questions);
		model.addAttribute("question", questionRessult);
		return "questions";
	}
	
	@PostMapping("/add-test-cases-api") 
	public ResponseEntity<TestCases>saveTesatCases(@RequestBody Map<String, Object> testCases) {
		TestCases test = new TestCases();
		int questionId = (int)testCases.get("questionId");
		String input = (String)testCases.get("input");
		String output = (String)testCases.get("output");
		
		Question question = commonService.getQuestion(questionId); 
		test.setInput(input);
		test.setOutput(output);
		test.setQuestion(question);
		TestCases testCase = commonService.saveTestCases(test);
		return ResponseEntity.ok(testCase);
	}
	
	@PostMapping("/solve-challenge")
	public String solveEditor(@RequestParam("questionId") String questionId,Model model) {
		int id = Integer.parseInt(questionId);
		Question question = commonService.getQuestion(id);
		List<TestCases> test = commonService.getTestCase(question.getQuestionId());
		model.addAttribute("question", question);
		model.addAttribute("test", test);
		return "challenge";
	}
	
	
	@PostMapping("/java-compiler-api") 
	public ResponseEntity<Response> javaCompiler(@RequestBody Map<String, Object> data){
		Response response = new Response();
		String output = "", line = "", input="", testCase = "", languageExtension = "java", programOuput = "";
		String code = (String) data.get("code");
		int questionId = (int)data.get("questionId");
		Process pro = null;
		BufferedReader in = null;
		
		//generate code file
		commonService.getCodeFile(code,languageExtension);
		try
		  {
			programOuput = commonService.compile("javac.exe Practise.java");
			if(programOuput !=null && !programOuput.isEmpty()) {	
				response.setStatusMessage("Error: " + programOuput);
				response.setOutput(programOuput);
				return ResponseEntity.ok(response);
			}   

			File inputFile = new File(CommonProperty.programFileDestination+"input.txt");	
			BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
			
			
			List<Callable<String>> taskList = new ArrayList<Callable<String>>();
			List<Future<String>> futureList = new ArrayList<Future<String>>(); 
			while((input = inputReader.readLine()) != null){
				final String userInput = input;
				taskList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return commonService.run("java.exe Practise "+userInput);
					}
				});
//				programOuput += commonService.run("java.exe Practise "+input,testCase);
			}
			
			ExecutorService executor = Executors.newFixedThreadPool(taskList.size());
			futureList = executor.invokeAll(taskList);
			executor.shutdown();
			
			for(int i=0; i<futureList.size(); i++) {
				Future<String> result = futureList.get(i);
				programOuput += (String)result.get();
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		response.setOutput(programOuput);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/java-compiler-db-api")
	public ResponseEntity<Response> javaCompilerWithdb(@RequestBody Map<String, Object> data){
		Response response = new Response();

		String languageExtension = "java", programOuput = "";
		String code    = (String) data.get("code");
		String submit  = (String) data.get("submit");
		int questionId = (int)data.get("questionId");
		
		//generate code file
		commonService.getCodeFile(code,languageExtension);
		try
		  {
			programOuput = commonService.compile("javac.exe Practise.java");
			if(programOuput !=null && !programOuput.isEmpty()) {	
				response.setStatusMessage("Error: " + programOuput);
				response.setOutput(programOuput);
				return ResponseEntity.ok(response);
			}   
			
			if(submit!=null && !submit.isEmpty()) {
				programOuput = commonService.runCode(questionId,submit);
			} else {
				programOuput = commonService.execute(questionId,submit);
		    }
		 } catch (Exception e) {
			e.printStackTrace();
		}
		response.setOutput(programOuput);
		return ResponseEntity.ok(response);
	}
}

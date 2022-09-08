package com.codecompiler.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codecompiler.common.property.CommonProperty;
import com.codecompiler.entity.Response;
import com.codecompiler.entity.TestCases;
import com.codecompiler.service.CommonService;

@Controller
public class PythonController {

	@Autowired private CommonService commonService;


	@SuppressWarnings("resource")
	@PostMapping(value = "/python-compiler-api")
	@ResponseBody
	public ResponseEntity<Response> getCompiler(@RequestBody Map<String, Object> data, Model model)
			throws IOException {
		Response response = new Response();
		String input = "", languageExtension = "py", programOuput = "";
		String code = (String) data.get("code");
		System.out.println(code);
//		int questionId = (int)data.get("questionId");
	
		//generate code file
		commonService.getCodeFile(code,languageExtension);
		try
		  {
			programOuput = commonService.compile("py Practise.py");
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
						return commonService.run("py Practise.py "+userInput);
					}
				});
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
	
	
	@PostMapping("/python-compiler-db-api")
	public ResponseEntity<Response> javaCompilerWithdb(@RequestBody Map<String, Object> data){
		Response response = new Response();

		String languageExtension = "py", programOuput = "";
		String submit = (String) data.get("submit");
		String code = (String) data.get("code");
		int questionId = (int)data.get("questionId");
		
		//generate code file
		commonService.getCodeFile(code,languageExtension);
		try
		  {
			programOuput = commonService.compile("py Practise.py");
			if(programOuput !=null &&  !programOuput.isEmpty()) {	
				response.setStatusMessage("Error: " + programOuput);
				response.setOutput(programOuput);
				return ResponseEntity.ok(response);
			}   
			
			//fetching testCases from db for this question(QuestionId)
			List<TestCases> testCases = commonService.getTestCase(questionId);
			
			List<Callable<String>> taskList = new ArrayList<Callable<String>>();
			List<Future<String>> futureList = new ArrayList<Future<String>>(); 
			
			for(TestCases test : testCases) {
				final String sampleInput = test.getInput();
				final String sampleOutput = test.getOutput();
				taskList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return commonService.runDbTestCases("py Practise.py "+sampleInput,sampleOutput,submit);
					}
				});
			}
						
			ExecutorService executor = Executors.newFixedThreadPool(taskList.size());
			futureList = executor.invokeAll(taskList);
			executor.shutdown();
			
			for(int i=0; i<futureList.size(); i++) {
				Future<String> result = futureList.get(i);
				programOuput += (String)result.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		response.setOutput(programOuput);
		return ResponseEntity.ok(response);
	}
}

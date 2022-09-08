package com.codecompiler.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codecompiler.common.property.CommonProperty;
import com.codecompiler.entity.Contest;
import com.codecompiler.entity.Question;
import com.codecompiler.entity.TestCases;
import com.codecompiler.repository.ContestRepository;
import com.codecompiler.repository.QuestionRepository;
import com.codecompiler.repository.TestCasesRepository;

@Service
public class CommonService {

	@Autowired private QuestionRepository questionRepository;
	@Autowired private ContestRepository contestRepository;
    @Autowired private TestCasesRepository testCasesRepository;
    
    /**
	 * @author Aakash
	 * @param questionId
	 * @implNote saving question object 
	 */
	public Question addQuestion(Question question) {
		return questionRepository.save(question);
	}
	
   /**
	 * @author Aakash
	 * @param questionId
	 * @implNote getting question object using questionId
	 */
	public Question getQuestion(int questionId) {
		Question question = questionRepository.findByQuestionId(questionId);
		return question;
	}

	/**
	 * @author Aakash
	 * @param TestCases Object
	 * @implNote saving TestCases object
	 */
	public TestCases saveTestCases(TestCases test) {
		return testCasesRepository.save(test);
	}
	
	/**
	 * @author Aakash
	 * @param questionId
	 * @implNote getting test cases using questionId bzc of parent child relationship
	 */
	public List<TestCases> getTestCase(int questionId){
		Question question = questionRepository.findByQuestionId(questionId);
		List<TestCases> testCase = testCasesRepository.findByQuestion(question);
		return testCase;
	}
	
	/**
	 * @author Aakash
	 * @param contest Object
	 * @implNote saving contest
	 */
	public Contest saveContest(Contest contest) {
		Contest con = contestRepository.save(contest);
		return con;
	}

	/**
	 * @author Aakash
	 * @param Contest
	 * @param contestId
	 * @implNote getting data of contest using contestId
	 */
	public Contest getContest(String contestId) {
		System.out.println("ContestId:- "+contestId);
		Contest con = contestRepository.findByContestId(contestId);
		return con;
	}
	
	/**
	 * @author Aakash
	 * @param code
	 * @param languageExtension
	 * @implNote Generate code file
	 */
	public void getCodeFile(String code, String languageExtension) { 
		FileWriter fl;
		try {
			fl = new FileWriter(CommonProperty.programFileDestination +""+ CommonProperty.programName + "." +languageExtension);
			PrintWriter pr = new PrintWriter(fl);
			pr.write(code);
			pr.flush();
			pr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Aakash
	 * @param  progName
	 * @return Process 
	 * @throws IOException
	 * @implNode Compile and run code , progName is command which we're executing to execute a program
	 */
	public String run(String progName) throws IOException {
		Process pro;
		BufferedReader in;
		String output = "";
		String line = "";
		String testOutput="";
		List<String> testCases = new ArrayList<String>();
		List<String> userOutput = new ArrayList<String>();
		
		File outputFile= new File(CommonProperty.programFileDestination+"output.txt");
		BufferedReader outputReader= new BufferedReader(new FileReader(outputFile));
		
		while((testOutput = outputReader.readLine()) !=null) {
			testCases.add(testOutput);
		}
		pro = Runtime.getRuntime().exec(progName, null,new File(CommonProperty.programFileDestination));
		in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		while ((line = in.readLine()) != null) {
			userOutput.add(line);
			output += line + "\n";
		}
		if(testCases.containsAll(userOutput)) {
			System.out.println("Success");
		} else {
			System.out.println("Failure");
		}
		outputReader.close();
		return output;
	}

	/**
	 * @author Aakash
	 * @param  progName
	 * @return String output 
	 * @throws IOException
	 * @implNode Compile  the code, progName is command which we're executing to execute a program
	 */
	public String compile(String progName) {
	    Process pro = null;
		BufferedReader in = null;
		String output = "";
		String line = "";

		try {
			pro = Runtime.getRuntime().exec(progName, null,new File(CommonProperty.programFileDestination));
			in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
			line = in.readLine();
			if(line != null) {
				output+=line+"\n";
				while ((line = in.readLine()) != null) {
					output+=line + "\n";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}   
		return output;
	}
	
	/**
	 * @author aakash 
	 * @param progName,sampleOutput,submit
	 * @return String output 
	 * @implNode Testing with testcases, run user program against multiple testCase
	 */
	public String runDbTestCases(String progName,String sampleOutput,String submit) throws IOException {
		Process pro;
		BufferedReader in;
		String output = "";
		String line = "";
		List<String> testCases = new ArrayList<String>();
		List<String> codeOutput = new ArrayList<String>();
		List<String> userOutput = new ArrayList<String>();
		
		pro = Runtime.getRuntime().exec(progName, null,new File(CommonProperty.programFileDestination));
		in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		
		while ((line = in.readLine()) != null) {
			codeOutput.add(line);
		}
		
		if(submit!=null && !submit.isEmpty()) {
			output  = String.join("\n", codeOutput);
			userOutput.add(output);
			testCases.add(sampleOutput);
			
			if(testCases.containsAll(userOutput)) {
				output = "success" + "\n";
			} else {
				output = "failure" + "\n";
			}
		} else {
			output  = String.join("\r\n", codeOutput);
			userOutput.add(output);
			testCases.add(sampleOutput);
			
			if(testCases.containsAll(userOutput)) {
				output = "success" + "\n";
			} else {
				output = "failure" + "\n";
			}
		}
		
		return output;
	}

	
	
	/**
	 * @author aakash 
	 * @param questionId , submit
	 * @return String programOuput 
	 * @implNode Testing with testcases
	 */
	public String runCode(int questionId,String submit) {
		String programOuput = "";
		try {
			List<TestCases> testCases = getTestCase(questionId);
			List<Callable<String>> taskList = new ArrayList<Callable<String>>();
			List<Future<String>> futureList = new ArrayList<Future<String>>(); 
			
			for(TestCases test : testCases) {
				final String sampleInput = test.getInput();
				final String sampleOutput = test.getOutput();
				taskList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return runDbTestCases("java.exe Practise "+sampleInput,sampleOutput,submit);
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	   System.out.println(programOuput);
	  return programOuput;
	}

	/**
	 * @author aakash 
	 * @param sampleOutput,submit
	 * @return String programOuput 
	 * @implNode Execute the code by calling runDbTestCases
	 */
	public String execute(int questionId,String submit) {
		String programOuput = "";
		try {
			//fetching testCases from db for this question(QuestionId)
			  Question question   =  getQuestion(questionId);
			  programOuput        =  runDbTestCases("java.exe Practise "+question.getInput(),question.getOutput(),submit);
				
		} catch(Exception e) {
			e.printStackTrace();
		}
	    System.out.println(programOuput);
	  return programOuput;
	}
}

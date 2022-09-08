package com.codecompiler.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codecompiler.entity.Response;

@Controller
public class CommonController {

	@GetMapping("/home")
	public String home() {
		return "editor";
	}

	@PostMapping(value = "/compiler")
	@ResponseBody
	public ResponseEntity<Response> getCompiler(@RequestBody Map<String, Object> data, Model model)
			throws IOException {
		Response response = new Response();
		String str = "";
		String language = (String) data.get("language");

		if (language.equals("python")) {
			String uid = "HelloPython";
			// File fl = new File(uid+"."+language);
			FileWriter fl = new FileWriter(
					"C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\" + uid + "." + "py");
			PrintWriter pr = new PrintWriter(fl);
			pr.write((String) data.get("code"));
			pr.flush();
			pr.close();
			Process pro = null;
			BufferedReader in = null;
			String line = null;
			try {
				//pro = Runtime.getRuntime().exec("py HelloPython.py", null,new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp"));
				pro = Runtime.getRuntime().exec("py HelloPython.py", null,new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));
				in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				line = in.readLine();
				if(line != null) {
					str+=line+"\n";
					while ((line = in.readLine()) != null) {
						str+=line + "\n";
					}	
					System.out.println("PythonCode Error Message:- "+str);
					response.setStatusMessage(str);
					response.setOutput(str);
					return ResponseEntity.ok(response);
				}

				pro = Runtime.getRuntime().exec("py HelloPython.py", null,new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = in.readLine()) != null) {
					System.out.println("PythonCode line - " + line);
					str += line + "\n";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (language.equals("java")) {
			String uid = "Main";
			//FileWriter fl = new FileWriter("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\" + uid + "." + "java");
			FileWriter fl = new FileWriter("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\" + uid + "." + "java");
			PrintWriter pr = new PrintWriter(fl);
			pr.write((String) data.get("code"));
			pr.flush();
			pr.close();
			Process pro = null;
			BufferedReader in = null;
			String line = null;
			try {
				//pro = Runtime.getRuntime().exec("javac.exe Main.java", null,new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp"));
				pro = Runtime.getRuntime().exec("javac.exe Main.java", null,new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));
				in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				line = in.readLine();
				if(line != null) {
					str+=line+"\n";
					while ((line = in.readLine()) != null) {
						str+=line + "\n";
					}	
					System.out.println("JavaCode Error Message:- "+str);
					response.setStatusMessage(str);
					response.setOutput(str);
					return ResponseEntity.ok(response);
				}   
				//pro = Runtime.getRuntime().exec("java.exe Main", null,new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp"));
				pro = Runtime.getRuntime().exec("java.exe Main 125", null,new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = in.readLine()) != null) {
					System.out.println("JavaCode line - " + line);
					str += line + "\n";
				}
				System.out.println(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(language.equals("c")) {
			String uid = "HelloC";
			//FileWriter fl = new FileWriter("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\" + uid + "." + "c");
			FileWriter fl = new FileWriter("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\" + uid + "." + "c");
			PrintWriter pr = new PrintWriter(fl);
			pr.write((String) data.get("code"));
			pr.flush();
			pr.close();
			Process pro = null;
			BufferedReader in = null;
			String line = null;
			try {

				//pro = Runtime.getRuntime().exec("C:\\TDM-GCC-64\\bin\\gcc.exe HelloC.c", null, new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp"));
				pro = Runtime.getRuntime().exec("C:\\TDM-GCC-64\\bin\\gcc.exe HelloC.c", null, new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp"));			
				in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));				
				line = in.readLine();
				if(line !=null) {
					str+=line+"\n";
					while ((line = in.readLine()) != null) {
						str+=line + "\n";
					}	
					System.out.println("CCode Error Message:- "+str);
					response.setStatusMessage(str);
					response.setOutput(str);
					return ResponseEntity.ok(response);
				}
				//pro = Runtime.getRuntime().exec("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\a.exe", null,new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\"));
				pro = Runtime.getRuntime().exec("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\a.exe", null,new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));
//				pro = Runtime.getRuntime().exec(CommonProperty.programFileDestination+"a.exe "+input, null,new File(CommonProperty.programFileDestination));
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = in.readLine()) != null) {
					System.out.println("CCode line - " + line);
					str += line + "\n";
				}
				System.out.println(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(language.equals("cpp")) {
			String uid = "HelloCPP";
			//FileWriter fl = new FileWriter("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\" + uid + "." + "cpp");

			FileWriter fl = new FileWriter("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\" + uid + "." + "cpp");
			PrintWriter pr = new PrintWriter(fl);
			pr.write((String) data.get("code"));
			pr.flush();
			pr.close();
			Process pro = null;
			BufferedReader in = null;
			String line = null;
			try {
				//pro = Runtime.getRuntime().exec("C:\\TDM-GCC-64\\bin\\g++.exe HelloCPP.cpp", null, new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\"));
				pro = Runtime.getRuntime().exec("C:\\TDM-GCC-64\\bin\\g++.exe HelloCPP.cpp", null, new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));	
//				pro = Runtime.getRuntime().exec(CommonProperty.compilerDestionation+"g++.exe Practise.cpp", null, new File(CommonProperty.programFileDestination));	
				in = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
				line = in.readLine();
				if(line !=null) {
					str+=line+"\n";
					while ((line = in.readLine()) != null) {
						str+=line + "\n";
					}	
					System.out.println("C++Code Error Message:- "+str);
					response.setStatusMessage(str);
					response.setOutput(str);
					return ResponseEntity.ok(response);
				}
				//pro = Runtime.getRuntime().exec("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\a.exe", null,new File("C:\\Users\\Public\\Montrix\\CodeCompiler\\src\\main\\resources\\temp\\"));
				pro = Runtime.getRuntime().exec("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\a.exe", null,new File("C:\\AMUL\\Compiler\\Springboot-online-compiler\\Online-Editor\\src\\main\\resources\\temp\\"));
				in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = in.readLine()) != null) {
					System.out.println("C++Code line - " + line);
					str += line + "\n";
				}
				System.out.println(str);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		response.setOutput(str);
		return ResponseEntity.ok(response);
	}

}

package com.javawebtutor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.javawebtutor.model.exam.Answer;
import com.javawebtutor.model.exam.Question;
import com.javawebtutor.service.ExamService;

public class ExamServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		ExamService examService=new ExamService();
		
		String process = request.getParameter("process");
		if(process.equals("NQUESTION")){
			Integer exam = Integer.valueOf(request.getParameter("exam"));
			Integer multiple = Integer.valueOf(request.getParameter("multiple"));
			String tquestion=new String(request.getParameter("tquestion").getBytes("ISO-8859-1"),"UTF-8");
			tquestion=tquestion.replaceAll("\n", "<br/>");
			Question question=examService.saveQuestion(tquestion, exam,multiple);
			String json = new Gson().toJson(question);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
		}else if(process.equals("NASNWER")){
			Integer question = Integer.valueOf(request.getParameter("question"));
			String tanswer=new String(request.getParameter("tanswer").getBytes("ISO-8859-1"),"UTF-8");
			tanswer=tanswer.replaceAll("\n", "<br/>");
			Integer correct = Integer.valueOf(request.getParameter("correct"));
			Answer answer=examService.saveAnswer(question, correct, tanswer);
			String json = new Gson().toJson(answer);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			
		}else if(process.equals("DELANS")){
			Integer id = Integer.valueOf(request.getParameter("id"));
			examService.delAnswer(id);
		}else if(process.equals("STUDY")){
			Integer exam = Integer.valueOf(request.getParameter("exam"));
			List<Question> questions=examService.getQuestions(exam);
			
			String json = new Gson().toJson(questions);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
		}
	}
}

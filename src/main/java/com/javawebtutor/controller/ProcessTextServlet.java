package com.javawebtutor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.javawebtutor.model.Text;
import com.javawebtutor.model.Word;
import com.javawebtutor.service.WordsService;

public class ProcessTextServlet extends HttpServlet {
	private WordsService wordsService;

	public ProcessTextServlet() {
		super();
		wordsService = new WordsService();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String process = request.getParameter("process");

		if (process.equals("LIST")) {

			Integer idText = Integer.valueOf(request.getParameter("idText"));
			List<Word> words = wordsService.getWordOfText(idText);
			String json = new Gson().toJson(words);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
		} else if (process.equals("DELWORD")) {
			Integer id = Integer.valueOf(request.getParameter("id"));
			wordsService.delWord(id);
		} else if (process.equals("UPD")) {
			Integer id = Integer.valueOf(request.getParameter("idword"));
			String spanish = new String(request.getParameter("spanish").getBytes("ISO-8859-1"), "UTF-8");
			
			wordsService.registerWord(id, spanish);
		} else if (process.equals("LEARNED")) {
			Integer id = Integer.valueOf(request.getParameter("idword"));		
			
			wordsService.learned(id);
		}else if (process.equals("TEXTS")) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				Date date = format.parse(request.getParameter("date"));
				List<Text> texts = wordsService.loadText(date);
				String json = new Gson().toJson(texts);

				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print(json);
				out.flush();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (process.equals("DELTEXT")) {
			Integer id = Integer.valueOf(request.getParameter("id"));
			wordsService.delText(id);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = "";

		String text = "";
		if (request.getParameter("text") != null) {
			text = new String(request.getParameter("text").getBytes("ISO-8859-1"), "UTF-8");

			String process = request.getParameter("process");

			if (process.equals("TEXT")) {
				Integer idText = wordsService.getIdText(text);
				json = new Gson().toJson(idText);

			}
		}

		out.print(json);
		out.flush();
	}

}

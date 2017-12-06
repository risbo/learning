package com.javawebtutor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gtranslate.Language;
import com.gtranslate.Translator;

public class Util {
	public List<String> loadWords(String value) {
		value = value.toLowerCase().replaceAll(Pattern.quote("|"), "").replaceAll(Pattern.quote("�"), "")
				.replaceAll(Pattern.quote("\""), "").replaceAll(Pattern.quote("#"), "")
				.replaceAll(Pattern.quote("$"), "").replaceAll(Pattern.quote("%"), "")
				.replaceAll(Pattern.quote("&"), "").replaceAll(Pattern.quote("/"), "")
				.replaceAll(Pattern.quote("("), "").replaceAll(Pattern.quote(")"), "")
				.replaceAll(Pattern.quote("="), "").replaceAll(Pattern.quote("?"), "")
				.replaceAll(Pattern.quote("�"), "").replaceAll(Pattern.quote("�"), "")
				.replaceAll(Pattern.quote(","), "").replaceAll(Pattern.quote("."), "")
				.replaceAll(Pattern.quote(";"), "").replaceAll(Pattern.quote(":"), "")
				.replaceAll(Pattern.quote("<"), "").replaceAll(Pattern.quote(">"), "")
				.replaceAll(Pattern.quote("*"), "").replaceAll(Pattern.quote("{"), "")
				.replaceAll(Pattern.quote("}"), "").replaceAll(Pattern.quote("�"), "")
				.replaceAll(Pattern.quote("�"), "").replaceAll(Pattern.quote("�"), "")
				.replaceAll(Pattern.quote("-"), " ").replaceAll(Pattern.quote("�"), "")
				.replaceAll(Pattern.quote("\n"), " ").replaceAll(Pattern.quote("\t"), " ")
				.replaceAll(Pattern.quote("\r"), " ").replaceAll(Pattern.quote("'s"), " ")
				.replaceAll(Pattern.quote("!"), " ").replaceAll(Pattern.quote("�"), "")
				.replaceAll(Pattern.quote("'"), "")
				.replaceAll("\n", "<br/>")
				.replaceAll(Pattern.quote("�"), "");

		String[] listTemp = value.split(" ");
		List<String> list = new ArrayList<String>();
		for (String word : listTemp) {
			if (word.length() > 3 && !isNumeric(word) && !word.isEmpty() && !list.contains(word))
				list.add(word);
		}
		Collections.sort(list, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		});
		
		return list;
	}

	public boolean isNumeric(String str) {
		return str.matches("-?\\d+(.\\d+)?");
	}
	
	
	
}

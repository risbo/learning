package com.javawebtutor.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the words database table.
 * 
 */
@Entity
@Table(name="words")
@NamedQueries({
@NamedQuery(name="Word.findAll", query="SELECT m FROM Word m order by m.word"),
@NamedQuery(name="Word.findAllWord", query="SELECT m FROM Word m where m.word= :word  order by m.word asc")
})
public class Word implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Integer id;

	private String spanish;

	private String word;
	
	private String example;

	public Word() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpanish() {
		return this.spanish;
	}

	public void setSpanish(String spanish) {
		this.spanish = spanish;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

}
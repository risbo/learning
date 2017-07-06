package com.javawebtutor.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mywords database table.
 * 
 */
@Entity
@Table(name="mywords")
@NamedQueries({
@NamedQuery(name="Myword.findAll", query="SELECT m FROM Myword m"),
@NamedQuery(name="Myword.findAllWord", query="SELECT m FROM Myword m where m.word= :word order by m.word asc")
})
public class Myword implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Integer id;

	private String spanish;

	private String word;
	
	private String example;

	public Myword() {
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
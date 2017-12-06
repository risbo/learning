package com.javawebtutor.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "words_text")
public class WordsText implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name="id_word")
	private Integer idWord;
	
	@Column(name="id_text")
	private Integer idText;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_word",insertable=false,updatable=false)
	private Word word;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdWord() {
		return idWord;
	}

	public void setIdWord(Integer idWord) {
		this.idWord = idWord;
	}

	public Integer getIdText() {
		return idText;
	}

	public void setIdText(Integer idText) {
		this.idText = idText;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}
	
	

}

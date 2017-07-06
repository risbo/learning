package com.javawebtutor.model.exam;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Answer.findAll", query="SELECT a FROM Answer a"),
@NamedQuery(name="Answer.findAllByIdquestion", query="SELECT a FROM Answer a where a.idquestion=:idquestion")
})
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
	private Integer id;

	private String answer;

	private int correct;

	@Temporal(TemporalType.DATE)
	private Date fcreate;

	private int idquestion;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idquestion", nullable = false, insertable=false, updatable=false)	
	private Question question;

	public Answer() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getCorrect() {
		return this.correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public Date getFcreate() {
		return this.fcreate;
	}

	public void setFcreate(Date fcreate) {
		this.fcreate = fcreate;
	}

	public int getIdquestion() {
		return this.idquestion;
	}

	public void setIdquestion(int idquestion) {
		this.idquestion = idquestion;
	}
	

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	

}
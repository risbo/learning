package com.javawebtutor.model.exam;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the question database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
@NamedQuery(name = "Question.findAllByIdExam", query = "SELECT q FROM Question q where q.idexam =:idexam ")
})
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date fcreate;

	private int idexam;

	private String question;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
	private List<Answer> answers;
	
	private int multiple;
	

	public Question() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFcreate() {
		return this.fcreate;
	}

	public void setFcreate(Date fcreate) {
		this.fcreate = fcreate;
	}

	public int getIdexam() {
		return this.idexam;
	}

	public void setIdexam(int idexam) {
		this.idexam = idexam;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

}
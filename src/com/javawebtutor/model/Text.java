package com.javawebtutor.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the texts database table.
 * 
 */
@Entity
@Table(name="texts")
@NamedQueries({
	@NamedQuery(name="Text.findAll", query="SELECT t FROM Text t"),
	@NamedQuery(name="Text.findAllByFcreate", query="SELECT m FROM Text m where m.fcreate >= :pfcreate order by m.fcreate asc")
})
public class Text implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date fcreate;

	private String phrases;

	public Text() {
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

	public String getPhrases() {
		return this.phrases;
	}

	public void setPhrases(String phrases) {
		this.phrases = phrases;
	}

}
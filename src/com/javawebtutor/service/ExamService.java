package com.javawebtutor.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.javawebtutor.hibernate.util.HibernateUtil;
import com.javawebtutor.model.Word;
import com.javawebtutor.model.exam.Answer;
import com.javawebtutor.model.exam.Question;

public class ExamService {

	public Question saveQuestion(String question,int exam,int multiple){
		Session session = HibernateUtil.openSession();		
		Transaction tx = null;
		Question q=new Question();
		try {
			tx = session.getTransaction();
			tx.begin();
			
			q.setMultiple(multiple);
			q.setFcreate(new Date());
			q.setIdexam(exam);
			q.setQuestion(question);
			session.saveOrUpdate(q);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return q;
	}
	
	public Answer saveAnswer(int question,  int correct,String answer){
		Session session = HibernateUtil.openSession();		
		Transaction tx = null;
		Answer a=new Answer();
		try {
			tx = session.getTransaction();
			tx.begin();
			
			a.setFcreate(new Date());
			a.setIdquestion(question);			
			a.setCorrect(correct);
			a.setAnswer(answer);
			session.saveOrUpdate(a);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return a;
	}
	
	public List<Question> getQuestions(int idexam) {
		Session session = HibernateUtil.openSession();
		Transaction tx = null;
		List<Question> list = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Query query = session.getNamedQuery("Question.findAllByIdExam");
			query.setParameter("idexam", idexam);
			list = query.list();

			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		for (Question question : list) {
			question.setAnswers(getAnswers(question.getId()));
		}
		return list;
	}
	
	 
	public List<Answer> getAnswers(int idquestion) {
		Session session = HibernateUtil.openSession();
		Transaction tx = null;
		List<Answer> list = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Query query = session.getNamedQuery("Answer.findAllByIdquestion");
			query.setParameter("idquestion", idquestion);
			list = query.list();

			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return list;
	}
	
	public void delAnswer(int id) {
		Session session = HibernateUtil.openSession();
		
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Answer a=(Answer)session.get(Answer.class, id);
			session.delete(a);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void delQuestion(int id) {
		Session session = HibernateUtil.openSession();
		
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Question q=(Question)session.get(Question.class, id);
			session.delete(q);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}

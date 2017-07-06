package com.javawebtutor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.javawebtutor.controller.Util;
import com.javawebtutor.hibernate.util.HibernateUtil;
import com.javawebtutor.model.Myword;
import com.javawebtutor.model.Text;
import com.javawebtutor.model.Word;
import com.javawebtutor.model.WordsText;

public class WordsService {

	public List<Word> loadWords(String text, Integer idText) {
		Session session = HibernateUtil.openSession();
		Transaction tx = null;
		Util util = new Util();
		List<Integer> idsWords=new ArrayList<Integer>();

		List<String> words = util.loadWords(text);
		try {
			tx = session.getTransaction();
			tx.begin();

			Text t = new Text();
			t.setFcreate(new Date());
			t.setPhrases(text);

			if (text != null && !text.isEmpty())
				registerText(t);

			for (String string : words) {
				Word w = new Word();
				w.setWord(string);
				Word word=registerWord(w);
				
				WordsText wordsText=new WordsText();
				wordsText.setIdText(t.getId());
				wordsText.setIdWord(word.getId());
				registerWordsText(wordsText);
				
				idsWords.add(word.getId());
			}
			
			if(idText!=null){
				idsWords=idsWord(idText);
			}
				
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return getWords(idsWords);
	}

	public String getImplementWord(String word) {
		if (word.substring(word.length() - 3, word.length()).contains("ing")) {
			word = word.substring(0, word.length() - 3);
		}
		if (word.substring(word.length() - 2, word.length()).contains("ed")) {
			word = word.substring(0, word.length() - 2);
		}
		if (word.substring(word.length() - 1, word.length()).contains("s")) {
			word = word.substring(0, word.length() - 1);
		}
		if (word.substring(word.length() - 2, word.length()).contains("ly")) {
			word = word.substring(0, word.length() - 2);
		}

		return word;
	}

	public List<Text> loadText(Date pfcreate) {
		Session session = HibernateUtil.openSession();
		Transaction tx = null;

		try {
			tx = session.getTransaction();
			tx.begin();

			Query q = session.getNamedQuery("Text.findAllByFcreate");
			if (pfcreate == null)
				pfcreate = new Date();
			q.setParameter("pfcreate", pfcreate);
			return q.list();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return new ArrayList<Text>();
	}

	public boolean isMyWordExists(String word) {
		Session session = HibernateUtil.openSession();
		boolean result = false;
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Query query = session.getNamedQuery("Myword.findAllWord");
			query.setParameter("word", word);
			List<Myword> mw = query.list();
			tx.commit();
			if (mw != null && !mw.isEmpty())
				result = true;
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}

	public Word isWordExists(String word) {
		Session session = HibernateUtil.openSession();		
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Query query = session.getNamedQuery("Word.findAllWord");
			query.setParameter("word", word);
			List<Word> mw = query.list();
			tx.commit();
			if (mw != null && !mw.isEmpty())
				return mw.get(0);
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return null;
	}

	public boolean addMyword(Integer id) {
		Session session = HibernateUtil.openSession();
		boolean result = false;
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();

			Word w = (Word) session.get(Word.class, id);
			Myword mw = new Myword();
			mw.setWord(w.getWord());
			mw.setSpanish(w.getSpanish());
			mw.setExample(w.getExample());
			registerMyword(mw);
			session.delete(w);
			
			
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}
	
	public List<WordsText> loadWordsText(Integer id) {
		Session session = HibernateUtil.openSession();
		List<WordsText> wordsTexts=null;
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();

			Query query=session.createQuery("select w from WordsText w where w.idText = :idText");
			query.setParameter("idText", id);
			wordsTexts=query.list();
			
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return wordsTexts;
	}
	
	private List<Integer> idsWord(Integer id){
		List<Integer> listIds=new ArrayList<Integer>();
		for (WordsText wordsText : loadWordsText(id)) {
			listIds.add(wordsText.getIdWord());
		}
		return listIds;
	}
	
	public boolean registerWordsText(WordsText wordsText) {
		Session session = HibernateUtil.openSession();		
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			session.saveOrUpdate(wordsText);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	public boolean registerMyword(Myword myWord) {
		Session session = HibernateUtil.openSession();
		if (isMyWordExists(myWord.getWord()))
			return false;

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			session.saveOrUpdate(myWord);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	public void registerText(Text t) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			session.saveOrUpdate(t);
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

	public void delText(Integer id) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Text t = (Text) session.get(Text.class, id);
			session.delete(t);
						
			for (WordsText wordsText : loadWordsText(id)) {
				session.delete(wordsText);
			}
			
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

	public void delWord(Integer id) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Word t = (Word) session.get(Word.class, id);
			session.delete(t);
			
			Query query=session.createQuery("select w from WordsText w where w.idWord = :idWord");
			query.setParameter("idWord", id);
			List<WordsText> wordsTexts=query.list();
			for (WordsText wordsText : loadWordsText(id)) {
				session.delete(wordsText);
			}
			
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

	public Word registerWord(Word word) {
		Session session = HibernateUtil.openSession();
		Word w=isWordExists(word.getWord());
		if (w!=null)
			return w;

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			session.saveOrUpdate(word);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return word;
	}

	public boolean registerWord(Integer id, String spanish, String example) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Word w = (Word) session.get(Word.class, id);
			w.setSpanish(spanish);
			w.setExample(example);
			session.saveOrUpdate(w);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	public List<Word> getWords(List<Integer> ids) {
		Session session = HibernateUtil.openSession();
		Transaction tx = null;
		List<Word> list = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			
			if(!ids.isEmpty()){
				Query query = session.createQuery("select w from Word w where w.id in(:ids)");
				query.setParameterList("ids", ids);
				list = query.list();
				
			}else{
				Query query = session.getNamedQuery("Word.findAll");
				list = query.list();
			}
			

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

}

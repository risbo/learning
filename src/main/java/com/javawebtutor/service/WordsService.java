package com.javawebtutor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.javawebtutor.controller.Util;
import com.javawebtutor.hibernate.util.HibernateUtil;
import com.javawebtutor.model.Text;
import com.javawebtutor.model.Word;
import com.javawebtutor.model.WordsText;

public class WordsService {

	public Integer getIdText(String text) {
		if (text != null && !text.isEmpty()) {

			Session session = HibernateUtil.openSession();
			Transaction tx = null;
			Util util = new Util();
			Text t = new Text();

			List<String> words = util.loadWords(text);
			try {
				tx = session.getTransaction();
				tx.begin();

				t.setFcreate(new Date());
				t.setPhrases(text);
				registerText(t);

				for (String string : words) {
					Word w = new Word();
					w.setWord(string);	
					w.setStatus("LEARN");
					Word word = registerWord(w);

					WordsText wordsText = new WordsText();
					wordsText.setIdText(t.getId());
					wordsText.setIdWord(word.getId());
					registerWordsText(wordsText);

				}

				return t.getId();
			} catch (Exception e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace();
			} finally {
				session.close();
			}

		} else {
			return null;
		}
		return null;

	}

	public List<Word> getWordOfText(Integer idText) {
		Session session = HibernateUtil.openSession();
		List<Word> wordsTexts=new ArrayList<Word>();
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();

			Query query = session.createQuery("select w.word from WordsText w "
					+ "where w.idText = :idText "
					+ "and w.word.status like 'LEARN'");
			query.setParameter("idText", idText);
			wordsTexts = query.list();
			

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return wordsTexts;
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

	public List<WordsText> loadWordsText(Integer id) {
		Session session = HibernateUtil.openSession();
		List<WordsText> wordsTexts = null;
		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();

			Query query = session.createQuery("select w from WordsText w "
					+ "inner join w.word wo "
					+ "where w.idText = :idText "
					+ "and wo.status like 'LEARN' ");
			
			
			
			query.setParameter("idText", id);
			wordsTexts = query.list();

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

	private List<Integer> idsWord(Integer id) {
		List<Integer> listIds = new ArrayList<Integer>();
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
	
	public void learned(Integer id) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Word t = (Word) session.get(Word.class, id);
			t.setStatus("LEARNED");
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

	public void delWord(Integer id) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Word t = (Word) session.get(Word.class, id);
			session.delete(t);

			Query query = session.createQuery("select w from WordsText w "
					+ "inner join w.word wo "
					+ "where w.idWord = :idWord "
					+ "and wo.status not like 'LEARNED'");
			query.setParameter("idWord", id);
			List<WordsText> wordsTexts = query.list();
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
		Word w = isWordExists(word.getWord());
		if (w != null) {
			return w;
		}

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
			System.out.println(word.getWord());
			e.printStackTrace();
		} finally {
			session.close();
		}
		return word;
	}

	public boolean registerWord(Integer id, String spanish) {
		Session session = HibernateUtil.openSession();

		Transaction tx = null;
		try {
			tx = session.getTransaction();
			tx.begin();
			Word w = (Word) session.get(Word.class, id);
			w.setSpanish(spanish);
//			w.setExample(example);
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

			if (!ids.isEmpty()) {
				Query query = session.createQuery("select w from Word w where w.id in(:ids) and w.status LIKE 'LEARN' order by w.word");
				query.setParameterList("ids", ids);
				list = query.list();

			} else {
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

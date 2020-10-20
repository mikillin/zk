package db.service;

import db.HibernateUtil;
import db.entity.AssessmentEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SurveyService {


    public List<AssessmentEntity> getSurveyBySearchWord(String searchWord) {

        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "select distinct AE from AssessmentEntity AE " +
                "where AE.date =  (select max(date) from AssessmentEntity where AE.surveyId= surveyId)";
        if (!StringUtils.isEmpty(searchWord))
            hql += "and (AE.date like '%" + searchWord + "%' " +
                    "or AE.surveyName like '%" + searchWord + "%' " +
                    "or AE.status like '%" + searchWord + "%')";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            results = query.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }
        return results;
    }

    public List<AssessmentEntity> getSurveyByDate(Date date) {

        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE WHERE AE.date = :date";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("date", date);
            results = query.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }
        return results;
    }

    public List<AssessmentEntity> getSurveyById(Integer id) {

        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE WHERE AE.surveyId = :id";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            results = query.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }
        return results;
    }
}

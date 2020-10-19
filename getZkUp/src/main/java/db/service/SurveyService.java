package db.service;

import db.HibernateUtil;
import db.entity.AssessmentEntity;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TemporalType;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SurveyService {


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
}

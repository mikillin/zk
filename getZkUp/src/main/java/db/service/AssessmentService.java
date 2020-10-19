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

public class AssessmentService {


    public List<AssessmentEntity> getAllAssessments() {
        Transaction tx = null;
        List<AssessmentEntity> result = Collections.EMPTY_LIST;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            result = session.createCriteria(AssessmentEntity.class).list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }

        return result;
    }

    public AssessmentEntity getAssessmentByQuestionId(int id) {
        Transaction tx = null;

        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE WHERE AE.id = :assessment_id";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("assessment_id", id);
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

        if (results.size() == 1)
            return results.get(0);
        else {
            System.err.println(">> No Assessments with id=" + id);
            return null;
        }
    }

    public List<AssessmentEntity> getAssessmentByDatesAndQuestionId(Date beginDate, Date endDate, int id) {
        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE " +
                "WHERE AE.questionId = :question_id and AE.date BETWEEN :startDate AND :endDate ORDER BY AE.date";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
            query.setParameter("question_id", id);
            query.setParameter("startDate", beginDate, TemporalType.DATE);
            query.setParameter("endDate", endDate, TemporalType.DATE);
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

//    public List<AssessmentEntity> getAssessmentsForModalFilter () {
//        Transaction tx = null;
//        List<AssessmentEntity> results = Collections.EMPTY_LIST;
//        String hql = "select distinct category_id, question_id, category_name, question from ASSESSMENT order by 1, 2";
//
//
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        try {
//            tx = session.beginTransaction();
//
//            Query query = session.createQuery(hql);
//
//            results = query.list();
//            tx.commit();
//        } catch (HibernateException ex) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            ex.printStackTrace(System.err);
//        } finally {
//            session.close();
//        }
//        return results;
//    }
}

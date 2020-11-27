package db.service;

import db.HibernateUtil;
import db.entity.AssessmentEntity;
import fhdo.Survey;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AssessmentService {

//todo: delete transations for read

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


    public List<Survey> getSurveysByDatesAndName(Date chart1_db0, Date chart1_db1, Integer surveyType) {
        Transaction tx = null;
        List<Object[]> result = Collections.EMPTY_LIST;

        String hql = "select distinct surveyId, surveyName FROM AssessmentEntity AE " +
                "WHERE AE.surveyId = :surveyId and AE.date BETWEEN :startDate AND :endDate ORDER BY AE.date"; //todo: change to Id or to type

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
            query.setParameter("surveyId", surveyType);
            query.setParameter("startDate", chart1_db0, TemporalType.DATE);
            query.setParameter("endDate", chart1_db1, TemporalType.DATE);
            // query.uniqueResult();
            result = query.list();

            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }


        List<Survey> surveys = new ArrayList<>();
        for (Object[] obj : result) {
            surveys.add(new Survey(String.valueOf(obj[0]), String.valueOf(obj[1])));
        }

        return surveys;
    }

    public List<Survey> getSurveysByDates(Date chart1_db0, Date chart1_db1) {
        Transaction tx = null;
        List<Object[]> result = Collections.EMPTY_LIST;

        String hql = "select distinct surveyId, surveyName FROM AssessmentEntity AE ";

        if (chart1_db0 != null && chart1_db1 != null)
            hql += " WHERE   AE.date BETWEEN :startDate AND :endDate ORDER BY AE.date";

        else if (chart1_db0 != null)
            hql += " WHERE   AE.date >= :startDate ORDER BY AE.date";

        else if (chart1_db1 != null)
            hql += " WHERE   AE.date <= :endDate ORDER BY AE.date";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
            if (chart1_db0 != null && chart1_db1 != null) {
                query.setParameter("startDate", chart1_db0, TemporalType.DATE);
                query.setParameter("endDate", chart1_db1, TemporalType.DATE);
            } else if (chart1_db0 != null) {
                query.setParameter("startDate", chart1_db0, TemporalType.DATE);
            } else if (chart1_db1 != null) {
                query.setParameter("endDate", chart1_db1, TemporalType.DATE);
            }
            result = query.list();

            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }


        List<Survey> surveys = new ArrayList<>();
        for (Object[] obj : result) {
            surveys.add(new Survey(String.valueOf(obj[0]), String.valueOf(obj[1])));
        }

        return surveys;
    }

    public AssessmentEntity getAssessmentById(int id) {
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

    public List<AssessmentEntity> getAssessmentByDatesAndCategoryIdQuestionId(Date beginDate, Date endDate, int categoryId, int questionId) {
        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE " +
                "WHERE AE.questionId = :question_id and AE.categoryId = :category_id and AE.date BETWEEN :startDate AND :endDate ORDER BY AE.date";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
            query.setParameter("question_id", questionId);
            query.setParameter("category_id", categoryId);
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

    public List<AssessmentEntity> getAssessmentByDatesAndWhereClause(Date beginDate, Date endDate, String whereClause) {
        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE " +
                "WHERE  AE.date BETWEEN :startDate AND :endDate  " +
                whereClause;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
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

    public List<AssessmentEntity> getAssessmentEventsDateDesc() {
        Transaction tx = null;
        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE " +
                "ORDER BY AE.date desc";

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

    public static List<AssessmentEntity> getAllWarnings() {
        Transaction tx = null;
        List<AssessmentEntity> result = Collections.EMPTY_LIST;


        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            //only the latest survey is important
            String hql = "from AssessmentEntity where date = (select max(date) from AssessmentEntity)";
            Query query = session.createQuery(hql);

            result = query.list();

            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace(System.err);
        } finally {
            session.close();
        }
        List<AssessmentEntity> resultTmp = new ArrayList<>();
        int maxValue;
        int minValue;
        int value;
        for (AssessmentEntity ae : result) {
            maxValue = Integer.parseInt(ae.getMaxValue());
            minValue = Integer.parseInt(ae.getMinValue());
            if (ae.getValue() != null) {
                value = ae.getValue();
                // from HeatMap Colors: if (value <= max + (max - min) * 0.75 && value >= max - (max - min) * 0.75) {
                if (value > maxValue + (maxValue - minValue) * 0.75
                        || value < maxValue - (maxValue - minValue) * 0.75)
                    resultTmp.add(ae);
            }

        }
        return resultTmp;
    }


    public List<AssessmentEntity> getAssessmentByQuestionId(int questionId) {
        Transaction tx = null;

        List<AssessmentEntity> results = Collections.EMPTY_LIST;
        String hql = "FROM AssessmentEntity AE WHERE AE.questionId = :questionId ORDER BY AE.date";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("questionId", questionId);
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

package db.service;

import db.HibernateUtil;
import db.entity.AssessmentEntity;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

import java.util.Collections;
import java.util.List;

public class TreeDynamicService {


    public List<Object[]> getCategoriesAndQuestions() {
        Transaction tx = null;
        List result = Collections.EMPTY_LIST;

        String hql = "select distinct categoryId, questionId, categoryName, question from AssessmentEntity order by 1, 2";


        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();

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

        return result;
    }
}

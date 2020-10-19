package db.service;

import db.HibernateUtil;
import db.entity.AssessmentEntity;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarningService {


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
            value = ae.getValue();
            // from HeatMap Colors: if (value <= max + (max - min) * 0.75 && value >= max - (max - min) * 0.75) {
            if (value > maxValue + (maxValue - minValue) * 0.75
                    || value < maxValue - (maxValue - minValue) * 0.75)
                resultTmp.add(ae);

        }
        return resultTmp;
    }

}

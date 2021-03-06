package db;

import db.service.AssessmentService;
import db.service.TreeSearchService;
import fhdo.Survey;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Date;
import java.util.List;


// if (PermissionHelper.getInstance().grantAndShowAccessMessage(PermissionTag.PATIENTLIST, "Patientenliste")) {

public class HibernateUtil {
    static SessionFactory sessionFactoryObj;

    private static SessionFactory buildSessionFactory() {
        Configuration configObj = new Configuration();
        configObj.configure("hibernate.cfg.xml");
        configObj.addAnnotatedClass(db.entity.AssessmentEntity.class);

        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();
        sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
        return sessionFactoryObj;
    }


    public static SessionFactory getSessionFactory() {
        if (sessionFactoryObj == null) {
            sessionFactoryObj = buildSessionFactory();
        }
        return sessionFactoryObj;
    }


    //todo: delete
    public static void main(String[] args) {
//        List<AssessmentEntity> list = new AssessmentService().getAllAssessments();
//        AssessmentEntity assessment = new AssessmentService().getAssessmentByQuestionId(1);
//        List<AssessmentEntity> listAssessments = new AssessmentService().getAssessmentByDatesAndQuestionId(new Date(1990), new Date(), 1);

        List<Survey> listAssessments = new AssessmentService().getSurveysByDatesAndName(new Date(1990), new Date(), 1);
        System.out.println("after select");


    }
}
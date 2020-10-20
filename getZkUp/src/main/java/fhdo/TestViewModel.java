package fhdo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import java.util.Date;

public class TestViewModel {
    static Assessment assessment;
    static Session sessionObj;
    static SessionFactory sessionFactoryObj;


    @Command("render")
    public void render() {
        System.out.println("render executed");
        //nothing

//        try {
//            sessionObj = buildSessionFactory().openSession();
//            sessionObj.beginTransaction();
//
//            for (int i = 101; i <= 105; i++) {
//                assessment = new Assessment();
//                assessment.setId(i);
//                assessment.setQuestion("Question " + i);
//                assessment.setDate(new Date());
//
//                sessionObj.save(assessment);
//            }
//            System.out.println("\n.......Records Saved Successfully To The Database.......\n");
//
//            // Committing The Transactions To The Database
//            sessionObj.getTransaction().commit();
//        } catch (Exception sqlException) {
//            if (null != sessionObj.getTransaction()) {
//                System.out.println("\n.......Transaction Is Being Rolled Back.......");
//                sessionObj.getTransaction().rollback();
//            }
//            sqlException.printStackTrace();
//        } finally {
//            if (sessionObj != null) {
//                sessionObj.close();
//            }
//        }
    }

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);
        String script = "list = document.getElementsByClassName(\"pointer\");\n" +
                "    for (var i = 0; i < list.length; i++) {\n" +
                "        list[i].addEventListener(\"click\", function(e) {\n" +
                "           document.getElementById(\"info\").innerHTML  = this.dataset.hint;\n" +
                " open();" +
                "        });\n" +
                "    }";
        Clients.evalJavaScript(script);

    }

    @Listen("onTest=#btnOpen")
    public void onTest(Event evt) {

        Messagebox.show("1234");


        try {
            sessionObj = buildSessionFactory().openSession();
            sessionObj.beginTransaction();

            for (int i = 101; i <= 105; i++) {
                assessment = new Assessment();
                assessment.setId(i);
                assessment.setQuestion("Question " + i);
                assessment.setDate(new Date());

                sessionObj.save(assessment);
            }
            System.out.println("\n.......Records Saved Successfully To The Database.......\n");

            // Committing The Transactions To The Database
            sessionObj.getTransaction().commit();
        } catch (Exception sqlException) {
            if (null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }


    }

    @Listen("onTest")
    public void onTest2(Event evt) {

        Messagebox.show("onTest2");
    }


    private static SessionFactory buildSessionFactory() {
        // Creating Configuration Instance & Passing Hibernate Configuration File
        Configuration configObj = new Configuration();
        configObj.configure("hibernate.cfg.xml");

        // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();

        // Creating Hibernate SessionFactory Instance
        sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
        return sessionFactoryObj;
    }


}
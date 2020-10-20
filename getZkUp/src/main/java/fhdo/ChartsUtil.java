package fhdo;

import org.zkoss.zk.ui.Executions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ChartsUtil {

    public static final String SCRIPT_PATH = "scripts/charts/patterns/";

    /**
     * Reads the js file of chart
     *
     * @param fileName name of the file without extension
     * @return String to execute by Clients.evalJavaScript(script);
     */
    public String compileChart(String fileName) {

        StringBuffer sb = new StringBuffer();
        try {
            String directoryPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + SCRIPT_PATH;
            File myObj = new File(directoryPath + fileName + ".js");
            Scanner myReader = new Scanner(myObj, "UTF-8");
            while (myReader.hasNextLine()) {
                String newLine = myReader.nextLine() + "\n";
                sb.append(newLine);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("::File not found: " + SCRIPT_PATH + fileName + ".js: " + e);
            return "console.log('Error occurred: ' + " + e + ")";
        }
        return sb.toString();
    }

    /**
     * Adds to the end of file. Invokes the method
     *
     * @param fileName   name of the file without extension
     * @param methodName name of the method to execute
     * @return String to execute by Clients.evalJavaScript(script);
     */
    public String compileChart(String fileName, String methodName) {

        StringBuffer sb = new StringBuffer(compileChart(fileName));
        sb.append(methodName)
                .append("();");
        return sb.toString();
    }


    /**
     * Adds to the end of file. Invokes the method with params
     *
     * @param fileName   name of the file without extension
     * @param methodName name of the method to execute
     * @param params     params for the execution method
     * @return String to execute by Clients.evalJavaScript(script);
     */
    public String compileChart(String fileName, String methodName, String params) {

        StringBuffer sb = new StringBuffer(compileChart(fileName));
        sb.append(methodName)
                .append("(")
                .append(params)
                .append(");");
        return sb.toString();
    }
}

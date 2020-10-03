package tutorial;

import org.zkoss.zk.ui.Executions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ChartsHelper {

    public static final String PATH_CHARTS_SCRIPTS_PATTERNS = "scripts/charts/patterns/";
    private String chartName;

    public ChartsHelper() {
    }

    public ChartsHelper(String chartName) {
        this.chartName = chartName;
    }

    public String readChartFile() {

        StringBuffer sb = new StringBuffer();
        try {
            String directoryPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + PATH_CHARTS_SCRIPTS_PATTERNS;
            File myObj = new File(directoryPath + chartName + ".js");
            Scanner myReader = new Scanner(myObj, "UTF-8");
            while (myReader.hasNextLine()) {
                String newLine = myReader.nextLine() + "\n";
                sb.append(newLine);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("::File not found: " + chartName + "\n " + e);
            return "console.log('Error occurred: ' + " + e + ")";
        }
        return sb.toString();
    }
}

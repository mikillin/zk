package tutorial;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.util.Clients;

import java.util.LinkedList;
import java.util.List;

public class FragebogenServiceImpl implements FragebogenService {

    private List<Fragebogen> frageboegenList = new LinkedList<Fragebogen>();

    public FragebogenServiceImpl() {


        // Beispiele
        String json1 = "{\"id\":1, \"name\":\"PHQ-D\", \"date\":\"01.01.2021\", \"status\":\"fertig\"}";
        String json2 = "{\"id\":2, \"name\":\"IES-R\", \"date\":\"03.11.2022\", \"status\":\"Zugewiesen\"}";
        String json21 = "{\"id\":21, \"name\":\"IES-R\", \"date\":\"03.03.2022\", \"status\":\"ergänzt\"}";
        String json3 = "{\"id\":3, \"name\":\"PHQ-D\", \"date\":\"02.02.2023\", \"status\":\"angefangen\"}";
        String json4 = "{\"id\":4, \"name\":\"PHQ-D\", \"date\":\"04.04.2024\", \"status\":\"ergänzt\"}";
        String json5 = "{\"id\":5, \"name\":\"PHQ-D\", \"date\":\"04.04.2024\", \"status\":\"ergänzt\"}";


//        Gson dataForItem1 = new Gson();


        frageboegenList.add(this.parseFragebogenFromString(json1));
        frageboegenList.add(this.parseFragebogenFromString(json2));
        frageboegenList.add(this.parseFragebogenFromString(json21));
        frageboegenList.add(this.parseFragebogenFromString(json4));
        frageboegenList.add(this.parseFragebogenFromString(json5));
        frageboegenList.add(this.parseFragebogenFromString(json3));


//        Clients.showNotification("Loaded!");

    }

    @Override
    public List<Fragebogen> findAllFrageboegen() {
        return searchFragebogen("");
    }

    public List<Fragebogen> searchFragebogen(String keyword) {
        List<Fragebogen> result = new LinkedList<Fragebogen>();
        if (StringUtils.isEmpty(keyword)) {
            result = frageboegenList;
        } else {
            for (Fragebogen fragebogen : frageboegenList) {
                if (fragebogen.getDate().toLowerCase().contains(keyword.toLowerCase())
                        || fragebogen.getName().toLowerCase().contains(keyword.toLowerCase())
                        || fragebogen.getStatus().toLowerCase().contains(keyword.toLowerCase())) {
                    result.add(fragebogen);
                }
            }
        }
        return result;
    }

    private Fragebogen parseFragebogenFromString(String src){
        return new Gson().fromJson(new JsonParser().parse(src).getAsJsonObject(), Fragebogen.class);
    }

}

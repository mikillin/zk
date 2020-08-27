package tutorial.inline;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import tutorial.Fragebogen;

import java.util.ArrayList;
import java.util.List;

public class FragebogenData {


    private final List<Fragebogen> allFrageboegen = new ArrayList<Fragebogen>();


    public FragebogenData() {
        // Beispiele
        String json1 = "{\"id\":1, \"name\":\"PHQ-D\", \"date\":\"01.01.2021\", \"status\":\"fertig\"}";
        String json2 = "{\"id\":2, \"name\":\"IES-R\", \"date\":\"03.11.2022\", \"status\":\"Zugewiesen\"}";
        String json21 = "{\"id\":21, \"name\":\"IES-R\", \"date\":\"03.03.2022\", \"status\":\"ergänzt\"}";
        String json3 = "{\"id\":3, \"name\":\"PHQ-D\", \"date\":\"02.02.2023\", \"status\":\"angefangen\"}";
        String json4 = "{\"id\":4, \"name\":\"PHQ-D\", \"date\":\"04.04.2024\", \"status\":\"ergänzt\"}";
        String json5 = "{\"id\":5, \"name\":\"PHQ-D\", \"date\":\"04.04.2024\", \"status\":\"ergänzt\"}";
//        frageboegenList.add();
        JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

//            );

        allFrageboegen.add(new Gson().fromJson(new JsonParser().parse(json1).getAsJsonObject(), Fragebogen.class));
        allFrageboegen.add(new Gson().fromJson(new JsonParser().parse(json2).getAsJsonObject(), Fragebogen.class));
        allFrageboegen.add(new Gson().fromJson(new JsonParser().parse(json21).getAsJsonObject(), Fragebogen.class));
        allFrageboegen.add(new Gson().fromJson(new JsonParser().parse(json4).getAsJsonObject(), Fragebogen.class));
        allFrageboegen.add(new Gson().fromJson(new JsonParser().parse(json5).getAsJsonObject(), Fragebogen.class));
        allFrageboegen.add(new Gson().fromJson(new JsonParser().parse(json3).getAsJsonObject(), Fragebogen.class));


    }

    public List<Fragebogen> getAllFrageboegen() {
        return allFrageboegen;
    }
}


package fhdo;

import com.google.gson.Gson;

public class Survey {
    private String id;
    private String name;
    private String date;
    private String status;
    private Gson data;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Gson getData() {
        return data;
    }

    public void setData(Gson data) {
        this.data = data;
    }
}

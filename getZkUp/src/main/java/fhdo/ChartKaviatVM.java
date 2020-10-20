package fhdo;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import java.util.*;


public class ChartKaviatVM {


    private Date chart1_db0;
    private Date chart1_db1;


    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        this.renderChart();
    }


    @Command
    public void renderChart() {
        Clients.evalJavaScript(new ChartsUtil().compileChart("kaviat"));

    }


    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);
        new ChartsUtil().compileChart("kaviat");

    }

    public Date getChart1_db0() {
        return chart1_db0;
    }

    public void setChart1_db0(Date chart1_db0) {
        this.chart1_db0 = chart1_db0;
    }

    public Date getChart1_db1() {
        return chart1_db1;
    }

    public void setChart1_db1(Date chart1_db1) {
        this.chart1_db1 = chart1_db1;
    }
}

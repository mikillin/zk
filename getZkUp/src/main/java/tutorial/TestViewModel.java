package tutorial;

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

public class TestViewModel {


    @Command("render")
    public void render() {
        //nothing
    }

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);

        String script = "let render = function() {\n" +
                "    let sourceJSON = getJSON();\n" +
                "    const svg1 = document.createElementNS(\"http://www.w3.org/2000/svg\", \"svg\");\n" +
                "    let title = document.createElementNS(\"http://www.w3.org/2000/svg\", \"title\");\n" +
                "    let text = document.createElementNS(\"http://www.w3.org/2000/svg\", \"text\");\n" +
                "    let g = document.createElementNS(\"http://www.w3.org/2000/svg\", \"g\");\n" +
                "    svg1.setAttribute(\"width\", \"1300\");\n" +
                "    svg1.setAttribute(\"height\", \"1300\");\n" +
                "\n" +
                "    rect1 = document.createElementNS(\"http://www.w3.org/2000/svg\", \"rect\");\n" +
                "    rect1.setAttribute(\"x\", 200);\n" +
                "    rect1.setAttribute(\"y\", 200);\n" +
                "    rect1.setAttribute(\"width\", \"20\");\n" +
                "    rect1.setAttribute(\"height\", \"40\");\n" +
                "    rect1.setAttribute(\"stroke-width\", \"1\");\n" +
                "    let title = document.createElementNS(\"http://www.w3.org/2000/svg\", \"title\");\n" +
                "    title.innerHTML = \"Inner HTML\";\n" +
                "    rect1.appendChild(title);\n" +
                "\n" +
                "    rect1.setAttribute(\"fill\", \"red\");\n" +
                "    rect1.setAttribute(\"stroke\", \"orange\");\n" +
                "\n" +
                "    g.appendChild(rect1);\n" +
                "    svg1.appendChild(g);\n" +
                "\n" +
                "\n" +
                "    let svgParent = document.getElementsByClassname(\"svgClass\")[0];\n" +
                "    svgParent.appendChild(svg1);\n" +
                "\n" +
                "\n" +
                "    d3.selectAll('rect')\n" +
                "        .on('click', function(d, i) {\n" +
                "            d3.select(this)\n" +
                "                .style('fill', 'orange');\n" +
                "\n" +
                "            open();\n" +
                "\n" +
                "        })\n" +
                "}\n" +
                "render();";

        script = "list = document.getElementsByClassName(\"pointer\");\n" +
                "    for (var i = 0; i < list.length; i++) {\n" +
                "        list[i].addEventListener(\"click\", function(e) {\n" +
                "           document.getElementById(\"info\").innerHTML  = this.dataset.hint;\n" +
                " open();"+
                "        });\n" +
                "    }";
        Clients.evalJavaScript(script);

    }

    @Listen("onTest=#btnOpen")
    public void onTest(Event evt) {
        Messagebox.show("1234");
    }

    @Listen("onTest")
    public void onTest2(Event evt) {
        Messagebox.show("onTest2");
    }


}
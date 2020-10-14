function renderRadarChart() {
    var lineFunction = d3.svg.line()
        .x(function(d) { return d.x; })
        .y(function(d) { return d.y; })
        .interpolate('linear');



    // TODO: refactor
    var height = 460;
    var width = 460;

    var max = app11.response.data.minimumValue;
    var min = app11.response.data.maximumValue;
    var step = app11.response.data.stepSize;

    var lineData = [];


    //The SVG Container
    var svgContainer = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("background-color", "blue")
        .on('click', function () {
            recalculate(d3.mouse(this)) // log the mouse x,y position
        });
    ;

   // console.log("svg - " + svgContainer)


    function drawSteps(countAxis, axe, min, max, step) {

        let stepsAmount = (max - min) / step;

        var axisLength = (height / 2) - 40;
        var centerX = width / 2;
        var centerY = height / 2;

        var offset = 30;
        var stepLenght = (axisLength - offset) / stepsAmount;

        //axes drawing
        for (var i = 0; i < countAxis; ++i) {
            var degree = 360.0 / countAxis * i;

            for (var j = 0; j <= stepsAmount; j++) {
                var axeX = centerX + (stepLenght * j + offset) * Math.sin(toRadians(degree));
                var axeY = centerY + (stepLenght * j + offset) * Math.cos(toRadians(degree));

                svgContainer.append("circle")
                    .attr("cx", axeX)
                    .attr("cy", axeY)
                    .attr("r", 5);

                svgContainer.append("text")
                    .text(min + step * j)
                    .attr("y", axeY + 10)
                    .attr("x", axeX + 10)
                    .attr("font-size", 12)
                    .attr("font-family", "sans-serif")
                    .attr("fill", "black"); //TODO: color to change

                lineData.push({"achse": i, "data": {"wert": j, "koordination": {"x": axeX, "y": axeY}}});
            }
        }
    }


    //TODO: only for test
    var countAxis = 3;


    var axisLength = (height / 2) - 40;
    var centerX = width / 2;
    var centerY = height / 2;
    var lineData2 = [];
    for (var i = 0; i < countAxis; ++i) {
        var degree = 360.0 / countAxis * i;
        var axeX = centerX + axisLength * Math.sin(toRadians(degree));
        var axeY = centerY + axisLength * Math.cos(toRadians(degree));

        drawLine(centerX, centerY, axeX, axeY, svgContainer);

        //TODO: from server
        var axesNames = ["Axes1", "Axes2", "Axes3"];

        svgContainer.append("text")
            .attr("x", axeX)
            .attr("y", axeY)
            .attr("text-anchor", "middle")
            .style("font-size", "16px")
            .style("text-decoration", "underline")
            .text(axesNames[i]);


        lineData2.push({"x": axeX, "y": axeY});

        //TODO: put correct data
        drawSteps(3, i, 5, 10, 1)
    }


    lineData2.push({
        "x": centerX + axisLength * Math.sin(toRadians(0)),
        "y": centerY + axisLength * Math.cos(toRadians(0))
    }); //to refactor

    //The line SVG Path we draw
    var lineGraph = svgContainer.append("path")
        .attr("d", lineFunction(lineData2))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "none");

    //TODO: von Server neue Bewertung
    var data = [{"axe": 0, "value": 2}, {"axe": 1, "value": 5}, {"axe": 2, "value": 3}];

    var assessment = [];
    for (i = 0; i < data.length; i++) {
        for (j = 0; j < lineData.length; j++) {
            if (lineData[j].achse == data[i].axe && lineData[j].data.wert == data[i].value) {
                assessment.push(lineData[j].data.koordination);
                break;
            }
        }
    }

    //TODO:  console.log(assessment);
    var lineGraph = svgContainer.append("path")
        .attr("d", lineFunction(assessment))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "red")
        .attr("opacity", 0.5)
        .attr("id", "bewertung");


    //von Server Ziel
    var data2 = [{"axe": 0, "value": 5}, {"axe": 1, "value": 3}, {"axe": 2, "value": 4}];

    // var lineGraph = svgContainer.append("path")
    //     .attr("d", lineFunction(assessment))
    //     .attr("stroke", "blue")
    //     .attr("stroke-width", 2)
    //     .attr("fill", "red")
    //     .attr("opacity", 0.5)
    //     .attr("id", "ziel");
    //console.log("deleted ziel path");

    var assessment = [];
    for (i = 0; i < data2.length; i++) {
        for (j = 0; j < lineData.length; j++) {
            if (lineData[j].achse == data2[i].axe && lineData[j].data.wert == data2[i].value) {
                assessment.push(lineData[j].data.koordination);
                break;
            }

        }
    }

    var lineGraph = svgContainer.append("path")
        .attr("d", lineFunction(assessment))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "yellow")
        .attr("opacity", 0.5);
    zielData = assessment;


    function recalculate(evt) {
        x = evt[0];
        y = evt[1];

        if (lineData.length > 0) {
            x_wert = lineData[0].data.koordination.x;
            y_wert = lineData[0].data.koordination.y;
        }

        distance = Math.sqrt(Math.pow(x - x_wert, 2) + Math.pow(y - y_wert, 2));
        axe = 0;
        value = 0;
        for (j = 0; j < lineData.length; j++) {
            x_wert = lineData[j].data.koordination.x;
            y_wert = lineData[j].data.koordination.y;
            current_distance = Math.sqrt(Math.pow(x - x_wert, 2) + Math.pow(y - y_wert, 2));
            if (distance > current_distance && current_distance < 100) { //TODO: change to step /2 as a limit
                distance = current_distance;
                axe = lineData[j].achse;
                value = lineData[j].data.wert;
            }
        }

        //got the axe and new value
        for (j = 0; j < data.length; j++) {
            if (data[j].axe == axe)
                //console.log("Coordinations: " + lineData[j].data.koordination.x + ":: " + lineData[j].data.koordination.y );
            {
                console.log("=> changed:" + data[j].axe + ", value become " + value);
                data[j].value = value;
                // console.log("=> new data[j]:" + data[j]);
                break;
            }

        }

        // redraw
        d3.selectAll("path").remove();

        assessment = [];
        for (i = 0; i < data.length; i++) {
            // console.log("lineData" + data[i].value );
            for (j = 0; j < lineData.length; j++) {
                if (lineData[j].achse == data[i].axe && lineData[j].data.wert == data[i].value) {
                    assessment.push(lineData[j].data.koordination);
                    break;
                }

            }
        }

        var lineGraph = svgContainer.append("path")
            .attr("d", lineFunction(assessment))
            .attr("stroke", "blue")
            .attr("stroke-width", 2)
            .attr("fill", "red")
            .attr("opacity", 0.5)
            .attr("id", "bewertung");


        var lineGraph = svgContainer.append("path")
            .attr("d", lineFunction(lineData2))
            .attr("stroke", "blue")
            .attr("stroke-width", 2)
            .attr("fill", "none");

        // var lineGraph = svgContainer.append("path")
        //     .attr("d", lineFunction(assessment))
        //     .attr("stroke", "blue")
        //     .attr("stroke-width", 2)
        //     .attr("fill", "red")
        //     .attr("opacity", 0.5)
        //     .attr("id", "ziel");

        var lineGraph = svgContainer.append("path")
            .attr("d", lineFunction(zielData))
            .attr("stroke", "blue")
            .attr("stroke-width", 2)
            .attr("fill", "yellow")
            .attr("opacity", 0.5);

    }

}

function toRadians(degrees) {
    return degrees * (Math.PI / 180);
}

function drawLine(x1, y1, x2, y2, svgContainer) {

    console.log("svgContainer --- : " + svgContainer);
    svgContainer.append("line")
        .attr("x1", x1)
        .attr("y1", y1)
        .attr("x2", x2)
        .attr("y2", y2)
        .attr("class", "line")
}
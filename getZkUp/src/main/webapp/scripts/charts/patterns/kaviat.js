let map = {};
var chartHeight = 460;
var chartWidth = 460;

map = {
    "chartHeight": chartHeight,
    "chartWidth": chartWidth,
    "data": [{"axe": 0, "value": 2}, {"axe": 1, "value": 5}, {"axe": 2, "value": 3}],
    "axesNames": ["Axes1", "Axes2", "Axes3"]
}

function renderRadarChart(map) {

    var lineFunction = d3.line()
        .x(function (d) {
            return d.x;
        })
        .y(function (d) {
            return d.y;
        })
        .curve(d3.curveLinear);

    // var lineFunction = d3.svg.line()
    //     .x(function(d) { return d.x; })
    //     .y(function(d) { return d.y; })
    //     .interpolate('linear');


    // TODO: refactor
    var height = map.chartHeight;
    var width = map.chartWidth;


    //todo: from server
    var max = 2;
    var min = 10;
    var step = 1;

    var lineData = [];


    //The SVG Container
    var svgContainer = d3.select("body").append("svg")
        .attr("width", map.chartWidth)
        .attr("height", map.chartHeight)
        .attr("background-color", "blue");
    ;

    function drawSteps(countAxis, axe, min, max, step) {

        let stepsAmount = (max - min) / step;

        var axisLength = (map.chartHeight / 2) - 40;
        var centerX = map.chartWidth / 2;
        var centerY = map.chartHeight / 2;

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
                    .attr("fill", "black");

                lineData.push({"achse": i, "data": {"wert": j, "koordination": {"x": axeX, "y": axeY}}});
            }
        }
    }


    //change to test
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

        svgContainer.append("text")
            .attr("x", axeX)
            .attr("y", axeY)
            .attr("text-anchor", "middle")
            .style("font-size", "16px")
            .style("text-decoration", "underline")
            .text(map.axesNames[i]);


        lineData2.push({"x": axeX, "y": axeY});

        //TODO: put correct data
        drawSteps(map.axesNames.length, i, 5, 10, 1)
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

    var assessment = [];
    for (i = 0; i < map.data.length; i++) {
        for (j = 0; j < lineData.length; j++) {
            if (lineData[j].achse == map.data[i].axe && lineData[j].data.wert == map.data[i].value) {
                assessment.push(lineData[j].data.koordination);
                break;
            }
        }
    }
    if (assessment.length > 0)
        assessment.push(assessment[0]); //to close the area

    var lineGraph = svgContainer.append("path")
        .attr("d", lineFunction(assessment))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "green")
        .attr("opacity", 0.5)
        .attr("id", "bewertung");

}

function toRadians(degrees) {
    return degrees * (Math.PI / 180);
}

function drawLine(x1, y1, x2, y2, svgContainer) {
    svgContainer.append("line")
        .attr("x1", x1)
        .attr("y1", y1)
        .attr("x2", x2)
        .attr("y2", y2)
        .attr("class", "line")
}

renderRadarChart(map);
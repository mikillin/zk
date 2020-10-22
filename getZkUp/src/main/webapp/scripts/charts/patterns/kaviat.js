let map =  {
    "chartHeight": 460,
    "chartWidth": 460,
    "data": [
        { "axe": 0, "value": 2 },
        { "axe": 1, "value": 4 },
        { "axe": 2, "value": 3 },
        { "axe": 3, "value": 3 },
        { "axe": 4, "value": 3 }
    ],
    "axisData": [
        { "axe": 0, "info": { "min": 1, "max": 10, "step": 2 } },
        { "axe": 1, "info": { "min": 2, "max": 4, "step": 1 } },
        { "axe": 2, "info": { "min": 2, "max": 10, "step": 1 } },
        { "axe": 3, "info": { "min": 2, "max": 10, "step": 1 } },
        { "axe": 4, "info": { "min": 2, "max": 10, "step": 1 } },
    ],
    "axesNames": ["Axes1", "Axes2", "Axes3", "Axes4", "Axes5"]
}

function renderRadarChart(map) {

    var lineFunction = d3.line()
        .x(function(d) {
            return d.x;
        })
        .y(function(d) {
            return d.y;
        })
        .curve(d3.curveLinear);

    //depends on the version of d3 library
    // var lineFunction = d3.svg.line()
    //     .x(function(d) { return d.x; })
    //     .y(function(d) { return d.y; })
    //     .interpolate('linear');



    var height = map.chartHeight;
    var width = map.chartWidth;

    var dataAreaCoordinates = [];

    var svgContainer = d3.select("body").append("svg")
        .attr("width", map.chartWidth)
        .attr("height", map.chartHeight)
        .attr("background-color", "blue");;

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

                dataAreaCoordinates.push({ "axe": i, "data": { "value": j, "coordinate": { "x": axeX, "y": axeY } } });
            }
        }
    }



    var countAxis = map.data.length;
    var axisLength = (height / 2) - 40;
    var centerX = width / 2;
    var centerY = height / 2;
    var outerCoordinates = [];
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


        outerCoordinates.push({ "x": axeX, "y": axeY });

        for (let d = 0; d < map.axisData.length; d++)
            if (map.axisData[d].axe == i) {
                drawSteps(map.axesNames.length, i, map.axisData[d].info.min, map.axisData[d].info.max, map.axisData[d].info.step)
                break;
            }
    }


    outerCoordinates.push({
        "x": centerX + axisLength * Math.sin(toRadians(0)),
        "y": centerY + axisLength * Math.cos(toRadians(0))
    });


    var lineGraph = svgContainer.append("path")
        .attr("d", lineFunction(outerCoordinates))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "none");

    var assessment = [];
    for (i = 0; i < map.data.length; i++) {
        for (j = 0; j < dataAreaCoordinates.length; j++) {
            if (dataAreaCoordinates[j].axe == map.data[i].axe && dataAreaCoordinates[j].data.value == map.data[i].value) {
                assessment.push(dataAreaCoordinates[j].data.coordinate);
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
        .attr("fill", "blue")
        .attr("opacity", 0.5)
        .attr("id", "bevalueung");

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
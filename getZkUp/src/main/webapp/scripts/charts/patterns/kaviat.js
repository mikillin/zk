let map = {
    "chartData": {
        "chartHeight": 460,
        "chartWidth": 460,
        "centerOffset": 30
    },
    "axisData": [
        { "axe": 0, "name": "Axes1", "value": 5, "displayValue": { "1": "Ja", "2": "Nein" }, "info": { "min": 1, "max": 10, "step": 1 } },
        { "axe": 1, "name": "Axes2", "value": 2, "info": { "min": 1, "max": 6, "step": 1 } },
        { "axe": 2, "name": "Axes3", "value": 3, "info": { "min": 1, "max": 7, "step": 1 } },
        { "axe": 3, "name": "Axes4", "value": 4, "info": { "min": 1, "max": 8, "step": 1 } },
        { "axe": 4, "name": "Axes5", "value": 5, "info": { "min": 1, "max": 9, "step": 1 } }
    ]
}


function renderRadarChart(map) {

    let lineFunction = d3.line()
        .x(function(d) {
            return d.x;
        })
        .y(function(d) {
            return d.y;
        })
        .curve(d3.curveLinear);

    //depends on the version of d3 library
    // let lineFunction = d3.svg.line()
    //     .x(function(d) { return d.x; })
    //     .y(function(d) { return d.y; })
    //     .interpolate('linear');

    let svgContainer = d3.select("body").append("svg")
        .attr("width", map.chartData.chartWidth)
        .attr("height", map.chartData.chartHeight)
        .attr("background-color", "blue");


    svgContainer.on("click", function(event) {


        console.log(">> x= " + event.clientX + ", y= " + event.clientY);
    });


    let drawAreaInformation = initDrawAreaInformation(map.chartData, map.axisData);

    let axisLength = (map.chartData.chartHeight / 2) - 40;
    let centerX = map.chartData.chartWidth / 2;
    let centerY = map.chartData.chartHeight / 2;

    let outerCoordinates = [];
    for (let i = 0; i < map.axisData.length; ++i) {
        //calculate data for axes
        let degree = 360.0 / map.axisData.length * i;
        let axeX = centerX + axisLength * Math.sin(toRadians(degree));
        let axeY = centerY + axisLength * Math.cos(toRadians(degree));

        //draw axes
        drawLine(centerX, centerY, axeX, axeY, svgContainer);

        //add names
        let percent = Math.round((map.axisData[i].value - map.axisData[i].info.min) * 100 / (map.axisData[i].info.max - map.axisData[i].info.min));
        svgContainer.append("text")
            .attr("x", axeX)
            .attr("y", axeY)
            .attr("text-anchor", "middle")
            .style("font-size", "16px")
            .style("text-decoration", "underline")
            .text(map.axisData[i].name + " (" + percent + "%)");

        outerCoordinates.push({ "x": axeX, "y": axeY });
    }
    //form  outer border coordinates
    outerCoordinates.push({
        "x": centerX + axisLength * Math.sin(toRadians(0)),
        "y": centerY + axisLength * Math.cos(toRadians(0))
    });

    //todo: insert angles for rotating the name text 
    //draw outer border
    let outerBorder = svgContainer.append("path")
        .attr("d", lineFunction(outerCoordinates))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "none");

    //calculate assessments coordinates
    //add nearest coordinate
    let assessment = [];
    for (i = 0; i < map.axisData.length; i++) {
        let nearestStepCoordinate;
        let smallestDifference = null;

        for (j = 0; j < drawAreaInformation.length; j++) {
            if (smallestDifference == null && drawAreaInformation[j].axe == map.axisData[i].axe) {
                smallestDifference = Math.abs(drawAreaInformation[j].data.value - map.axisData[i].value);
                nearestStepCoordinate = drawAreaInformation[j].data.coordinate;
            }
            if (drawAreaInformation[j].axe == map.axisData[i].axe)
                if (Math.abs(drawAreaInformation[j].data.value - map.axisData[i].value) < smallestDifference) {

                    smallestDifference = Math.abs(drawAreaInformation[j].data.value - map.axisData[i].value);
                    nearestStepCoordinate = drawAreaInformation[j].data.coordinate;
                    console.log("map.axisData[i].name: " + map.axisData[i].name + ", map.axisData[i].value " + map.axisData[i].value +
                        "drawAreaInformation[j].data.coordinate x: " + drawAreaInformation[j].data.coordinate.x + ", y: " + drawAreaInformation[j].data.coordinate.y);
                }
        }

        assessment.push(nearestStepCoordinate);
    }
    if (assessment.length > 0)
        assessment.push(assessment[0]); //to close the area

    //draw assesment area
    svgContainer.append("path")
        .attr("d", lineFunction(assessment))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "blue")
        .attr("opacity", 0.5)
        .attr("id", "bevalueung");

    drawSteps(svgContainer, drawAreaInformation, map.chartData, map.axisData);

}


function drawSteps(svgContainer, drawAreaInformation, chartData, axisData) {


    let axisLength = (chartData.chartHeight / 2) - 40;
    let centerX = chartData.chartWidth / 2;
    let centerY = chartData.chartHeight / 2;
    let offset = chartData.centerOffset;

    //axes drawing
    for (let i = 0; i < axisData.length; ++i) {
        let degree = 360.0 / axisData.length * i;

        let stepsAmount = (axisData[i].info.max - axisData[i].info.min) / axisData[i].info.step;
        let stepLenght = (axisLength - offset) / stepsAmount;

        for (let j = 0; j <= stepsAmount; j++) {
            let axeX = centerX + (stepLenght * j + offset) * Math.sin(toRadians(degree));
            let axeY = centerY + (stepLenght * j + offset) * Math.cos(toRadians(degree));

            svgContainer.append("circle")
                .attr("cx", axeX)
                .attr("cy", axeY)
                .attr("r", 5);
            let pointText;
            pointText = axisData[i].info.min + axisData[i].info.step * j;


            let smallestDifference = null;  
            for (jk = 0; jk < drawAreaInformation.length; jk++) {
                if (smallestDifference == null && drawAreaInformation[jk].axe == axisData[i].axe) {
                    smallestDifference = Math.abs(drawAreaInformation[jk].data.value - axisData[i].value);
                    nearestStepCoordinate = drawAreaInformation[jk].data.coordinate;
                }
                if (drawAreaInformation[jk].axe == axisData[i].axe)
                    if (Math.abs(drawAreaInformation[jk].data.value - axisData[i].value) < smallestDifference) {

                        smallestDifference = Math.abs(drawAreaInformation[jk].data.value - axisData[i].value);
                        nearestStepCoordinate = drawAreaInformation[jk].data.coordinate;
                        console.log("axisData[i].name: " + axisData[i].name + ", axisData[i].value " + axisData[i].value +
                            "drawAreaInformation[j].data.coordinate x: " + drawAreaInformation[jk].data.coordinate.x +
                             ", y: " + drawAreaInformation[jk].data.coordinate.y);
                    }
            }

            svgContainer.append("text")
                .text(pointText)
                .attr("y", axeY + 10)
                .attr("x", axeX + 10)
                .attr("font-size", 12)
                .attr("font-family", "sans-serif")
                .attr("fill", "black");
        }
    }
}


function initDrawAreaInformation(chartData, axisData) {

    let axisLength = (chartData.chartHeight / 2) - 40;
    let centerX = chartData.chartWidth / 2;
    let centerY = chartData.chartHeight / 2;
    let offset = chartData.centerOffset;

    let drawAreaInformation = [];

    for (let i = 0; i < axisData.length; ++i) {
        let degree = 360.0 / axisData.length * i;

        let stepsAmount = (axisData[i].info.max - axisData[i].info.min) / axisData[i].info.step;
        let stepLenght = (axisLength - offset) / stepsAmount;

        for (let j = 0; j <= stepsAmount; j++) {
            let axeX = centerX + (stepLenght * j + offset) * Math.sin(toRadians(degree));
            let axeY = centerY + (stepLenght * j + offset) * Math.cos(toRadians(degree));

            let stepValue = j * axisData[i].info.step + 1; //there is no 0 point for usability with Handys

            drawAreaInformation.push({ "axe": i, "data": { "value": stepValue, "coordinate": { "x": axeX, "y": axeY } } });
        }
    }
    return drawAreaInformation;


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
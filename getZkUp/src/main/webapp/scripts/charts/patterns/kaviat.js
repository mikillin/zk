let map = {
    "chartData": {
        "chartHeight": 460,
        "chartWidth": 860,
        "centerOffset": 30
    },
    "axisData": [
        { "axeId": 0, "name": "Die Antwort ist Ja", "value": 1, "displayValues": { "1": "Ja", "0": "Nein" }, "info": { "min": 0, "max": 1, "step": 1 } },
        { "axeId": 1, "name": "Wie war Ihre Schlafqualität?", "value": 2, "info": { "min": 1, "max": 6, "step": 1 } },
        { "axeId": 2, "name": "Wie lang haben Sie davon tatsächlich geschlafen?", "value": 3, "info": { "min": 1, "max": 12, "step": 2 } },
        { "axeId": 3, "name": "Wie lang waren Sie im Bett?", "value": 4, "info": { "min": 1, "max": 8, "step": 1 } },
        { "axeId": 4, "name": "Achse mit Auswertungen ab 4 bis 9", "value": 5, "info": { "min": 4, "max": 9, "step": 1 } }
    ]
}


function render(map) {

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

    let svgContainer = d3.select(".charts-kaviat").append("svg")
        .attr("width", map.chartData.chartWidth)
        .attr("height", map.chartData.chartHeight)
        .attr("background-color", "blue");


    svgContainer.on("click", function(event) {
        console.log(">> debug >> x= " + event.clientX + ", y= " + event.clientY);
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

        let textAnchor = null;
        let dx = 0;
        let dy = 0;
        if (degree >= 0 && degree < 180) {
            textAnchor = "begin";
            dx = 5;

        } else {
            textAnchor = "end";
            dx = -15;
        }

        if (degree >= 0 && degree < 90 || degree >= 180 && degree < 270) {
            dy = 15;

        } else {
            dy = -15;
        }


        let printedLength = 0;
        let textPart = "";
        let wordIndex = 0;
        let fullText =  (map.axisData[i].name + " (" + percent + "%)").trim();
        let words = fullText.split(' ');
        let rowIndex = 0;
        while (printedLength < fullText.length) {
            rowIndex++;
            while (textPart.length < 15 && (textPart.length + printedLength)< fullText.length) {
                textPart += words[wordIndex] + " ";
                wordIndex++;
            }

            if(dy<0)
                dy*=-1; 
            svgContainer
                .append("g")
                .append("text")
                .attr("x", axeX)
                .attr("y", axeY)
                .attr("dx", dx)
                .attr("dy", rowIndex * dy)
                .style("font-size", "12px")
                .text(textPart)
                .attr("text-anchor", textAnchor);
            printedLength +=  textPart.length;
            textPart = "";

        }

    

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
            if (smallestDifference == null && drawAreaInformation[j].axeId == map.axisData[i].axeId) {
                smallestDifference = Math.abs(drawAreaInformation[j].data.value - map.axisData[i].value);
                nearestStepCoordinate = drawAreaInformation[j].data.coordinate;
            }
            if (drawAreaInformation[j].axeId == map.axisData[i].axeId)
                if (Math.abs(drawAreaInformation[j].data.value - map.axisData[i].value) < smallestDifference) {

                    smallestDifference = Math.abs(drawAreaInformation[j].data.value - map.axisData[i].value);
                    nearestStepCoordinate = drawAreaInformation[j].data.coordinate;
                    // console.log("map.axisData[i].name: " + map.axisData[i].name + ", map.axisData[i].value " + map.axisData[i].value +
                    //     "drawAreaInformation[j].data.coordinate x: " + drawAreaInformation[j].data.coordinate.x + ", y: " + drawAreaInformation[j].data.coordinate.y);
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


            pointText = null;

            //change if there is exception for the defined value
            //todo: find a solution to approximate value... !!!!! like upper nearestStepCoordinate
            //it shouldn't be.. as this value is missed because of absence value on the axe
            // in this case, it should be displayed the error!!!!!!

            // !!!todo!!! show the error if exists


            //if there is exception for this value
            //there are several coordinates

            //(axeX, axeY)
            let exceptionName = null;
            for (jk = 0; jk < drawAreaInformation.length; jk++) {
                if (axeX == drawAreaInformation[jk].data.coordinate.x &&
                    axeY == drawAreaInformation[jk].data.coordinate.y) {
                    if (axisData[i].hasOwnProperty("displayValues") && axisData[i].displayValues.hasOwnProperty(drawAreaInformation[jk].data.value))
                        exceptionName = axisData[i].displayValues[drawAreaInformation[jk].data.value];
                }

            }

            if (exceptionName == null)
                pointText = axisData[i].info.min + axisData[i].info.step * j;
            else
                pointText = exceptionName;

            let dx = 0;
            let dy = 0;
            if (degree >= 0 && degree < 180) {
                textAnchor = "begin";
                dx = -5;

            } else {
                textAnchor = "end";
                dx = 5;
            }

            if (degree >= 0 && degree < 90) {
                dy = 5;

            } else {
                dy = -5;
            }

            if (degree >= 180 && degree < 270) {
                dy = 5;

                dx = -5;

            } else {
                dy = -5;
            }

            svgContainer.append("text")
                .text(pointText)
                .attr("y", axeY + 10)
                .attr("x", axeX + 10)
                .attr("dy", dx) //5 - dot's radius
                .attr("dx", dy)
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

            let stepValue = j * axisData[i].info.step ; //there is no 0 point for usability with Handys

            drawAreaInformation.push({ "axeId": i, "data": { "step": axisData[i].info.step, "value": stepValue+axisData[i].info.min , "coordinate": { "x": axeX, "y": axeY } } });
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

render(map);
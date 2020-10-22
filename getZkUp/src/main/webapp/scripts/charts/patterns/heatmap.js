//finctions with another data structure
function preProccessing(src, optimalValues) {
    let chartDates = [];
    let chartNames = [];

    for (i = 0; i < src.length; i++) {
        if (optimalValues.hasOwnProperty(src[i].activity)) { // if there is a goal
            if (!chartDates.includes(src[i].date)) // find all possible dates
                chartDates.push(src[i].date);
            if (!chartNames.includes(src[i].activity)) // find all possible activity names
                chartNames.push(src[i].activity);
        }
    }


    for (i = 0; i < chartNames.length; i++) {
        let presentedDates = [];
        let allDates = chartDates;
        //todo: refactor
        for (j = 0; j < src.length; j++) {
            if (chartNames.includes(src[j].activity)) { // the activity has a goal
                for (var k = 0; k < allDates.length; k++) {
                    if (allDates[k] == src[j].date && chartNames[i] == src[j].activity && !presentedDates.includes(src[j].date)) {
                        presentedDates.push(allDates[k]);
                    }
                }
            }
        }

        let missedDates = [];
        for (j = 0; j < allDates.length; j++) {
            if (!presentedDates.includes(allDates[j])) {
                missedDates.push(allDates[j]);
            }
        }


        //have all missed dates in tempDates
        // add all missed dates with -1 value
        let missedValuesObj = {};
        let tmpSrc = [];
        let inserted = false;

        let missedInserts = 0;
        // while (missedDates.length <= missedInserts) {


        for (var k = 0; k < missedDates.length; k++) {
            inserted = false;
            let missedValuesObj = {};
            missedValuesObj["activity"] = chartNames[i];
            missedValuesObj["date"] = missedDates[k];
            missedValuesObj["value"] = -1;

            tmpSrc = src;
            for (j = 0; j < tmpSrc.length && !inserted; j++) {
                if (new Date(tmpSrc[j].date) > new Date(missedDates[k]) && chartNames[i] == tmpSrc[j].activity) {
                    tmpSrc.splice(j, 0, missedValuesObj);
                    inserted = true;
                }
            }

            if (inserted === false) {
                tmpSrc.push(missedValuesObj);
            }
            src = tmpSrc;

        }

        //delete activities without goals
        let tmp = [];
        for (var k = 0; k < src.length; k++) {
            if (chartNames.includes(src[k].activity))
                tmp.push(src[k]);
        }
        src = tmp;
    }

    return src;
}


var colors = ["#FF7007", "#FFFB07", "#BBFF07", "#108901", "#f5f5f5"];

getColor = function (min, max, value) {
    // console.log("min: " + min + ", max: " + max + ", value: " + value);
    if (value == -1) // keine Auswertung
        return colors[4];
    else if (value <= max + (max - min) * 0.25 && value >= max - (max - min) * 0.25) {
        return colors[3];
    } else if (value <= max + (max - min) * 0.5 && value >= max - (max - min) * 0.5) {
        return colors[2];
    } else if (value <= max + (max - min) * 0.75 && value >= max - (max - min) * 0.75) {
        return colors[1];
    } else {
        return colors[0];
    }
}

function render() {
    render("")
}


function render(params) {

    // src = preProccessing(src, optimalValues); // add missed dates

    var heatMapElement = document.getElementsByClassName("charts-heatmap")[0];

    heatMapElement.style.width = "200px"; // name + margin + item-amount * 30px
    heatMapElement.style.height = "300px";
    heatMapElement.style.fontSize = "12px";
    heatMapElement.style.overflowX = "auto";
    heatMapElement.style.overflowY = "auto";
    heatMapElement.style.paddingTop = "15px";

    if (params.src.length > 0)
        heatMapElement.innerHTML = "";
    else {
        heatMapElement.innerHTML = "Keine Daten";
        return;
    }

    var toolTipDiv = document.getElementsByClassName("bottom-tooltip")[0];
    toolTipDiv.innerHTML = "";
    toolTipDiv.style.width = heatMapElement.style.width;
    toolTipDiv.style.height = "50px";

    // begin form "dates"
    let tmpRowDiv = document.createElement("div");
    tmpRowDiv.classList.add("heat-map-row");
    tmpRowDiv.classList.add("heat-map-row-common");

    let tmpDiv = document.createElement("div");
    tmpDiv.classList.add("category-name"); //empty space
    tmpRowDiv.appendChild(tmpDiv);

    if (params.src.length > 0)
        for (var j = 0; j < params.dates.length; j++) {
            let tmpDiv = document.createElement("div");
            tmpRowDiv.classList.add("heat-map-row");
            tmpRowDiv.classList.add("heat-map-row-common");

            tmpDiv.classList.add("heat-map-date");
            tmpDiv.style.width = "50px";
            tmpDiv.style.height = "45px";
            tmpDiv.style.fontSize = "10px";
            tmpDiv.style.cursor = "pointer";


            let tmpSpan = document.createElement("span");
            if (params.dates[j])
                tmpSpan.innerHTML = params.dates[j];

            tmpDiv.appendChild(tmpSpan);

            tmpDiv.addEventListener("click", function (event) {
                //  console.log("showKaviatChart date "+ event.target.innerText) ;
                showKaviatChart(event.target.innerText);
            }, false);


            tmpRowDiv.appendChild(tmpDiv);

        }
    heatMapElement.appendChild(tmpRowDiv);

    //** end form "dates" ** //

    let rowId = "";
    tmpRowDiv = document.createElement("div");

    for (var i = 0, d = 0; i < params.src.length; i++, d++) {
        //console.log("go d: " + d + ", params.dates.length: " + params.dates.length);
        // add empty fields
        if (d == params.dates.length) {
            // console.log("init d: " + d)
            d = 0;
        }

        //new row
        if (rowId != params.src[i].categoryId * 100 + params.src[i].questionId) { //new row

            //need to check if there should be empty divs for missed dates

            //add empty fields
            while (params.dates[d] != params.src[i].date && d < params.dates.length) {
                //console.log("-----------------------------------------------")
                //console.log(params.names[params.src[i].categoryId * 100 + params.src[i].questionId] + "add empty fields: d: " + d + ", params.dates[d]: " + params.dates[d] + ", params.src[i].date" + params.src[i].date);
                // add divs with no value
                // elements of row
                //console.log("params.dates[d]:  " + params.dates[d] + ", params.src[i].date:" + params.src[i].date)
                let tmpDiv = document.createElement("div");
                tmpDiv.classList.add("tooltip");

                let tmpSpan = document.createElement("span");
                tmpSpan.classList.add("tooltiptext");

                tmpSpan.innerHTML = "Keine Auswertung";
                tmpDiv.style.cursor = "pointer";
                tmpDiv.classList.add("heat-map-item");

                tmpDiv.style.backgroundColor = getColor(0, params.goals[params.src[i].categoryId * 100 + params.src[i].questionId],
                    -1);


                tmpDiv.dataset.value = "Keine Auswertung.";
                tmpDiv.dataset.name = params.names[params.src[i].categoryId * 100 + params.src[i].questionId];
                tmpDiv.dataset.id = params.src[i].id;
                tmpDiv.dataset.date = params.dates[d];
                if (params.goals.hasOwnProperty(params.src[i].categoryId * 100 + params.src[i].questionId))
                    tmpDiv.dataset.optimalValue = params.goals[params.src[i].categoryId * 100 + params.src[i].questionId];
                else
                    tmpDiv.dataset.optimalValue = -1;

                tmpDiv.appendChild(tmpSpan);
                tmpDiv.addEventListener("click", function (event) {
                    let bottomHint = "";
                    if (!event.target.dataset.category &&
                        !event.target.dataset.date &&
                        !event.target.dataset.value
                    ) {
                        // stub: clicked on the tooltip
                    } else {
                        bottomHint = event.target.dataset.name +
                            ". Datum:&nbsp;" + event.target.dataset.date +
                            ". Auswertung:&nbsp;Keine. ";
                        if (event.target.dataset.optimalValue != -1)
                            bottomHint += "Ziel:&nbsp;" + event.target.dataset.optimalValue + ".";

                    }
                    document.getElementsByClassName("bottom-tooltip")[0].innerHTML = bottomHint;
                }, false);


                tmpRowDiv.appendChild(tmpDiv);
                d++;
            }

            //end of adding empty divs


            d = 0; // dates from the beginnig
            rowId = params.src[i].categoryId * 100 + params.src[i].questionId;
            tmpRowDiv = document.createElement("div");
            tmpRowDiv.classList.add("heat-map-row");
            tmpRowDiv.classList.add("heat-map-row-common");

            tmpRowDiv.style.width = heatMapElement.style.width;

            let tmpDivText = document.createElement("div");
            tmpDivText.classList.add("category-name");
            tmpDivText.innerHTML = params.names[params.src[i].categoryId * 100 + params.src[i].questionId];
            tmpDivText.style.width = "100px";
            tmpRowDiv.appendChild(tmpDivText);
            //delete element
            tmpDivText = document.createElement("div");
            var img = document.createElement("IMG");
            img.src = "img/cancel.png";
            img.style.width = "20px";
            img.style.marignLeft = "10px";
            img.style.marignRight = "10px";
            img.style.cursor = "pointer";

            img.dataset.id = params.src[i].categoryId * 100 + params.src[i].questionId;


            img.addEventListener("click", function (event) {
                deleteActivity(event.target.dataset.id);
                document.getElementsByClassName("bottom-tooltip")[0].innerHTML = "";
            }, false);

            tmpDivText.appendChild(img);
            tmpRowDiv.appendChild(tmpDivText);
        }


        //add empty fields
        while (params.dates[d] != params.src[i].date && d < params.dates.length) {
            //console.log("-----------------------------------------------")
            //console.log(params.names[params.src[i].categoryId * 100 + params.src[i].questionId] + "add empty fields: d: " + d + ", params.dates[d]: " + params.dates[d] + ", params.src[i].date" + params.src[i].date);
            // add divs with no value
            // elements of row
            //console.log("params.dates[d]:  " + params.dates[d] + ", params.src[i].date:" + params.src[i].date)
            let tmpDiv = document.createElement("div");
            tmpDiv.classList.add("tooltip");

            let tmpSpan = document.createElement("span");
            tmpSpan.classList.add("tooltiptext");

            tmpSpan.innerHTML = "Keine Auswertung";
            tmpDiv.style.cursor = "pointer";
            tmpDiv.classList.add("heat-map-item");

            tmpDiv.style.backgroundColor = getColor(0, params.goals[params.src[i].categoryId * 100 + params.src[i].questionId],
                -1);


            tmpDiv.dataset.value = "Keine Auswertung.";
            tmpDiv.dataset.name = params.names[params.src[i].categoryId * 100 + params.src[i].questionId];
            tmpDiv.dataset.id = params.src[i].id;
            tmpDiv.dataset.date = params.dates[d];
            if (params.goals.hasOwnProperty(params.src[i].categoryId * 100 + params.src[i].questionId))
                tmpDiv.dataset.optimalValue = params.goals[params.src[i].categoryId * 100 + params.src[i].questionId];
            else
                tmpDiv.dataset.optimalValue = -1;

            tmpDiv.appendChild(tmpSpan);
            tmpDiv.addEventListener("click", function (event) {
                let bottomHint = "";
                if (!event.target.dataset.category &&
                    !event.target.dataset.date &&
                    !event.target.dataset.value
                ) {
                    // stub: clicked on the tooltip
                } else {
                    bottomHint = event.target.dataset.name +
                        ". Datum:&nbsp;" + event.target.dataset.date +
                        ". Auswertung:&nbsp;Keine. ";
                    if (event.target.dataset.optimalValue != -1)
                        bottomHint += "Ziel:&nbsp;" + event.target.dataset.optimalValue + ".";

                }
                document.getElementsByClassName("bottom-tooltip")[0].innerHTML = bottomHint;
            }, false);


            tmpRowDiv.appendChild(tmpDiv);
            d++;
        }


        //elements of row
        tmpDiv = document.createElement("div");
        tmpDiv.classList.add("tooltip");

        let tmpSpan = document.createElement("span");
        tmpSpan.classList.add("tooltiptext");

        let str = "Wert:" + params.src[i].value;
        if (params.goals.hasOwnProperty(params.src[i].categoryId * 100 + params.src[i].questionId))
            str += " von " + params.goals[params.src[i].categoryId * 100 + params.src[i].questionId];

        tmpSpan.innerHTML = str;
        tmpDiv.classList.add("heat-map-item");

        tmpDiv.style.backgroundColor = getColor(0, params.goals[params.src[i].categoryId * 100 + params.src[i].questionId],
            (! params.src[i].value ||  params.src[i].value == "null" ? -1 :  params.src[i].value) );
        tmpDiv.style.cursor = "pointer";
        tmpDiv.dataset.value = params.src[i].value;
        tmpDiv.dataset.name = params.names[params.src[i].categoryId * 100 + params.src[i].questionId];
        tmpDiv.dataset.id = params.src[i].id;
        tmpDiv.dataset.date = params.src[i].date;
        if (params.goals.hasOwnProperty(params.src[i].categoryId * 100 + params.src[i].questionId))
            tmpDiv.dataset.optimalValue = params.goals[params.src[i].categoryId * 100 + params.src[i].questionId];
        else
            tmpDiv.dataset.optimalValue = -1;

        tmpDiv.appendChild(tmpSpan);
        tmpDiv.addEventListener("click", function (event) {
            let bottomHint = "";
            if (!event.target.dataset.category &&
                !event.target.dataset.date &&
                !event.target.dataset.value
            ) {
                //stub: clicked on the tooltip
            } else {
                bottomHint = event.target.dataset.name +
                    ". Datum:&nbsp;" + event.target.dataset.date +
                    ". Auswertung:&nbsp;" + (!event.target.dataset.value || event.target.dataset.value == "null" ? "Keine." : event.target.dataset.value) + ". ";
                if (event.target.dataset.optimalValue != -1)
                    bottomHint += "Ziel:&nbsp;" + event.target.dataset.optimalValue + ".";

            }
            document.getElementsByClassName("bottom-tooltip")[0].innerHTML = bottomHint;
        }, false);

        tmpRowDiv.appendChild(tmpDiv);
        //the last positions may be empty. add empty divs
        //if ()

        //add empty fields. in the end. i +1 , d +1 because of the for loop
        //send everything in the java class.
        if (i + 1 == params.src.length)
            while (d + 1 < params.dates.length) {
                // console.log("-----------------------------------------------")
                // console.log(params.names[params.src[i].categoryId * 100 + params.src[i].questionId] + "add empty fields: d: " + d + ", params.dates[d]: " + params.dates[d] + ", params.src[i].date" + params.src[i].date);
                // add divs with no value
                // elements of row
                //("params.dates[d]:  " + params.dates[d] + ", params.src[i].date:" + params.src[i].date)
                let tmpDiv = document.createElement("div");
                tmpDiv.classList.add("tooltip");

                let tmpSpan = document.createElement("span");
                tmpSpan.classList.add("tooltiptext");

                tmpSpan.innerHTML = "Keine Auswertung";
                tmpDiv.style.cursor = "pointer";
                tmpDiv.classList.add("heat-map-item");

                tmpDiv.style.backgroundColor = getColor(0, params.goals[params.src[i].categoryId * 100 + params.src[i].questionId],
                    -1);
                tmpDiv.dataset.value = "Keine Auswertung.";
                tmpDiv.dataset.name = params.names[params.src[i].categoryId * 100 + params.src[i].questionId];
                tmpDiv.dataset.id = params.src[i].id;
                tmpDiv.dataset.date = params.dates[d];
                if (params.goals.hasOwnProperty(params.src[i].categoryId * 100 + params.src[i].questionId))
                    tmpDiv.dataset.optimalValue = params.goals[params.src[i].categoryId * 100 + params.src[i].questionId];
                else
                    tmpDiv.dataset.optimalValue = -1;

                tmpDiv.appendChild(tmpSpan);
                tmpDiv.addEventListener("click", function (event) {
                    let bottomHint = "";
                    if (!event.target.dataset.category &&
                        !event.target.dataset.date &&
                        !event.target.dataset.value
                    ) {
                        // stub: clicked on the tooltip
                    } else {
                        bottomHint = event.target.dataset.name +
                            ". Datum:&nbsp;" + event.target.dataset.date +
                            ". Auswertung:&nbsp;Keine. ";
                        if (event.target.dataset.optimalValue != -1)
                            bottomHint += "Ziel:&nbsp;" + event.target.dataset.optimalValue + ".";

                    }
                    document.getElementsByClassName("bottom-tooltip")[0].innerHTML = bottomHint;
                }, false);
                tmpRowDiv.appendChild(tmpDiv);
                d++;
            }
        heatMapElement.appendChild(tmpRowDiv);
    }


}

//todo. add empty field make in a function
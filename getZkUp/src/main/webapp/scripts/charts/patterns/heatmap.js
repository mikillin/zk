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

getJSON = function () {
    return JSON.parse('[  {    "category": "Laufen",    "data": [     {        "id": 1,        "data": {          "date": "01.08.2020",          "value": "1"        }      },      {        "id": 2,        "data": {          "date": "11.08.2019",          "value": "4"        }      },      {        "id": 3,        "data": {          "date": "21.08.2020",          "value": "7"        }      },      {        "id": 4,        "data": {          "date": "31.08.2020",          "value": "5"        }      }    ]  },  {    "category": "Schlafen",    "data": [      {        "id": 1,        "data": {          "date": "03.08.2020",          "value": "7"        }      },      {        "id": 2,        "data": {          "date": "13.08.2020",          "value": "9"        }      },      {        "id": 3,        "data": {          "date": "22.08.2020",          "value": "8"        }      },      {        "id": 4,        "data": {          "date": "31.08.2020",          "value": "12"        }      }    ]  } ,  {    "category": "Essen/Trinken",    "data": [      {        "id": 1,        "data": {          "date": "03.08.2020",          "value": "3"        }      },      {        "id": 2,        "data": {          "date": "13.08.2020",          "value": "2"        }      },      {        "id": 3,        "data": {          "date": "22.08.2020",          "value": "3"        }      },      {        "id": 4,        "data": {          "date": "31.08.2020",          "value": "4"        }      }    ]  }]')
}

var colors = ["#FF7007", "#FFFB07", "#BBFF07", "#108901", "#f5f5f5"];

getColor = function (min, max, value) {
    console.log("min: " + min + ", max: " + max + ", value: " + value);
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


var optimalValues = {};
var src = {};

function render(params) {

    // console.log(">>>" + params);
    if (!params || !params.hasOwnProperty(optimalValues))
        optimalValues = [{
            "category": "Essen/Trinken",
            "question": "Wieviele Mahlzeiten haben Sie zu sich genommen?",
            "value": 3
        }, {
            "category": "Wietere positive Aktivitäten",
            "question": "Wie haben Sie sich dabei gefühlt?",
            "value": 11
        }, {
            "category": "Schlaf",
            "question": "Wielange waren Sie im Bett (in Stunden)?",
            "value": 8
        }, {
            "category": "Entschpanungs- oder Atmenübungen",
            "question": "Wie haben Sie sich dabei gefühlt?",
            "value": 10
        }];

    if (!params || !params.hasOwnProperty("src"))
        src = getJSON();
    else
        src = params.src;
    let overallLength;
    if (!params || !params.hasOwnProperty("overallLength"))
        overallLength = (100 + src[0].data.length * 50 + 100);
    else
        overallLength = params.overallLength;

    src = preProccessing(src, optimalValues); // add missed dates
//init length of rows
    var rowLength = (100 + src[0].data.length * 50) + "px"

//todo init as column
    var heatMapElement = document.getElementsByClassName("charts-heatmap")[0];
    heatMapElement.innerHTML = "";

    heatMapElement.style.width = overallLength > 350 ? "350px" : overallLength + "px"; // name + margin + item-amount * 30px

    let overallHeight = (src.length * 50 + 80 + 20); // rows * 30 + dates +  margin
    heatMapElement.style.height = overallHeight > 250 ? "250px" : overallHeight + "px";

    heatMapElement.style.overflowX = "auto";
    heatMapElement.style.paddingTop = "15px";

    var toolTipDiv = document.getElementsByClassName("bottom-tooltip")[0];

    toolTipDiv.style.width = heatMapElement.style.width;
    toolTipDiv.style.height = "50px";

    for (var i = 0; i < src.length; i++) {
        //      console.log(src[i].category);
        let tmpRowDiv = document.createElement("div");
        tmpRowDiv.classList.add("heat-map-row");
        tmpRowDiv.classList.add("heat-map-row-common");

        tmpRowDiv.style.width = rowLength;

        let tmpDivText = document.createElement("div");
        tmpDivText.classList.add("category-name");
        tmpDivText.innerHTML = src[i].category + ": " + src[i].question;
        tmpDivText.style.width = "100px";
        tmpRowDiv.appendChild(tmpDivText);
        //delete element
        tmpDivText = document.createElement("div");
        //tmpDivText.classList.add("delete");
        var img = document.createElement("IMG");
        img.src = "img/cancel.png";
        img.style.width = "20px";
        img.style.marignLeft = "10px";
        img.style.marignRight = "10px";

        img.dataset.category = src[i].category;


        img.addEventListener("click", function (event) {
            //console.log("img clicked")
            //todo:::
            // add request to delete the item
            //  console.log(event.target.dataset.category); // need id and redraw
            //invoke function zscript from there a command

        }, false);

        tmpDivText.appendChild(img);


        tmpRowDiv.appendChild(tmpDivText);


        for (var j = 0; j < src[i].data.length; j++) {

            let optimalValue = -1;
            for (var o = 0; o < optimalValues.length; o++) {
                if (optimalValues[o].category == src[i].category && optimalValues[o].question == src[i].question)
                    optimalValue = optimalValues[o].value;
            }

            let tmpDiv = document.createElement("div");
            tmpDiv.classList.add("tooltip");

            let tmpSpan = document.createElement("span");
            tmpSpan.classList.add("tooltiptext");
            tmpSpan.innerHTML = "Wert:" + src[i].data[j].item.value + " von " + optimalValue;

            tmpDiv.classList.add("heat-map-item");

            tmpDiv.style.backgroundColor = getColor(0, optimalValue, src[i].data[j].item.value);
            tmpDiv.dataset.value = src[i].data[j].item.value;
            tmpDiv.dataset.category = src[i].category;
            tmpDiv.dataset.question = src[i].question;
            tmpDiv.dataset.id = src[i].data[j].id;
            tmpDiv.dataset.date = src[i].data[j].item.date;
            tmpDiv.dataset.optimalValue = optimalValue;

            tmpDiv.appendChild(tmpSpan);

            tmpDiv.addEventListener("click", function (event) {

                let bottomHint = "";
                if (!event.target.dataset.category
                    && !event.target.dataset.date
                    && !event.target.dataset.value
                ) {
                    //stub: clicked on the tooltip
                } else {
                    bottomHint = "Aktivität:&nbsp;" + event.target.dataset.category +
                        ". Frage:&nbsp;" + event.target.dataset.question +
                        ". Datum:&nbsp;" + event.target.dataset.date +
                        ". Auswertung:&nbsp;" + event.target.dataset.value +
                        ". Ziel:&nbsp;" + event.target.dataset.optimalValue + ".";

                }
                document.getElementsByClassName("bottom-tooltip")[0].innerHTML = bottomHint;
            }, false);
            tmpRowDiv.appendChild(tmpDiv);
        }
        heatMapElement.appendChild(tmpRowDiv);
    }

    /*
        let tmpRowDiv = document.createElement("div");
        tmpRowDiv.classList.add("heat-map-row");
        tmpRowDiv.classList.add("heat-map-row-common");

        let tmpDiv = document.createElement("div");
        tmpDiv.classList.add("category-name"); //empty space
        tmpRowDiv.appendChild(tmpDiv);

        if (src.length > 0)
            for (var j = 0; j < src[0].data.length; j++) {
                let tmpDiv = document.createElement("div");
                tmpRowDiv.classList.add("heat-map-row");
                tmpRowDiv.classList.add("heat-map-row-common");

                tmpDiv.classList.add("heat-map-date");
                tmpDiv.style.width = "50px";
                tmpDiv.style.height = "45px";
                tmpDiv.style.fontSize = "10px";


                let tmpSpan = document.createElement("span");
                if (src[0].data[j].item.date)
                    tmpSpan.innerHTML = src[0].data[j].item.date;

                tmpDiv.appendChild(tmpSpan);
                tmpRowDiv.appendChild(tmpDiv);

            }
    heatMapElement.appendChild(tmpRowDiv);*/
}

var optimalValues = {
    "Schlafen": 100,
    "Essen/Trinken": 50,
    "Laufen": 70
}

var colors = ["#FF7007", "#FFFB07", "#BBFF07", "#108901"]


var margin = { top: 80, right: 25, bottom: 130, left: 140 },
    width = 650 - margin.left - margin.right,
    height = 350 - margin.top - margin.bottom;

// append the svg object to the body of the page
var svg = d3.select(".chart-class-type-6")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform",
        "translate(" + margin.left + "," + margin.top + ")");

//Read the data

var src = [{ "activity": "Schlafen", "date": "02.02.2020", "value": 0 }, { "activity": "Schlafen", "date": "02.03.2020", "value": 10 }, { "activity": "Schlafen", "date": "02.04.2020", "value": 20 }, { "activity": "Schlafen", "date": "02.05.2020", "value": 30 }, { "activity": "Schlafen", "date": "02.06.2020", "value": 40 }, { "activity": "Schlafen", "date": "02.07.2020", "value": 50 }, { "activity": "Schlafen", "date": "02.08.2020", "value": 60 }, { "activity": "Schlafen", "date": "02.09.2020", "value": 70 }, { "activity": "Schlafen", "date": "02.10.2020", "value": 80 }, { "activity": "Schlafen", "date": "02.11.2020", "value": 90 }, { "activity": "Essen/Trinken", "date": "02.02.2020", "value": 37 }, { "activity": "Essen/Trinken", "date": "02.03.2020", "value": 50 }, { "activity": "Essen/Trinken", "date": "02.04.2020", "value": 81 }, { "activity": "Essen/Trinken", "date": "02.05.2020", "value": 79 }, { "activity": "Essen/Trinken", "date": "02.06.2020", "value": 84 }, { "activity": "Essen/Trinken", "date": "02.07.2020", "value": 91 }, { "activity": "Essen/Trinken", "date": "02.08.2020", "value": 82 }, { "activity": "Essen/Trinken", "date": "02.09.2020", "value": 89 }, { "activity": "Essen/Trinken", "date": "02.10.2020", "value": 6 }, { "activity": "Essen/Trinken", "date": "02.11.2020", "value": 67 }, { "activity": "Laufen", "date": "02.02.2020", "value": 96 }, { "activity": "Laufen", "date": "02.03.2020", "value": 13 }, { "activity": "Laufen", "date": "02.04.2020", "value": 98 }, { "activity": "Laufen", "date": "02.05.2020", "value": 10 }, { "activity": "Laufen", "date": "02.06.2020", "value": 86 }, { "activity": "Laufen", "date": "02.07.2020", "value": 23 }, { "activity": "Laufen", "date": "02.08.2020", "value": 74 }, { "activity": "Laufen", "date": "02.09.2020", "value": 47 }, { "activity": "Laufen", "date": "02.10.2020", "value": 73 }, { "activity": "Laufen", "date": "02.11.2020", "value": 40 }];

var data = src;


var datesDomain = d3.map(data, function(d) { return d.date; }).keys()
var activitiesDomain = d3.map(data, function(d) { return d.activity; }).keys()




// Build X scales and axis:
var x = d3.scaleBand()
    .range([0, width])
    .domain(datesDomain)
    .padding(0.05);
svg.append("g")
    .style("font-size", 15)
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x).tickSize(0))
    .call(g => g.select(".domain").remove())

    .selectAll("text")
    .style("text-anchor", "end")
    .attr("dx", "-.8em")
    .attr("dy", ".15em")
    .attr("transform", "rotate(-90)")
    .attr("font-size", "12px")




// Build Y scales and axis:
var y = d3.scaleBand()
    .range([height, 0])
    .domain(activitiesDomain)
    .padding(0.05);
svg.append("g")
    .style("font-size", 15)
    .call(d3.axisLeft(y).tickSize(0))
    .select(".domain").remove()



// create a tooltip
var tooltip = d3.select(".chart-class-type-6")
    .append("div")
    .style("opacity", 0)
    .attr("class", "tooltip")
    .style("border-width", "2px")
    .style("border-radius", "5px")
    .style("padding", "5px")
    .style("width", "500px")

    .style("margin-left", "140px")


// Three function that change the tooltip when user hover / move / leave a cell
var mouseover = function(d) {
    tooltip
        .style("opacity", 1)
    d3.select(this)
        .style("opacity", 1)
}
var mousemove = function(d) {
    tooltip
        .html("Aktivitaet: <span class ='bold'>" + d.activity + "</span>. Datum: <span class ='bold'>" + d.date + "</span>.<br/>Auswertung: <span class ='bold'>" + d.value + "</span>. Das Zeil: <span class ='bold'>" + optimalValues[d.activity] + "</span>")
        .style("left", (d3.mouse(this)[0] + 70) + "px")
        .style("top", (d3.mouse(this)[1]) + "px")
}
var mouseleave = function(d) {
    tooltip
        .style("opacity", 0)
    d3.select(this)
        .style("stroke", "none")
        .style("opacity", 0.8)
}

// add the squares
svg.selectAll()
    .data(data, function(d) { return d.activity + ':' + d.date; })
    .enter()
    .append("rect")
    .attr("x", function(d) { return x(d.date) })
    .attr("y", function(d) { return y(d.activity) })
    .attr("rx", 4)
    .attr("ry", 4)
    .attr("width", x.bandwidth())
    .attr("height", y.bandwidth())
    .style("fill", function(d) {


        let optimalValue = optimalValues[d.activity];
        let part = Math.abs(optimalValue - d.value);


        let color = "";
        if (part < 0.25 * optimalValue)
            color = colors[3];
        else if (part < 0.5 * optimalValue)
            color = colors[2];
        else if (part < 0.75 * optimalValue)
            color = colors[1];
        else
            color = colors[0];
        if (d.activity == "Schlafen") {
            console.log("optimalValue: " + optimalValue + ", current value: " + d.value + ", part:" + part + ", color: " + color)
        }
        return color;

    })
    .style("stroke-width", 4)
    .style("stroke", "none")
    .style("opacity", 0.8)
    .on("mouseover", mouseover)
    .on("mousemove", mousemove)
    .on("mouseleave", mouseleave)

// Add title to graph
svg.append("text")
    .attr("x", 0)
    .attr("y", -50)
    .attr("text-anchor", "left")
    .style("font-size", "22px")
    .text("Hot Map für Aktivitäten");


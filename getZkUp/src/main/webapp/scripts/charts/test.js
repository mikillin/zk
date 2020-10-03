var margin = {top: 80, right: 25, bottom: 30, left: 40},
    width = 550 - margin.left - margin.right,
    height = 550 - margin.top - margin.bottom;

// append the svg object to the body of the page
var svg = d3.select(".chart-class-type-6")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform",
        "translate(" + margin.left + "," + margin.top + ")");

//Read the data

var data= [{"activity":"Schlafen","date":"02.02.2020","value":30},{"activity":"Schlafen","date":"02.03.2020","value":95},{"activity":"Schlafen","date":"02.04.2020","value":22},{"activity":"Schlafen","date":"02.05.2020","value":14},{"activity":"Schlafen","date":"02.06.2020","value":59},{"activity":"Schlafen","date":"02.07.2020","value":52},{"activity":"Schlafen","date":"02.08.2020","value":88},{"activity":"Schlafen","date":"02.09.2020","value":20},{"activity":"Schlafen","date":"02.10.2020","value":99},{"activity":"Schlafen","date":"02.02.20200","value":66},{"activity":"Essen/Trinken","date":"02.02.2020","value":37},{"activity":"Essen/Trinken","date":"02.03.2020","value":50},{"activity":"Essen/Trinken","date":"02.04.2020","value":81},{"activity":"Essen/Trinken","date":"02.05.2020","value":79},{"activity":"Essen/Trinken","date":"02.06.2020","value":84},{"activity":"Essen/Trinken","date":"02.07.2020","value":91},{"activity":"Essen/Trinken","date":"02.08.2020","value":82},{"activity":"Essen/Trinken","date":"02.09.2020","value":89},{"activity":"Essen/Trinken","date":"02.10.2020","value":6},{"activity":"Essen/Trinken","date":"02.02.20200","value":67},{"activity":"Laufen","date":"02.02.2020","value":96},{"activity":"Laufen","date":"02.03.2020","value":13},{"activity":"Laufen","date":"02.04.2020","value":98},{"activity":"Laufen","date":"02.05.2020","value":10},{"activity":"Laufen","date":"02.06.2020","value":86},{"activity":"Laufen","date":"02.07.2020","value":23},{"activity":"Laufen","date":"02.08.2020","value":74},{"activity":"Laufen","date":"02.09.2020","value":47},{"activity":"Laufen","date":"02.10.2020","value":73},{"activity":"Laufen","date":"02.02.20200","value":40}];  // Labels of row and columns -> unique identifier of the column called 'group' and 'variable'
var myGroups = d3.map(data, function(d){return d.date;}).keys()
var myVars = d3.map(data, function(d){return d.activity;}).keys()

// Build X scales and axis:
var x = d3.scaleBand()
    .range([ 0, width ])
    .domain(myGroups)
    .padding(0.05);
svg.append("g")
    .style("font-size", 15)
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x).tickSize(0))
    .select(".domain").remove()

// Build Y scales and axis:
var y = d3.scaleBand()
    .range([ height, 0 ])
    .domain(myVars)
    .padding(0.05);
svg.append("g")
    .style("font-size", 15)
    .call(d3.axisLeft(y).tickSize(0))
    .select(".domain").remove()

// Build color scale
var myColor = d3.scaleSequential()
    .interpolator(d3.interpolateInferno)
    .domain([1,100])

// create a tooltip
var tooltip = d3.select(".chart-class-type-6")
    .append("div")
    .style("opacity", 0)
    .attr("class", "tooltip")
    .style("background-color", "white")
    .style("border", "solid")
    .style("border-width", "2px")
    .style("border-radius", "5px")
    .style("padding", "5px")

// Three function that change the tooltip when user hover / move / leave a cell
var mouseover = function(d) {
    tooltip
        .style("opacity", 1)
    d3.select(this)
        .style("stroke", "black")
        .style("opacity", 1)
}
var mousemove = function(d) {
    tooltip
        .html("The exact value of<br>this cell is: " + d.value)
        .style("left", (d3.mouse(this)[0]+70) + "px")
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
    .data(data, function(d) {return d.activity+':'+d.date;})
    .enter()
    .append("rect")
    .attr("x", function(d) { return x(d.activity) })
    .attr("y", function(d) { return y(d.date) })
    .attr("rx", 4)
    .attr("ry", 4)
    .attr("width", x.bandwidth() )
    .attr("height", y.bandwidth() )
    .style("fill", function(d) { return myColor(d.value)} )
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
    .text("A d3.js heatmap");

// Add subtitle to graph
svg.append("text")
    .attr("x", 0)
    .attr("y", -20)
    .attr("text-anchor", "left")
    .style("font-size", "14px")
    .style("fill", "grey")
    .style("max-width", 400)
    .text("A short description of the take-away message of this chart.");


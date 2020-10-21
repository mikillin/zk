/*-----*/
/*data example*/
let dataFromClass = [
    ['Year', 'Sales', 'Expenses', 'Add'],
    ['2003', 1000, 400, 500],
    ['2005', 1170, 460, 500],
    ['2006', 660, 1120, 500],
    ['2007', 1030, 540, 500]
];

let optionFromClass = {
    title: 'Company Performance',
    curveType: 'function',
    legend: { position: 'bottom' }
};

let element = "document.getElementsByClassName('chart13')[0]";
let map = { "data": dataFromClass,
    "option" : optionFromClass,
    "element" : element }
/*-----*/

function drawChart(map) {
    console.log(">>" + map);
    var data = google.visualization.arrayToDataTable(map.data);

    var options = map.option;

    var chart = new google.visualization.LineChart(eval(map.element));

    chart.draw(data, options);
}

google.charts.load('current', { 'packages': ['corechart'] });
// google.charts.setOnLoadCallback(function(){ drawChart(map) })


// add here divs..
drawChart({"option":{"title":"Weitere positive Aktivitäten: Wie haben Sie sich dabei gefühlt?","curveType":"function","legend":{"position":"bottom"},"width":400,"height":300},"element":"document.getElementsByClassName('chart13')[0]","data":[["Date","Ziel","Durschnitt","Auswertung"],["20-07-2020",10,11,10],["20-07-2020",10,11,15],["02-09-2020",10,11,15],["14-09-2020",10,11,4]]});
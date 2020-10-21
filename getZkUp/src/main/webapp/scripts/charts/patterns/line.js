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

    // window.addEventListener('load', function () {

        var data = google.visualization.arrayToDataTable(map.data);

        var options = map.option;

        var chart = new google.visualization.LineChart(eval(map.element));

        chart.draw(data, options);
    // })
}

google.charts.load('current', { 'packages': ['corechart'] });
// google.charts.setOnLoadCallback(function(){ drawChart(map) })
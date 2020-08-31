google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = new google.visualization.DataTable(); data.addColumn('string', 'Date');
    data.addColumn('number', 'Value');data.addRows([['01.01.2019', "1"],['01.02.2019', "2"],['01.03.2020', "3"],['01.04.2020', "2"],['01.05.2020', "3"],['01.06.2020', "5"],['07.06.2020', "5"],]);var options = {
        title: 'Company Performance',
        hAxis: {title: 'Year', titleTextStyle: {color: '#333'}},
        vAxis: {minValue: 0},
        width:400,
        height:300
    };
    document.getElementsByTagName('body')[0].children[0].children[0].appendChild(document.createElement("div"))

    var chart = new google.visualization.AreaChart(document.getElementsByTagName('body')[0].children[0].children[0].children[0]);
    chart.draw(data, options);
}
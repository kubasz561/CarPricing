import React, {Component} from 'react';
import Plot from 'react-plotly.js';

export default class Results extends Component {

    render() {
        return (
            <div id="results">
                {
                this.props.response &&  this.props.response.message &&
                <div>
                    <h2> {this.props.response.message}  </h2>
                </div>
                }
                {
                this.props.response &&  !this.props.response.message &&
                <div>
                    <h2>Proponowana Cena: {this.props.response.formPrice} zł </h2>
                    <h4>Średnie odchylenie od ceny: {this.props.response.averageDiff} zł </h4>
                    <h4>Mediana odchylenia od ceny: {this.props.response.median} zł </h4>
                    <h4>Obliczone na podstawie {this.props.response.lpResultDTO.filteredAdvertsCount} ogłoszeń </h4>
                    <h4>Parametry wzięte pod uwagę: ilość: {this.props.response.filtersInfo.split(",").length-1}, {this.props.response.filtersInfo.split(",").map(parameter => <h5>{parameter}</h5> )} </h4>
                    <h4>Wartości
                        współczynników: {this.props.response.lpResultDTO.wParams.map(w => " " + w + ", ")} solution: {this.props.response.lpResultDTO.totalDiff} </h4>
                </div>
                }

                <br/>
                {
                    this.props.response && this.props.response.charts && this.props.response.charts.map(chart =>
                        chart.formY && <p>Parametr: {chart.type} / Wartosc: {chart.formX} / Cena: {chart.formY} </p>
                    )
                }
                {
                    this.props.response && this.props.response.charts && this.props.response.charts.map(chart =>
                        <div>
                            <div>
                                <Plot
                                    data={[
                                        {
                                            x: chart.advertX,
                                            y: chart.advertY,
                                            type: 'scatter',
                                            mode: chart.mainChartMode,
                                            marker: {color: 'red', size: 12}
                                        },
                                        {
                                            x: chart.regressX,
                                            y: chart.regressY,
                                            type: 'scatter',
                                            mode: chart.approxChartMode,
                                            marker: {color: 'blue'}
                                        },
                                        {
                                            x: [chart.formX],
                                            y: [chart.formY],
                                            type: 'scatter',
                                            mode: chart.mainChartMode,
                                            marker: {color: 'green', size: 15}
                                        }
                                    ]}
                                    layout={{width: 800, height: 600, title: chart.type}}
                                />
                            </div>
                            <div>
                                {chart.r && <span>R: {chart.r} </span>}
                                {chart.r && <br/>}
                                {chart.formY &&
                                <h5>Parametr: {chart.type} / Wartosc: {chart.formX} / Cena: {chart.formY} </h5>}
                            </div>
                        </div>
                    )
                }

            </div>
        );
    }

}
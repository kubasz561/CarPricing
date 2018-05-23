import React, {Component} from 'react';
import Plot from 'react-plotly.js';

export default class Results extends Component {
    constructor(props){
        super(props)
    }


    render() {
        return (
            <div id="results">
                {
                this.props.response &&
                <div>
                    <h2>Proponowana Cena: {this.props.response.formPrice} zł </h2>
                    <h4>Srednie odchylenie od ceny: {this.props.response.averageDiff} zł </h4>
                    <h4>Mediana odchylenia od ceny: {this.props.response.median} zł </h4>
                    <h4>Obliczone na podstawie {this.props.response.lpResultDTO.filteredAdvertsCount} ogłoszeń </h4>
                    <h4>Parametry wzięte pod uwagę: {this.props.response.filtersInfo} </h4>
                    <h4>Wartośći
                        współczynników: {this.props.response.lpResultDTO.wParams.map(w => " " + w + ", ")} solution: {this.props.response.lpResultDTO.totalDiff} </h4>
                </div>
                }

                <br/>
                {
                    this.props.response && this.props.response.charts.map(chart =>
                        chart.formY && <p>Parametr: {chart.type} / Wartosc: {chart.formX} / Cena: {chart.formY} </p>
                    )
                }
                {
                    this.props.response && this.props.response.charts.map(chart =>
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
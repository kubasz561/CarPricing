import React, {Component} from 'react';

/**
 * Komponent wyświetlający tabelę współczynników
 */
export default class ParamsTable extends Component {

    render() {
        return (
            <table >
            <tbody >
                <tr>
                    <th>Parametr</th>
                    <th>Współczynnik</th>
                </tr>
                {this.props.filters.split(",").map((parameter,index) =>
                    <tr key={index}>
                        <td>{parameter.split(";")[0]}</td>
                        <td>{parameter.split(";")[1]}</td>
                    </tr>
                )}

            </tbody>
            </table>
        )
    }
}
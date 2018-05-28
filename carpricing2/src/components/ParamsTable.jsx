import React, {Component} from 'react';

export default class ParamsTable extends Component {

    render() {
        return (
            <table >
                <tr>
                    <th>Parametr</th>
                    <th>Współczynnik</th>
                </tr>
                {this.props.filters.split(",").map(parameter =>
                    <tr>
                        <td>{parameter.split(";")[0]}</td>
                        <td>{parameter.split(";")[1]}</td>
                    </tr>
                )}
            </table>
        )
    }
}
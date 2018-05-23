import React, { Component } from 'react';
import logo from './logo.svg';
import $ from 'jquery';
import './App.css';
import SelectYear from './components/SelectYear';
import Results from './components/Results';
import Plot from 'react-plotly.js';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            marka: "Volkswagen",
            model: "Golf",
            year: 2005,
            fuel: "Benzyna",
            mileage: 150000,
            engineCapacity: 2000,
            power: 200,
            color: "Czarny",
            type: "Sedan",
            isNew: false,
            hadAccident: true,
            isFirstOwner: false,
            description: "...opis"
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleCheckboxChange = this.handleCheckboxChange.bind(this);
        this.submit= this.submit.bind(this);
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo" />
                    <h1 className="App-title">Car Pricing</h1>
                </header>

                <br />
                <form onSubmit={this.submit} name="theForm">
                    <label>
                        MARKA:
                        <input
                            name="marka"
                            type="text"
                            value={this.state.marka}
                            onChange={this.handleInputChange} />
                    </label>
                    <br />
                    <label>
                        MODEL:
                        <input
                            name="model"
                            type="text"
                            value={this.state.model}
                            onChange={this.handleInputChange} />
                    </label>
                    <br />
                    <label>
                        ROK:
                    </label>
                    {/*<SelectYear
                        name="year"
                        value={this.state.year}
                        onChange={this.handleInputChange}
                        />*/}
                    <select name="year" value={this.state.year} onChange={this.handleInputChange}>
                        <option value="2007">2018</option>
                        <option value="2007">2007</option>
                        <option value="2006">2006</option>
                        <option value="2005">2005</option>
                        <option value="2004">2004</option>
                        <option value="2003">2003</option>
                        <option value="2002">2002</option>
                        <option value="2001">2001</option>
                        <option value="2000">2000</option>
                        <option value="1999">1999</option>
                        <option value="1998">1998</option>
                        <option value="1997">1997</option>
                        <option value="1996">1996</option>
                        <option value="1995">1995</option>
                        <option value="1994">1994</option>
                        <option value="1993">1993</option>
                        <option value="1992">1992</option>
                        <option value="1991">1991</option>
                        <option value="1990">1990</option>
                        <option value="1989">1989</option>
                        <option value="1988">1988</option>
                        <option value="1987">1987</option>
                        <option value="1986">1986</option>
                        <option value="1985">1985</option>
                    </select>
                    <br />
                    <label>
                        Rodzaj paliwa:
                    </label>
                    <select name="fuel" value={this.state.fuel} onChange={this.handleInputChange}>
                        <option value="benzyna">Benzyna</option>
                        <option value="diesel">Diesel</option>
                        <option value="LPG">Benzyna + LPG</option>
                    </select>
                    <br />
                    <label>
                        Przebieg:
                        <input
                            name="mileage"
                            type="text"
                            value={this.state.mileage}
                            onChange={this.handleInputChange} />
                    </label>
                    <br />
                    <label>
                        Pojemnosc:
                        <input
                            name="engineCapacity"
                            type="text"
                            value={this.state.engineCapacity}
                            onChange={this.handleInputChange} />
                    </label>
                    <br />
                    <label>
                        Moc:
                        <input
                            name="power"
                            type="text"
                            value={this.state.power}
                            onChange={this.handleInputChange} />
                    </label>
                    <br />
                    <label>
                        Typ nadwozia:
                    </label>
                    <select name="type" value={this.state.type} onChange={this.handleInputChange}>
                        <option value="coupe">Coupe</option>
                        <option value="hatchback">Kompakt</option>
                        <option value="sedan">Sedan</option>
                        <option value="cabrio">Cabrio</option>
                        <option value="cabrio">Auta miejskie</option>
                    </select>
                    <br />
                    <label>
                        Kolor:
                        <input
                            name="color"
                            type="text"
                            value={this.state.color}
                            onChange={this.handleInputChange} />
                    </label>
                    <br />
                    <label>
                        Nowy:
                        <input
                            name="isNew"
                            type="checkbox"
                            checked={this.state.isNew}
                            onChange={this.handleCheckboxChange} />
                    </label>
                    <br />
                    <label>
                        Bezwypadkowy:
                        <input
                            name="hadAccident"
                            type="checkbox"
                            checked={this.state.hadAccident}
                            onChange={this.handleCheckboxChange} />
                    </label>
                    <br />
                    <label>
                        Pierwszy właściciel:
                        <input
                            name="isFirstOwner"
                            type="checkbox"
                            checked={this.state.isFirstOwner}
                            onChange={this.handleCheckboxChange} />
                    </label>
                    <br />
                    <textarea
                        name="description"
                        value={this.state.description}
                        onChange={this.handleInputChange}
                        >
                        ...opis
                    </textarea>
                    <br />
                    <input type="submit" value="Submit"/>
                </form>
                <br/>
                <Results response={this.state.response}/>

            </div>
        );
    }
    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }
    handleCheckboxChange (event) {
        const target = event.target;
        const name = target.name;

        this.setState({
            [name]:  !this.state[name]
        });
    }

    submit(e){
        console.log("SUBMIT")
        e.preventDefault();
        let _this2= this;
        $.ajax({
            url: "/api/search",
            method: 'POST',
            success: function (result) {
                _this2.setState({

                    response:result
                });
            }
        });
    }
}

export default App;

import React, { Component } from 'react';
import logo from './logo.svg';
import $ from 'jquery';
import './App.css';
import SelectYear from './components/SelectYear';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            marka: "BMW",
            model: "Z4",
            year: 2004
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.submit= this.submit.bind(this);
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo" />
                    <h1 className="App-title">Welcome to React</h1>
                </header>

                <button id="button1" onClick={this.getcontent}>Get External Content</button>
                <button id="button2" onClick={this.createUser}>Create Database record</button>
                <br />
                <form onSubmit={this.submit}>
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
                    <input type="submit" value="Submit"/>
                </form>
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

    submit(e){
        console.log("SUBMIT")
        e.preventDefault();
        $.ajax({
            url: "/api/search",
            data:  this.state,
            method: 'POST',
            success: function (result) {
                console.log("RESPONSE: " + result);
            }
        });
    }
    createUser(){
        $.ajax({
            url: "/demo/add?name=Andrew", success: function (result) {
                console.log("RESPONSE: " + result);
            }
        });
    }
    getcontent(){
        $.ajax({
            url: "/api", success: function (result) {
                console.log("RESPONSE get contet: " + result);
                $("App").html(result);
            }
        });
    }
}

export default App;

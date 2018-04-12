import React, { Component } from 'react';
import logo from './logo.svg';
import $ from 'jquery';
import './App.css';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            marka: "",
            model: ""
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

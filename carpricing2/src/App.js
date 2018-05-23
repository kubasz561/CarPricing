import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import Results from './components/Results';
import CarForm from "./components/CarForm";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            response: null
        };
        this.handleResponse = this.handleResponse.bind(this);
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Car Pricing</h1>
                </header>

                <br/>
                <CarForm handleResponse={this.handleResponse.bind(this)}/>
                <br/>
                <Results response={this.state.response}/>

            </div>
        );
    }

    handleResponse(result) {
        this.setState({
            response: result
        });

    }
}

export default App;

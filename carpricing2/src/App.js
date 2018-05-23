import React, {Component} from 'react';
import logo from './logo.svg';
import banner from './porsche-banner.jpg';
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
                <img src={banner} alt="Banner Image"/>
                <h1 className="App-title">Wycena aut</h1>

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

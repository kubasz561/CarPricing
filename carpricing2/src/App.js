import React, {Component} from 'react';
import banner from './porsche-banner.jpg';
import './App.css';
import Results from './components/Results';
import CarForm from "./components/CarForm";

/**
 * Komponent startowy aplikacji, synchronizuje komponenty CarForm i Results.
 */
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

    /**
     * Przekierowanie odpowiedzi z serwera do komponentu Results
     */
    handleResponse(result) {
        this.setState({
            response: result
        });

    }
}

export default App;

import React, {Component} from 'react';
import $ from "jquery";

export default class CarForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            make: "",
            model: "",
            version: "",
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
            description: "...opis",
            method: "LINEAR_PROGRAMMING",
            makeList:[],
            modelList:[],
            versionList:[],
            loading:false,
            message:null
        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleMakeInputChange = this.handleMakeInputChange.bind(this);
        this.handleModelInputChange = this.handleModelInputChange.bind(this);
        this.handleCheckboxChange = this.handleCheckboxChange.bind(this);
        this.handleResponse = this.handleResponse.bind(this);
        this.submit = this.submit.bind(this);
        this.getMake = this.getMake.bind(this);
        this.getModel = this.getModel.bind(this);
        this.getVersion = this.getVersion.bind(this);
        this.getYearList = this.getYearList.bind(this);
        this.getColorList = this.getColorList.bind(this);
    }

    componentDidMount(){
        this.getMake()
    }

    render() {
        return (
            <form onSubmit={this.submit} name="theForm">
                <label>
                    Marka:
                    <select
                        name="make"
                        type="text"
                        value={this.state.make}
                        onChange={this.handleMakeInputChange}>
                        {this.state.makeList.map(make =>
                            <option value={make}>{make}</option>
                        )}
                    </select>
                </label>
                <br/>
                <label>
                    Model:
                    <select
                        name="model"
                        type="text"
                        value={this.state.model}
                        onChange={this.handleModelInputChange}>
                        {this.state.modelList.map(model =>
                            <option value={model}>{model}</option>
                        )}
                    </select>
                </label>
                <br/>
                <label>
                    Wersja:
                    <select
                        name="version"
                        type="text"
                        value={this.state.version}
                        onChange={this.handleInputChange}>
                        {this.state.versionList.map(version =>
                            <option value={version}>{version}</option>
                        )}
                    </select>
                </label>
                <br/>
                <label>
                    Rok:
                </label>
                {/*<SelectYear
                        name="year"
                        value={this.state.year}
                        onChange={this.handleInputChange}
                        />*/}
                <select name="year" value={this.state.year} onChange={this.handleInputChange}>
                    {this.getYearList().map(year =>
                        <option value={year}>{year}</option>
                    )}
                </select>
                <br/>
                <label>
                    Rodzaj paliwa:
                </label>
                <select name="fuel" value={this.state.fuel} onChange={this.handleInputChange}>
                    <option value="benzyna">Benzyna</option>
                    <option value="diesel">Diesel</option>
                    <option value="LPG">Benzyna + LPG</option>
                </select>
                <br/>
                <label>
                    Przebieg:
                    <input
                        name="mileage"
                        type="text"
                        value={this.state.mileage}
                        onChange={this.handleInputChange}/>
                </label>
                <br/>
                <label>
                    Pojemność:
                    <input
                        name="engineCapacity"
                        type="text"
                        value={this.state.engineCapacity}
                        onChange={this.handleInputChange}/>
                </label>
                <br/>
                <label>
                    Moc:
                    <input
                        name="power"
                        type="text"
                        value={this.state.power}
                        onChange={this.handleInputChange}/>
                </label>
                <br/>
                <label>
                    Typ nadwozia:
                </label>
                <select name="type" value={this.state.type} onChange={this.handleInputChange}>
                    {this.getTypeList().map(year =>
                        <option value={year}>{year}</option>
                    )}
                </select>
                <br/>
                <label>
                    Kolor:
                    <select
                        name="color"
                        value={this.state.color}
                        onChange={this.handleInputChange}>
                        {this.getColorList().map(year =>
                            <option value={year}>{year}</option>
                        )}
                    </select>
                </label>
                <br/>
                <label>
                    Nowy:
                    <input
                        name="isNew"
                        type="checkbox"
                        checked={this.state.isNew}
                        onChange={this.handleCheckboxChange}/>
                </label>
                <br/>
                <label>
                    Bezwypadkowy:
                    <input
                        name="hadAccident"
                        type="checkbox"
                        checked={this.state.hadAccident}
                        onChange={this.handleCheckboxChange}/>
                </label>
                <br/>
                <label>
                    Pierwszy właściciel:
                    <input
                        name="isFirstOwner"
                        type="checkbox"
                        checked={this.state.isFirstOwner}
                        onChange={this.handleCheckboxChange}/>
                </label>
                <br/>
                <textarea
                    name="description"
                    value={this.state.description}
                    onChange={this.handleInputChange}
                >
                        ...opis
                    </textarea>
                <br/>
                <label>
                    Metoda aproksymacji:
                    <select name="method" value={this.state.method} onChange={this.handleInputChange}>
                        <option value="LINEAR_PROGRAMMING">LINEAR_PROGRAMMING</option>
                        <option value="MAX_PRICE">MAX_PRICE</option>
                    </select>
                </label>
                <br/>
                <input type="submit" value="Submit"/>
                {this.state.loading && <h1>Loading...</h1>}
                {this.state.message && <h1>{this.state.message}</h1>}
            </form>
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

    handleMakeInputChange(event) {
        this.handleInputChange(event);
        this.getModel(event);
    }
    handleModelInputChange(event) {
        this.handleInputChange(event);
        this.getVersion(event);
    }

    handleCheckboxChange(event) {
        const target = event.target;
        const name = target.name;

        this.setState({
            [name]: !this.state[name]
        });
    }

    handleResponse(result) {
        this.props.handleResponse(result)
    }
    getYearList (){
        var years = [];
        for (var i = 2018; i >= 1990; i--) {
            years.push(i);
        }
        return years;
    }
    getColorList (){
        return ["Beżowy","Biały","Bordowy","Brązowy","Czarny","Czerwony","Fioletowy","Niebieski","Srebrny","Szary","Zielony","Złoty","Żółty","Inny kolor"];
    }
    getTypeList (){
        return ["Sedan","Kombi","Kompakt","SUV","Coupe","Auta miejskie","Auta małe","Minivan"];
    }
    submit(e) {
        this.setState({loading:true,message:null});
        e.preventDefault();
        let _this2 = this;
        $.ajax({
            url: "/api/search",
            data: this.state,
            method: 'POST',
            success: function (result) {
                _this2.handleResponse(result);
                _this2.setState({loading:false});
            },
            error: function (result) {
                _this2.setState({loading:false, message:"500 Internal server error"});
            }
        });
    }
    getMake() {
        let _this2 = this;
        $.ajax({
            url: "/api/getMakes",
            method: 'GET',
            success: function (result) {
                _this2.setState({makeList:result})
            }
        });
    }
    getModel(e) {
        e.preventDefault();
        let _this2 = this;
        $.ajax({
            url: "/api/getModels",
            data: {make: e.target.value},
            method: 'GET',
            success: function (result) {
                _this2.setState({modelList:result})
            }
        });
    }
    getVersion(e){
        e.preventDefault();
        let _this2 = this;
        $.ajax({
            url: "/api/getVersions",
            data: {make:this.state.make, model: e.target.value},
            method: 'GET',
            success: function (result) {
                _this2.setState({versionList:result})
            }
        });
    }
}
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
            searchNewAdverts: false,
            hadAccident: true,
            isFirstOwner: false,
            method: "MATH_PROGRAMMING",
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
        this.validateForm = this.validateForm.bind(this);
    }

    componentDidMount(){
        this.getMake()
    }

    render() {
        return (
            <form onSubmit={this.submit} name="theForm">
                <div className="grid-container-form">
                <label className="grid-item">
                    Marka:
                    <select
                        name="make"
                        type="text"
                        value={this.state.make}
                        onChange={this.handleMakeInputChange}>
                        <option value=""  disabled hidden>Wybierz</option>
                        {this.state.makeList.map(make =>
                            <option value={make}>{make}</option>
                        )}
                    </select>
                </label>

                <label className="grid-item">
                    Model:
                    <select
                        name="model"
                        type="text"
                        value={this.state.model}
                        onChange={this.handleModelInputChange}>
                        <option value=""  disabled hidden>Wybierz</option>
                        {this.state.modelList.map(model =>
                            <option value={model}>{model}</option>
                        )}
                    </select>
                </label>
                <label className="grid-item">
                    Wersja:
                    <select
                        name="version"
                        type="text"
                        value={this.state.version}
                        onChange={this.handleInputChange}>
                        <option value=""  disabled hidden>Wybierz</option>
                        {this.state.versionList.map(version =>
                            <option value={version}>{version}</option>
                        )}
                    </select>
                </label>
            <div/>
                <label className="grid-item">
                    Rok:
                    <select name="year" value={this.state.year} onChange={this.handleInputChange}>
                        {this.getYearList().map(year =>
                            <option value={year}>{year}</option>
                        )}
                    </select>
                </label>
                <label className="grid-item">
                    Przebieg:
                    <input
                        name="mileage"
                        type="text"
                        value={this.state.mileage}
                        onChange={this.handleInputChange}/>
                </label>
            <div/>
            <div/>

                <label className="grid-item">
                    Rodzaj paliwa:
                    <select name="fuel" value={this.state.fuel} onChange={this.handleInputChange}>
                        <option value="Benzyna">Benzyna</option>
                        <option value="Diesel">Diesel</option>
                        <option value="Benzyna + LPG">Benzyna + LPG</option>
                    </select>
                </label>
                <label className="grid-item">
                    Pojemność:
                    <input
                        name="engineCapacity"
                        type="text"
                        value={this.state.engineCapacity}
                        onChange={this.handleInputChange}/>
                </label>
                <label className="grid-item">
                    Moc:
                    <input
                        name="power"
                        type="text"
                        value={this.state.power}
                        onChange={this.handleInputChange}/>
                </label>
            <div/>

                <label className="grid-item">
                    Typ nadwozia:
                    <select name="type" value={this.state.type} onChange={this.handleInputChange}>
                        {this.getTypeList().map(year =>
                            <option value={year}>{year}</option>
                        )}
                    </select>
                </label>
                <label className="grid-item">
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
            <div/>
            <div/>

                <label className="grid-item">
                    Bezwypadkowy:
                    <input
                        name="hadAccident"
                        type="checkbox"
                        checked={this.state.hadAccident}
                        onChange={this.handleCheckboxChange}/>
                </label>
                <label className="grid-item">
                    Pierwszy właściciel:
                    <input
                        name="isFirstOwner"
                        type="checkbox"
                        checked={this.state.isFirstOwner}
                        onChange={this.handleCheckboxChange}/>
                </label>
            <div/>
            <div/>

                <label className="grid-item">
                    Metoda aproksymacji:
                    <select name="method" value={this.state.method} onChange={this.handleInputChange}>
                        <option value="MATH_PROGRAMMING">MATH_PROGRAMMING</option>
                        <option value="MAX_PRICE">MAX_PRICE</option>
                    </select>
                </label>
                <label className="grid-item">
                    Pobierz aktualne ogłoszenia:
                    <input
                        name="searchNewAdverts"
                        type="checkbox"
                        checked={this.state.searchNewAdverts}
                        onChange={this.handleCheckboxChange}/>
                </label>
            </div>
                <input type="submit" value="Wyceń pojazd"/>

                {this.state.loading && <h1>Loading...{this.state.make},{this.state.model},{this.state.version}...</h1>}
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
        for (var i = 2018; i >= 1970; i--) {
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
        if(this.validateForm()) {
            this.setState({loading: true, message: null});
            this.handleResponse(null);
            e.preventDefault();
            let _this2 = this;
            $.ajax({
                url: "/api/search",
                data: this.prepareSubmitData(this.state),
                method: 'POST',
                success: function (result) {
                    _this2.handleResponse(result);
                    _this2.setState({loading: false});
                },
                error: function (result) {
                    _this2.setState({loading: false, message: "500 Internal server error"});
                }
            });
        } else {
            e.preventDefault();
        }
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
                _this2.setState({model:""})
                _this2.setState({version:""})
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
                _this2.setState({version:""})
            }
        });
    }

    validateForm() {
        let result = true;
        if (!this.state.model || !this.state.make) {
            result = false;
            this.setState({loading: false, message: "Marka i model są wymagane do wyceny."});
        }
        if (!(this.isEmptyOrNumber(this.state.engineCapacity) && this.isEmptyOrNumber(this.state.mileage) && this.isEmptyOrNumber(this.state.power))) {
            result = false;
            this.setState({loading: false, message: "Pola przebieg, pojemność i moc mogą zawierać jedynie cyfry"});
        }
        return result;
    }
    isEmptyOrNumber(str) {
        return !str || /^\d+$/.test(str);
    }
    prepareSubmitData(state){
        return {
            make: state.make,
            model: state.model,
            version: state.version,
            year: state.year,
            fuel: state.fuel,
            mileage: state.mileage,
            engineCapacity: state.engineCapacity,
            power: state.power,
            color: state.color,
            type: state.type,
            searchNewAdverts: state.searchNewAdverts,
            hadAccident: state.hadAccident,
            isFirstOwner: state.isFirstOwner,
            method: state.method
        };
    }
}
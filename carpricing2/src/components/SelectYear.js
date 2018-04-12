import React, { Component } from 'react';

class SelectYear extends Component {
    constructor(props) {
        super(props);
        this.handleChange.bind(this);
    }
    handleChange(e){
        this.props.onChange(e);
    }
    render(){
        return (
            <select name={this.props.name} value={this.props.value} onChange={this.handleChange}>
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
        );
  }

}
export default SelectYear;

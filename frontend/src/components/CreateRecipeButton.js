import React, { Component } from "react";
import "./CreateRecipeButton.css";

export class CreateRecipeButton extends Component {
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(event) {
        if (this.props.onClick) {
            this.props.onClick(event);
        }
    }

    render() {
        return(
        <div className="Create-recipe-button" >
            <button onClick={this.handleClick}>
                Create Recipe</button>
        </div>
        );    
    }
}
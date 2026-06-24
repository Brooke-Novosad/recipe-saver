import React, { Component } from "react";

export class RecipeCards extends Component {
    state = {
        recipe: {},
        loadedRecipe: false
    };

    async componentDidMount() {
        const { savedUrl, savedTitle } = this.props;
        if (!this.state.loadedRecipe) {
            const url = `/createRecipe?InstagramUrl=${savedUrl}&recipeTitle=${savedTitle}`;       
            const response = await fetch(url);
            const body = await response.json();
            this.setState({recipe: body, loadedRecipe: true});
        }
        
    }


    render() {
        const {recipe} = this.state;
        const { savedTitle, savedUrl } = this.props;

        return(
            <div>
            <div className="Recipe-Square">
                <p> This is a square {savedTitle} and {savedUrl} </p>
            </div>
            <h2>Recipe</h2>
            <div key={recipe.title}>
              <h3>{recipe.title}</h3>
              <h4>Ingredients</h4>
              {recipe.ingredients && recipe.ingredients.map((ing, idx) => (
                <li key={idx}>{ing.amount} {ing.ingredient}</li>
              ))}
              <h4>Instructions:</h4>
              {recipe.instructions && recipe.instructions.map((instr, idx) => (
                <li key={idx}>{instr}</li>
              ))}
            </div>
            </div>
        );

    }

}

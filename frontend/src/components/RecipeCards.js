import React, { Component } from "react";
import "./RecipeCards.css";

export class RecipeCards extends Component {
    state = {
        recipe: {},
        loadedRecipe: false
    };

    async componentDidMount() {
        const { savedUrl, savedTitle, recipeBody, onRecipeLoaded, index } = this.props;
        if (recipeBody) {
            this.setState({ recipe: recipeBody, loadedRecipe: true });
            return;
        }

        if (!this.state.loadedRecipe) {
            const url = `/createRecipe?InstagramUrl=${savedUrl}&recipeTitle=${savedTitle}`;       
            const response = await fetch(url);
            const body = await response.json();
            this.setState({recipe: body, loadedRecipe: true});
            if (onRecipeLoaded) {
                onRecipeLoaded(index, body);
            }
        }
    }

    render() {
        const {recipe} = this.state;
        const { savedTitle, savedUrl } = this.props;

        return(
            <div className="Recipe-Square">
                <div key={recipe.title}>
                    <h3>{recipe.title}</h3>
                    <h4>Ingredients</h4>
                        {recipe.ingredients && recipe.ingredients.map((ing, idx) => (
                        <p>{ing.amount} {ing.ingredient}</p>
                        ))}
                    <h4>Instructions:</h4>
                    {recipe.instructions && recipe.instructions.map((instr, idx) => (
                    <p key={idx}>{instr}</p>
                    ))}
                </div>
            </div>
        );
    }

}

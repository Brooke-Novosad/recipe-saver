import './App.css';
import React, { Component } from "react";
import { CreateRecipeInput } from "./components/CreateRecipeInput";

class App extends Component {
  state = {
    recipe: {},
    InputTitle: "",
    InputUrl: ""
  };

  handleTitleChange = (e) => {
    this.setState({ InputTitle: e.target.value });
  };

  handleUrlChange = (e) => {
    this.setState({ InputUrl: e.target.value });
  };

  async componentDidMount() {
    const response = await fetch('/createRecipe?InstagramUrl=https://www.instagram.com/p/DZNUfEUBw6Y/&recipeTitle=cheeseburger');
    const body = await response.json();
    this.setState({recipe: body});
  }

  render() {
    const { recipe, InputTitle, InputUrl } = this.state;
    return (
      <div className="App">
        <header className="App-header">
          <div className="App-intro">
            <CreateRecipeInput
              label="Title"
              value={InputTitle}
              onChange={this.handleTitleChange}
            />
            <CreateRecipeInput
              label="Instagram URL"
              value={InputUrl}
              onChange={this.handleUrlChange}
            />
            <p> the title is {InputTitle}</p>
            <p> the url is {InputUrl}</p>
            {/* <h2>Recipe</h2>
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
            </div>*/}
          </div>
        </header>
      </div>
    );
  }
}
export default App;


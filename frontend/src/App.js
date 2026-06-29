import './App.css';
import React, { Component } from "react";
import { CreateRecipeInput } from "./components/CreateRecipeInput";
import { CreateRecipeButton } from "./components/CreateRecipeButton";
import { RecipeCards } from './components/RecipeCards';

class App extends Component {
  state = {
    InputTitle: "",
    InputUrl: "",
    showCreateForm: false,
    recipes: []
  };

  handleShowCreateForm = () => this.setState({ showCreateForm: true });

  handleTitleChange = (e) => {
    this.setState({ InputTitle: e.target.value });
  };

  handleUrlChange = (e) => {
    this.setState({ InputUrl: e.target.value });
  };

  handleRecipeLoaded = (index, recipeBody) => {
    this.setState((prevState) => {
      const updatedRecipes = [...prevState.recipes];
      updatedRecipes[index] = {
        ...updatedRecipes[index],
        recipe: recipeBody
      };
      return { recipes: updatedRecipes };
    });
  };

  handleSubmit = (event) => {
    event.preventDefault();
    this.setState((state) => ({
      recipes: [
        ...state.recipes,
        {
          title: state.InputTitle,
          url: state.InputUrl,
          recipe: null
        }
      ],
      showCreateForm: false,
      InputTitle: "",
      InputUrl: ""
    }));
  };

  render() {
    const { InputTitle, InputUrl, showCreateForm, recipes } = this.state;
    return (
      <div className="App">
        <header className="App-header">
          <div className="App-intro">
            <CreateRecipeButton onClick={this.handleShowCreateForm} />
            {showCreateForm && (
              <CreateRecipeInput
                label1="Title"
                label2="Instagram URL"
                value1={InputTitle}
                value2={InputUrl}
                onChange1={this.handleTitleChange}
                onChange2={this.handleUrlChange}
                onSubmit={this.handleSubmit}
              />
            )}
            {recipes.map((recipe, index) => (
              <RecipeCards
                key={index}
                savedTitle={recipe.title}
                savedUrl={recipe.url}
                recipeBody={recipe.recipe}
                index={index}
                onRecipeLoaded={this.handleRecipeLoaded}
              />
            ))}
          </div>
        </header>
      </div>
    );
  }
}
export default App;


import logo from './logo.svg';
import './App.css';
import React, { Component } from "react"

class App extends Component {
  state = {
    recipe: []
  };

  async componentDidMount() {
    const response = await fetch('/createRecipe?InstagramUrl=https://www.instagram.com/p/DZNUfEUBw6Y/&recipeTitle=cheeseburger');
    const body = await response.json();
    this.setState({recipe: body});
  }

  render() {
    const {recipe} = this.state;
    return (
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <div className="App-intro">
              <h2>Recipe</h2>
              {recipe.map(recipe =>
                  <div key={recipe.name}>
                    {recipe.Ingredients} ({recipe.Instructions})
                  </div>
              )}
            </div>
          </header>
        </div>
    );
  }
}
export default App;


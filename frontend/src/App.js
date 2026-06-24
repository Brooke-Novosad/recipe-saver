import './App.css';
import React, { Component } from "react";
import { CreateRecipeInput } from "./components/CreateRecipeInput";
import { RecipeCards } from './components/RecipeCards';

class App extends Component {
  state = {
    InputTitle: "",
    InputUrl: "",
    savedTitle: "",
    savedUrl: "",
    submitted: false
  };

  handleTitleChange = (e) => {
    this.setState({ InputTitle: e.target.value });
  };

  handleUrlChange = (e) => {
    this.setState({ InputUrl: e.target.value });
  };

  handleSubmit = (event) => {
    event.preventDefault();
    this.setState((state) => ({
      submitted: true,
      savedTitle: state.InputTitle,
      savedUrl: state.InputUrl
    }));
  };

  render() {
    const { recipe, InputTitle, InputUrl, savedTitle, savedUrl, submitted } = this.state;
    return (
      <div className="App">
        <header className="App-header">
          <div className="App-intro">
            {!submitted && (
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

            {submitted && (
              <div>
                <RecipeCards savedTitle={savedTitle} savedUrl= {savedUrl}/>
              </div>
            )}
          </div>
        </header>
      </div>
    );
  }
}
export default App;


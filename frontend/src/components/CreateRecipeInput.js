import React, { Component } from "react";

export class CreateRecipeInput extends Component {
  constructor(props) {
    super(props);
    this.handleTitleChange = this.handleTitleChange.bind(this);
    this.handleUrlChange = this.handleUrlChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleTitleChange(event) {
    if (this.props.onChange1) {
      this.props.onChange1(event);
    }
  }

  handleUrlChange(event) {
    if (this.props.onChange2) {
      this.props.onChange2(event);
    }
  }

  handleSubmit(event) {
    event.preventDefault();
    if (this.props.onSubmit) {
      this.props.onSubmit(event);
    }
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          {this.props.label1 || "Title"}:
          <input
            type="text"
            value={this.props.value1 || ""}
            onChange={this.handleTitleChange}
          />
        </label>
        <br />
        <label>
          {this.props.label2 || "URL"}:
          <input
            type="text"
            value={this.props.value2 || ""}
            onChange={this.handleUrlChange}
          />
        </label>
        <br />
        <button type="submit">Submit</button>
      </form>
    );
  }
}

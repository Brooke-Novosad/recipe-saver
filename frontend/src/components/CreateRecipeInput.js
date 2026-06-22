import React, { Component } from "react"


export class CreateRecipeInput extends Component {
  constructor(props) {
    super(props);
    this.state = {
      submitted: false
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    if (this.props.onChange) {
      this.props.onChange(event);
    }
  }

  handleSubmit(event) {
    event.preventDefault();
    if (this.props.onSubmit) {
      this.props.onSubmit(this.props.value);
    }
    this.setState({ submitted: true });
  }

  render() {
    if (this.state.submitted) {
      return null;
    }

    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          {this.props.label || "Title"}:
          <input
            type="text"
            value={this.props.value || ""}
            onChange={this.handleChange}
          />
        </label>
        <input type="submit" value="Submit" />
      </form>
    );
  }
}
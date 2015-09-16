import React from 'react';

class HelloMessage extends React.Component {
  constructor() {
    super()
  }

  render() {
    return <div>Hello {this.props.name}</div>
  }
}

export default HelloMessage;

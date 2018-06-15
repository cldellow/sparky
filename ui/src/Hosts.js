import React, { Component } from 'react';
import { VictoryStack, VictoryChart, VictoryAxis, VictoryBar } from 'victory';


class Hosts extends Component {
  constructor(props) {
    super(props);

    /*
    const data = [];
    for(var i = 1529030759; i < 1529030759 + 120; i++) {
      data.push({
        ts: i,
        internal: Math.random() * 20,
        externalOut: Math.random() * 100,
        externalIn: Math.random() * 100
      });
    }
    */

    this.state = {};
    this.tick = this.tick.bind(this);;
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.tick(),
      1000
    );
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick() {
    const me = this;
    fetch('http://localhost:4567/hosts')
      .then(function(response) {
        return response.json();
      })
      .then(function(json) {
        me.setState(json);
      })
  }

  render() {
    return (
      <div>
      <div>Actual vs Requested Machines</div>

      <h1 className="cpu" style={{"margin-top": "100"}}>
        {this.state.actual}/{this.state.requested}
      </h1>
    </div>
    );
  }
}

export default Hosts;

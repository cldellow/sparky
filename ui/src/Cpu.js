import React, { Component } from 'react';
import { VictoryChart, VictoryAxis, VictoryLine } from 'victory';


class Cpu extends Component {
  constructor(props) {
    super(props);

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
    fetch('http://localhost:4567/cluster-cpu')
      .then(function(response) {
        return response.json();
      })
      .then(function(json) {
        me.setState({data: json});
      })
  }

  render() {
    return (
      <div className="cpu">
      <div>Cluster CPU</div>
<div style={{"height": 300}}>
<VictoryChart>
  <VictoryAxis
    dependentAxis
    domain={[0, 1]}
    tickFormat={(x) => (x * 100) + "%" }/>
  <VictoryLine data={this.state.data} x="ts" y="value"/>
</VictoryChart>
</div>
      </div>
    );
  }
}

export default Cpu;

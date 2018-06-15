import React, { Component } from 'react';
import { VictoryChart, VictoryAxis, VictoryLine } from 'victory';


class Cpu extends Component {
  constructor(props) {
    super(props);

    const data = [];
    for(var i = 1529030759; i < 1529030759 + 120; i++) {
      data.push({ts: i, cpu: Math.random()});
    }

    this.state = {data: data};
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.tick(),
      100
    );
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick() {
    const data = this.state.data.slice(1);
    data.push({ts: data[data.length - 1].ts + 1, cpu: Math.random()});
    this.setState({data: data});
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
  <VictoryLine data={this.state.data} x="ts" y="cpu"/>
</VictoryChart>
</div>
      </div>
    );
  }
}

export default Cpu;

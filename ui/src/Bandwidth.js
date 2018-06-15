import React, { Component } from 'react';
import { VictoryStack, VictoryChart, VictoryAxis, VictoryBar } from 'victory';


class Bandwidth extends Component {
  constructor(props) {
    super(props);

    const data = [];
    for(var i = 1529030759; i < 1529030759 + 120; i++) {
      data.push({
        ts: i,
        internal: Math.random() * 20,
        externalOut: Math.random() * 100,
        externalIn: Math.random() * 100
      });
    }

    this.state = {data: data};
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
    const data = this.state.data.slice(1);
    data.push({
      ts: data[data.length - 1].ts + 1,
      internal: Math.random() * 20,
      externalOut: Math.random() * 100,
      externalIn: Math.random() * 100
    });

    this.setState({data: data});
  }

  render() {
    return (
      <div className="cpu">
      <div>Cluster Bandwidth</div>
<div style={{"height": 300}}>
<VictoryChart>
  <VictoryAxis
    dependentAxis
    domain={[0, 1000]}
    tickFormat={(x) => x + "MB/sec" }/>
    {/* external IN, external OUT, internal ? */}
  <VictoryStack
    colorScale={["tomato", "orange", "cyan"]}>
    <VictoryBar data={this.state.data} x="ts" y="externalOut"/>
    <VictoryBar data={this.state.data} x="ts" y="externalIn"/>
    <VictoryBar data={this.state.data} x="ts" y="internal"/>
  </VictoryStack>
</VictoryChart>
</div>
      </div>
    );
  }
}

export default Bandwidth;

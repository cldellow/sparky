import React, { Component } from 'react';
import { VictoryChart, VictoryAxis, VictoryBar } from 'victory';


class Job extends Component {
  constructor(props) {
    super(props);
    
    this.state = {id: 123, description: "Doing stuff", duration: 12, tasksDone: 12, tasksActive: 4, tasksTotal: 20};
  }

  render() {
    return (
        <tr>
          <td>{this.state.id}</td>
          <td>{this.state.description}</td>
          <td>{this.state.duration}</td>
          <td>{this.state.tasksDone} {this.state.tasksActive} {this.state.tasksTotal}</td>
        </tr>
    );
  }
}

export default Job;

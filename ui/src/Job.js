import React, { Component } from 'react';

class Job extends Component {
  constructor(props) {
    super(props);
    this.state = props.data;
  }


  render() {
    const pct = (num, denom) => {
      const rv = Math.floor(100.0 * num / denom);
      return rv + "%";
    }

    return (
        <tr>
          <td>{this.state.id}</td>
          <td>{this.state.description}</td>
          <td>{Math.ceil(((this.state.finishMs || new Date()) - this.state.startMs) / 1000)} sec</td>
          <td>
            <div className="progress" style={{"marginBottom": "0", "position": "relative"}}>
              <div className="progress-bar bg-success" style={{"width": pct(this.state.tasksDone, this.state.tasksTotal), "backgroundColor": "#3EC0FF"}}></div>
              <div className="progress-bar bg-info" style={{"width": pct(this.state.tasksActive, this.state.tasksTotal), "backgroundColor": "#A0DFFF"}}></div>
              <span className="task-progress">{this.state.tasksDone} / {this.state.tasksTotal}</span>
            </div>
  </td>
        </tr>
    );
  }
}

export default Job;

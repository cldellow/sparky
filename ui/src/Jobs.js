import React, { Component } from 'react';
import Job from './Job.js';

class Jobs extends Component {
  constructor(props) {
    super(props);
    
    this.state = {
      jobs: [
      {
      id: 123,
      description: "Doing stuff",
      duration: 12,
      tasksDone: 12,
      tasksActive: 4,
      tasksTotal: 20
      },
      {
      id: 124,
      description: "Doing other stuff",
      duration: 234,
      tasksDone: 14,
      tasksActive: 4,
      tasksTotal: 20
      }

      ]};
  }


  render() {
    const jobs = [];
    this.state.jobs.forEach(el => {
      jobs.push(<Job key={el.id} data={el}/>)
    });
    return (
      <table className="jobs">
        <thead>
          <tr>
            <th>ID</th>
            <th>Description</th>
            <th>Duration</th>
            <th>Tasks</th>
          </tr>
        </thead>
        <tbody>
          {jobs}
        </tbody>
      </table>
    );
  }
}

export default Jobs;

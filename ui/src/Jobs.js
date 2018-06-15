import React, { Component } from 'react';
import Job from './Job.js';

class Jobs extends Component {
  constructor(props) {
    super(props);
    
    this.state = {
      jobs: []
    };
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
    //this.setState({data: data});
    fetch('http://localhost:4567/active-jobs')
      .then(function(response) {
        return response.json();
      })
      .then(function(json) {
        me.setState({jobs: json});
      })
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

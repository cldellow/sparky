import React, { Component } from 'react';
import logo from './logo.svg';
import Job from './Job.js';
import { Grid, Row, Col } from 'react-bootstrap';
import './App.css';

class App extends Component {
  render() {
    const jobs = [ ];
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    jobs.push(<Col md={6}><Job/></Col>);
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to Stuff</h1>
        </header>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload!
        </p>
        <Grid>
          <Row>
            {jobs}
          </Row>

        </Grid>
      </div>
    );
  }
}

export default App;

import React, { Component } from 'react';
import Cpu from './Cpu.js';
import Jobs from './Jobs.js';
import Bandwidth from './Bandwidth.js';
import { Grid, Row, Col } from 'react-bootstrap';
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src="/logo.png" className="App-logo" alt="logo" style={{"float": "left"}}/>
          <h1 className="App-title">Your first mistake was writing a Spark application.</h1>
        </header>
        <p className="App-intro">
        </p>

        <Grid>
          <Row>
            <Col md={12}>
              <Jobs/>
            </Col>
          </Row>
          <Row>
            <Col md={6}>
              <Cpu/>
            </Col>
            <Col md={6}>
              <Bandwidth/>
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

export default App;

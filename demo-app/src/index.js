import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import axios from 'axios';
import { StateProvider } from "./StateProvider";
import reducer, { initialState } from "./reducer";

axios.defaults.headers.common = { 'Authorization': 'Bearer '.concat(localStorage.getItem('token')), 'Access-Control-Allow-Origin': '*' };

ReactDOM.render(
  <React.StrictMode>
    <StateProvider initialState={initialState} reducer={reducer}>
      <App />
    </StateProvider>
  </React.StrictMode>,
  document.getElementById('root')
);


import { BrowserRouter, Routes, Switch, Route, Link } from "react-router-dom";
import Homepage from "./pages/Homepage";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import UserDetails from "./pages/UserDetails";
import reducer, { initialState } from "./reducer";
import { StateProvider } from "./StateProvider";
import axios from 'axios';
import React, { useEffect } from "react";
import { useStateValue } from "./StateProvider";
import { apiPrefix } from './env';

function App() {
  
  const [{ user }, dispatch] = useStateValue();

  useEffect(() => {

    axios.get(apiPrefix + 'api/user').then(
      res => {
        dispatch({
          type: 'SET_USER',
          user: res.data,
        })
      },
      err => {
        console.log(err)
      }
    )
  }, [])

  return (
    <div className="App">
      <StateProvider initialState={initialState} reducer={reducer}>
        <BrowserRouter>
          <Routes>
            <Route exact path="/" element={<Homepage />} />
            <Route exact path="/signup" element={<Signup />} />
            <Route exact path="/login" element={<Login />} />
            <Route exact path="/user/:userId" element={<UserDetails />} />
          </Routes>
        </BrowserRouter>
      </StateProvider>
    </div>
  );
}

export default App;

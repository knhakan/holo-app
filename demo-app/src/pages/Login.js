import React, { useState } from "react";
import Header from '../components/Header'
import axios from 'axios';
import { Link, useNavigate } from "react-router-dom";
import { useStateValue } from "../StateProvider";
import './css/Login.css';
import { apiPrefix } from '../env';


function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [userId, setUserId] = useState('')
    const navigate = useNavigate();
    const [message, setMessage] = useState(undefined);
    const [{ user }, dispatch] = useStateValue();

    const handleSubmit = event => {
        event.preventDefault();

        const user = {
            username: username,
            password: password,
        }

        axios.post(apiPrefix + "api/authenticate", { username: user.username, password: user.password }, { headers: { 'Authorization': '' } })
            .then(res => {

                setUserId(res.data.userId)
                dispatch({
                    type: 'SET_USER',
                    user: res.data.userId,
                })
                localStorage.setItem('token', res.data.authenticationResponse);
                localStorage.setItem('user', res.data.userId);
                if (user) {
                    navigate('/');
                    window.location.reload(false);
                }


            })
            .catch((err) => {
                console.log(err);
                setMessage('Username or password is wrong. I cant tell which one though');
            })
    }
    return (
        <div>
            <Header />
            <div className="login-main">
                <form onSubmit={handleSubmit} className='login-form'>
                    <h1 className='login-title'>Login</h1>
                    <label className='login-label'>
                        Email:
                        <input
                            placeholder="Email"
                            required
                            className='login-input'
                            onChange={event => setUsername(event.target.value)}
                            type="email"
                            name="model" />
                    </label>

                    <label className='login-label'>
                        Password:
                        <input
                            placeholder="Password"
                            minLength={5}
                            maxLength={32}
                            required
                            className='login-input'
                            onChange={event => setPassword(event.target.value)}
                            type="password"
                            name="model" />
                    </label>

                    <input type="submit" value="Login" className='login-button' />

                    <Link to='/signup' className='signup-link'>
                        <div className='login-text'>
                            You dont have an account?
                        </div>
                    </Link>

                    <div className='login-error-message'>
                        {message}
                    </div>
                </form>
            </div>
        </div>
    )
}

export default Login

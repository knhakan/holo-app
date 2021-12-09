import React, { useState } from 'react'
import axios from 'axios';
import './css/Signup.css';
import { useNavigate } from "react-router-dom";
import { apiPrefix } from '../env';


function Signup() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState(undefined);
    const navigate = useNavigate();

    const handleSubmit = event => {

        event.preventDefault();
        const user = {
            username: username,
            password: password,
        };
        axios.post(apiPrefix + `api/add`, {
            username: user.username,
            password: user.password,
        }, { headers: { 'Authorization': '' } })
            .then(res => {
                setTimeout(function () { navigate('/login'); }, 3000);
            })
            .catch(error => {
                if (error.response.status === 409) {
                    setMessage("User already exists")
                } else {
                    setMessage("User could not be saved")
                }
            })
    }


    return (
        <div>
            <div className="signup-main">
                <form onSubmit={handleSubmit} className='signup-form'>
                    <h1 className='signup-title'>Sign up</h1>

                    <label className='signup-label'>
                        Email:
                        <input
                            placeholder="Email"
                            required
                            className='signup-input'
                            onChange={event => setUsername(event.target.value)}
                            type="email"
                            name="model" />
                    </label>

                    <label className='signup-label'>
                        Password:
                        <input
                            placeholder="Password"
                            minLength={5}
                            maxLength={32}
                            required
                            className='signup-input'
                            onChange={event => setPassword(event.target.value)}
                            type="password"
                            name="model" />
                    </label>


                    <input type="submit" value="Sign up" className='signup-button' />
                    <div className='signup-error-message'>
                        {message}
                    </div>
                </form>
            </div>
        </div>
    )
}

export default Signup

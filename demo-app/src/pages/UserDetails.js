import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router';
import axios from 'axios';
import Header from '../components/Header';
import './css/UserDetails.css';
import { useStateValue } from "../StateProvider";
import { useNavigate } from "react-router-dom";
import { apiPrefix } from '../env';


function UserDetails() {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [{ user }, dispatch] = useStateValue();
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [age, setAge] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [status, setStatus] = useState(undefined);
    const handleDelete = () => {

        axios.delete(apiPrefix + `api/users/` + user)
            .then(res => {
                setTimeout(function () { window.location.reload(); }, 3000);
                setStatus({ type: 'success' });
                localStorage.clear();

                dispatch({
                    type: 'SET_USER',
                    user: null,
                })

                navigate('/login');
            })
            .catch(error => {
                console.log(error);
                setStatus({ type: 'error' });
            })
    }

    const handleSubmit = event => {

        event.preventDefault();
        const user = {
            firstName: firstName,
            lastName: lastName,
            age: age,
            username: username,
            password: password
        };
        axios.put(apiPrefix + `api/users/` + userId, {
            firstName: user.firstName,
            lastName: user.lastName,
            age: user.age,
        })
            .then(res => {
                setTimeout(function () { window.location.reload(); }, 3000);
                setStatus({ type: 'success' });
            })
            .catch(error => {
                console.log(error);
                setStatus({ type: 'error' });
            })
    }


    useEffect(() => {
        axios.get(apiPrefix + `api/users/` + userId)
            .then(res => {
                setUsername(res.data.username);
                setFirstName(res.data.firstName);
                setLastName(res.data.lastName);
                setAge(res.data.age);
            })
    }, [])

    return (
        <div>
            <Header />
            {user && user === userId ?
                <div className="userDetails-main">
                    <form onSubmit={handleSubmit} className='userDetails-form'>
                        <h1 className='userDetails-title'>User Details</h1>

                        <label className='userDetails-label'>
                            Email:
                            <input
                                value={username}
                                placeholder="Email"
                                readOnly={true}
                                className='userDetails-input'
                                onChange={event => setUsername(event.target.value)}
                                type="email"
                                name="model" />
                        </label>

                        <label className='userDetails-label'>
                            First name:
                            <input
                                value={firstName}
                                placeholder="First name"
                                maxLength={50}
                                className='userDetails-input'
                                onChange={event => setFirstName(event.target.value)}
                                type="text"
                                name="model" />
                        </label>

                        <label className='userDetails-label'>
                            Last Name:
                            <input
                                value={lastName}
                                placeholder="Last name"
                                maxLength={50}
                                className='userDetails-input'
                                onChange={event => setLastName(event.target.value)}
                                type="text"
                                name="model" />
                        </label>

                        <label className='userDetails-label'>
                            Age:
                            <input
                                value={age}
                                placeholder="Age"
                                defaultValue={null}
                                className='userDetails-input'
                                onChange={event => setAge(event.target.value)}
                                type="number"
                                min="0"
                                max="120"
                                name="age" />
                        </label>


                        <input type="submit" value="Save" className='userDetails-button' />
                        <button onClick={handleDelete} className='userDetails-delete-button' >Delete User</button>
                    </form>

                </div> : <div className='no-user-message'> User data is not available! </div>}

            <div>
                {status?.type === 'success' && <p className='userDetails-message'>Success! User details have been saved</p>}
                {status?.type === 'error' && (
                    <p className='signup-message'>Oups! User details could not be saved</p>
                )}
            </div>
        </div>
    )
}

export default UserDetails

import React from 'react'
import { Link, useNavigate } from 'react-router-dom';
import './css/Header.css';
import { useStateValue } from "../StateProvider";

function Header() {
    const navigate = useNavigate();
    const [{ user }, dispatch] = useStateValue();

    const handleLogout = () => {
        localStorage.clear();

        dispatch({
            type: 'SET_USER',
            user: null,
        })
        navigate('/login');
    }

    return (
        <div className='navbar'>
            <div className='navbar-items'>
                <Link to='/' className='navbar-action-link'>
                    Home
                </Link>
                <Link to={`/user/${user}`} className='navbar-action-link'>
                    User Details
                </Link>
            </div>
            <div className='navbar-action'>
                {user ? <div style={{ marginLeft: '3px' }} onClick={handleLogout} className='navbar-action-link'>
                    Logout
                </div> : <Link to='/login' className='navbar-action-link'>
                    Login
                </Link>}
            </div>
        </div>
    )
}

export default Header

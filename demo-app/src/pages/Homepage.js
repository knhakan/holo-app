import React from 'react'
import Header from '../components/Header'
import './css/Homepage.css';

function Homepage() {
    return (
        <div>
            <Header />
            <div className='home-welcome-message'>
                Welcome to Holoride Demo Application!
            </div>
        </div>
    )
}

export default Homepage

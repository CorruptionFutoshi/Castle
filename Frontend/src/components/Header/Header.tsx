import React, { useState, FC, KeyboardEvent, useEffect } from 'react';
import { Link, useNavigate } from "react-router-dom";
import searchIcon from "../resources/serchIcon.png";

export const Header = () => {
    const [showSearch, setShowSearch] = useState(false);
    const [searchText, setSearchText] = useState('');
    const navigate = useNavigate();
    const [hasSessionId, setHasSessionId] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);

    const toggleSearch = () => {
        setShowSearch(!showSearch);
    };

    const handleSearch = async () => {
        navigate(`/search/${searchText}`);
    };

    const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') handleSearch();
    };

    const checkSessionId = () => {
        const sessionId = document.cookie.split('; ').find(row => row.startsWith('session'));
        setHasSessionId(!!sessionId);
    };

    const handleSignout = async () => {
        const response = await fetch('http://localhost:8080/app/member/signout/', { method: 'DELETE', credentials: 'include' });

        if (response.ok) {
            const disposeCookieResponse = await fetch('http://localhost:8080/app/member/signout/disposecookie', { method: 'GET', credentials: 'include' });

            if (disposeCookieResponse.ok) {
                alert('ログアウトしました');
                setHasSessionId(false);
            }
        }
    };

    const handleCancelMember = async () => {
        const response = await fetch('http://localhost:8080/app/member/cancelmember/', { method: 'DELETE', credentials: 'include' });

        if (response.ok) {
            const disposeCookieResponse = await fetch('http://localhost:8080/app/member/signout/disposecookie', { method: 'GET', credentials: 'include' });

            if (disposeCookieResponse.ok) {
                alert('退会しました');
                setHasSessionId(false);
            }
        }
    };

    const confirmCancelMember = () => {
        setShowConfirm(true);
    };

    const cancelMember = () => {
        handleCancelMember();
        setShowConfirm(false);
    };

    const declineCancelMember = () => {
        setShowConfirm(false);
    };

    useEffect(() => {
        checkSessionId();
    }, [document.cookie]);

    return (
        <div>
            <div id="header">
                <div className='blogTitle'>
                    <Link to={`/`}>未定</Link>
                </div>
                <nav>
                    <Link to={`/about`}>About</Link>
                    <Link to={`/tags`}>Tag</Link>
                    {hasSessionId ? (
                        <>
                            <button id="signoutButton" onClick={handleSignout}>Signout</button>
                            <button id="cancelMemberButton" onClick={confirmCancelMember}>CancelMember</button>
                        </>
                    ) : (
                        <>
                            <Link to={`/signin`}>Signin</Link>
                            <Link to={`/signup`}>Signup</Link>
                        </>
                    )}
                </nav>
                <button id="searchButton" onClick={toggleSearch}>
                    <img src={searchIcon} alt="Search" className='header-search-icon' />
                </button>
            </div>

            {showConfirm && (
                <div id="confirmDialog">
                    <p>退会しますか？</p>
                    <button onClick={cancelMember}>はい</button>
                    <button onClick={declineCancelMember}>いいえ</button>
                </div>
            )}

            {showSearch && (
                <div id="searchBar">
                    <input type="text" onChange={e => setSearchText(e.target.value)} onKeyPress={handleKeyPress} />
                </div>
            )}
            <hr />
        </div>
    );
};

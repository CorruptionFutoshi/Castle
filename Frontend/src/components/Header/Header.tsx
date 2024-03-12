import React, { useState, useContext, KeyboardEvent, useEffect } from 'react';
import { LoginContext } from "../../index";
import { Link, useNavigate } from "react-router-dom";
import searchIcon from "../resources/serchIcon.png";

export const Header = () => {
    const [showSearch, setShowSearch] = useState(false);
    const [searchText, setSearchText] = useState('');
    const [showConfirm, setShowConfirm] = useState(false);
    const navigate = useNavigate();
    const loginContext = useContext(LoginContext);
    let hasSessionId: boolean = false;
    let setHasSessionId: React.Dispatch<React.SetStateAction<boolean>>;

    if (loginContext) {
        ({ hasSessionId, setHasSessionId } = loginContext);
    }

    const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') navigate(`/search/${searchText}`);
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

    const cancelMember = () => {
        handleCancelMember();
        setShowConfirm(false);
    };

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
                            <button id="cancelMemberButton" onClick={() => setShowConfirm(true)}>CancelMember</button>
                        </>
                    ) : (
                        <>
                            <Link to={`/signin`}>Signin</Link>
                            <Link to={`/signup`}>Signup</Link>
                        </>
                    )}
                </nav>
                <button id="searchButton" onClick={() => setShowSearch(!showSearch)}>
                    <img src={searchIcon} alt="Search" className='header-search-icon' />
                </button>
            </div>

            {showConfirm && (
                <div id="confirmDialog">
                    <p>退会しますか？</p>
                    <button onClick={cancelMember}>はい</button>
                    <button onClick={() => setShowConfirm(false)}>いいえ</button>
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

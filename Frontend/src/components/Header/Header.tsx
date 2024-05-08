import React, { useState, useContext, KeyboardEvent, useEffect } from 'react';
import { LoginContext } from "../../index";
import { Link, useNavigate } from "react-router-dom";
import searchIcon from "../resources/serchIcon.png";
import { API_URL } from '../../config';

export const Header = () => {
    const [showSearch, setShowSearch] = useState(false);
    const [searchText, setSearchText] = useState('');
    const [showConfirm, setShowConfirm] = useState(false);
    const navigate = useNavigate();
    const loginContext = useContext(LoginContext);
    let hasSessionId: boolean = false;
    let setHasSessionId: React.Dispatch<React.SetStateAction<boolean>>;

    useEffect(() => {
        fetch(`${API_URL}/app/member/issignin`, {
            method: "GET",
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
            credentials: 'include',
        })
            .then(response => response.json())
            .then(data => { setHasSessionId(data.isSignin); });
    }, []);

    if (loginContext) {
        ({ hasSessionId, setHasSessionId } = loginContext);
    }

    const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') navigate(`/search/${searchText}`);
    };

    const handleSignout = async () => {
        const response = await fetch(`${API_URL}/app/member/signout/`, {
            method: 'DELETE',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
            credentials: 'include'
        });

        if (response.ok) {
            alert('ログアウトしました');
            setHasSessionId(false);
        }
    };

    const handleCancelMember = async () => {
        const response = await fetch(`${API_URL}/app/member/cancelmember/`, {
            method: 'DELETE',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
            credentials: 'include'
        });

        if (response.ok) {
            alert('退会しました');
            setHasSessionId(false);
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
                    <Link to={`/`}>Catsle{process.env.BLOG_TITLE}</Link>
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

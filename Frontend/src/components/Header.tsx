import React, { useState, FC, KeyboardEvent } from 'react';
import { Link, useNavigate } from "react-router-dom";
import searchIcon from "../resources/serchIcon.png";

export const Header: FC = () => {
    const [showSearch, setShowSearch] = useState(false);
    const [searchText, setSearchText] = useState('');
    const navigate = useNavigate();

    const toggleSearch = () => {
        setShowSearch(!showSearch);
    };

    const handleSearch = async () => {
        navigate(`/search/${searchText}`);
    };

    const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') handleSearch();
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
                </nav>
                <button id="searchButton" onClick={toggleSearch}>
                    <img src={searchIcon} alt="Search" className='header-search-icon' />
                </button>
            </div>

            {showSearch && (
                <div id="searchBar">
                    <input type="text" onChange={e => setSearchText(e.target.value)} onKeyPress={handleKeyPress} />
                </div>
            )}
            <hr />
        </div>
    );
};

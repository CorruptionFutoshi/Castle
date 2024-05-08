import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { LoginContext } from "../../index";
import { API_URL } from '../../config';

export const Signin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const loginContext = useContext(LoginContext);
    let setHasSessionId: React.Dispatch<React.SetStateAction<boolean>>;

    if (loginContext) {
        ({ setHasSessionId } = loginContext);
    }

    const handleLogin = async () => {
        try {
            const response = await fetch(`${API_URL}/app/member/signin/`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify({
                    username,
                    password
                }),
                credentials: 'include'
            });

            if (response.status === 200) {
                alert('ログインに成功しました');
                setHasSessionId(true);
                navigate(`/`);
            } else {
                setError('ユーザーネーム又はパスワードが間違っています');
            }
        } catch (error) {
            setError('エラーが発生しました');
        }
    };

    return (
        <div id="signinup-container">
            <h2 id="signinup-title">Signin</h2>
            <input
                id="signinup-username"
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                id="signinup-password"
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button id="signinup-button" onClick={handleLogin}>Signin</button>
            {error && <p id="signinup-error">{error}</p>}
        </div>
    );
};

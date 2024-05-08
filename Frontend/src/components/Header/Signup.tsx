import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { API_URL } from '../../config';

export const Signup = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await fetch(`${API_URL}/app/member/signup/`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify({
                    username,
                    password
                })
            });

            if (response.status === 200) {
                alert('会員登録に成功しました');
                navigate(`/`);
            } else {
                setError('ユーザーネームが既に使われています');
            }
        } catch (error) {
            setError('エラーが発生しました');
        }
    };

    return (
        <div id="signinup-container">
            <h2 id="signinup-title">Signup</h2>
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
            <button id="signinup-button" onClick={handleLogin}>Signup</button>
            {error && <p id="signinup-error">{error}</p>}
            申し訳ありませんが、外部サービスで使用している認証情報を流用しないでください。このブログは初学者が作成・運用しているため流出の可能性があります。
        </div>
    );
};

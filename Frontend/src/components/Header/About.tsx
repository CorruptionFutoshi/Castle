import React, { useState, useEffect } from "react";
import { API_URL } from '../../config';

interface Article {
    contents: string;
}

export const About = () => {
    const [article, setArticle] = useState<Article | null>(null);

    useEffect(() => {
        fetch(`${API_URL}/app/article/articles/1`, {
            method: "GET",
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
        })
            .then((res) => res.json())
            .then((article: Article) => {
                setArticle(article);
            })
            .catch((error) => console.error('Error:', error)
            );
    }, []);

    return (
        <div className="container">
            <ul>
                <br /><br />
                <br /><br />
                {article?.contents.split('\n').map((line, i) => (
                    <p key={i}>{line}</p>
                ))}
                <br />
                <p className="date">2024/05/20</p>
            </ul>
        </div>
    );
};
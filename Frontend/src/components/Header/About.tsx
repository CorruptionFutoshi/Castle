import React, { useState, useEffect } from "react";

interface Article {
    contents: string;
}

export const About = () => {
    const [article, setArticle] = useState<Article | null>(null);

    useEffect(() => {
        fetch(`https://api.catsle.net/app/article/articles/8`, {
            method: "GET",
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
        })
            .then((res) => res.json())
            .then((article: Article) => {
                article.contents = article.contents.replace(/\\n/g, '<br />');
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
                <p className="date">2024/01/28</p>
            </ul>
        </div>
    );
};
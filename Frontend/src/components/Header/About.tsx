import React, { useState, useEffect } from "react";

interface Article {
    contents: string;
}

export const About = () => {
    const [article, setArticle] = useState<Article | null>(null);

    useEffect(() => {
        fetch(`http://localhost:8080/app/article/articles/1`, { method: "GET" })
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
                <p dangerouslySetInnerHTML={{ __html: article?.contents || "" }}></p>
                <p className="date">2024/01/28</p>
            </ul>
        </div>
    );
};
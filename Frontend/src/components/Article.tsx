import moment from "moment";
import React, { useState, useEffect, FC } from "react";
import { Link, useParams } from "react-router-dom";

interface Article {
    title: string;
    createDate: string;
    tag: string;
    contents: string;
    [key: string]: any;
}

export const Article = () => {
    const [article, setArticle] = useState<Article | null>(null);
    const { id } = useParams<{ id: string }>();

    useEffect(() => {
        fetch(`http://localhost:8080/app/article/articles/${id}`, { method: "GET" })
            .then((res) => res.json())
            .then((article: Article) => {
                article.contents = article.contents.replace(/\\n/g, '<br />');
                setArticle(article);
            })
            .catch((error) => console.error('Error:', error)
            );
    }, [id]);

    return (
        <div className="container">
            <ul>
                <br /><br />
                <h2>{article?.title}</h2>
                <div className="dateAndTagArticle">{moment(article?.createDate).format('YYYY/MM/DD')}</div>
                <div className="dateAndTagArticle"><Link to={`/tags/${article?.tag}`}>{article?.tag}</Link></div>
                <p dangerouslySetInnerHTML={{ __html: article?.contents || "" }}></p>
            </ul>
        </div>
    );
};

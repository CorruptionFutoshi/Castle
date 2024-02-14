import moment from "moment";
import React, { useState, useEffect, FC } from "react";
import { Link, useParams } from "react-router-dom";

interface Article {
    id: number;
    title: string;
    createDate: string;
    tag: string;
    summary: string;
    [key: string]: any;
}

interface ParamTypes {
    searchText: string;
}

export const ArticlesBySearch: FC = () => {
    const [articles, setArticles] = useState<Article[]>([]);
    const { searchText } = useParams<{ searchText: string }>();

    useEffect(() => {
        fetch(`http://localhost:8080/app/article/search/${searchText}`, { method: "GET" })
            .then((res) => res.json())
            .then((articles: Article[]) => setArticles(articles.filter(article => article.id !== 1).reverse()))
            .catch((error) => console.error('Error:', error)
            );
    }, [searchText]);

    return (
        <div className="homeContainer">
            {articles.map((article, index) => (
                <ul key={index} className="homeArticle">
                    <div className="titleLink"><Link to={`/articles/${article.id}`}>{article.title}</Link></div>
                    <p>{article.summary}</p>
                    <div className="dateAndTag">{moment(article.createDate).format('YYYY/MM/DD')}　</div>
                    <div className="dateAndTag"><Link to={`/tags/${article.tag}`}>{article.tag}</Link></div>
                </ul>
            ))}
        </div>
    );
};

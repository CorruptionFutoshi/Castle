import moment from "moment";
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import { API_URL } from '../config';

interface Article {
    id: number;
    title: string;
    createDate: string;
    tag: string;
    summary: string;
}

export const ArticlesByTag = () => {
    const [articles, setArticles] = useState<Article[]>([]);
    const { tag } = useParams<{ tag: string }>();

    useEffect(() => {
        let encodedTag = "";

        if (tag) {
            encodedTag = encodeURIComponent(tag);
        }

        fetch(`${API_URL}/app/article/articles/${encodedTag}`, {
            method: "GET",
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
        })
            .then((res) => res.json())
            .then((articles: Article[]) => setArticles(articles.filter(article => article.id !== 1).reverse()))
            .catch((error) => console.error('Error:', error)
            );
    }, [tag]);

    return (
        <div className="homeContainer">
            {articles.map((article, index) => (
                <ul key={index} className="homeArticle">
                    <div className="titleLink"><Link to={`/articles/${article.id}`}>{article.title}</Link></div>
                    <p>{article.summary}</p>
                    <div className="dateAndTag">{moment(new Date(article.createDate).toISOString()).format('YYYY/MM/DD')}　</div>
                    <div className="dateAndTag"><Link to={`/tags/${article.tag}`}>{article.tag}</Link></div>
                </ul>
            ))}
        </div>
    );
};

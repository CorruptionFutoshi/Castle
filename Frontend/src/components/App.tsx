import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import moment from 'moment';
import './Main.css';

interface Article {
  id: number;
  title: string;
  summary: string;
  createDate: string;
  tag: string;
}

export const App = () => {
  const [articles, setArticles] = useState<Article[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/articles/home", { method: "GET" })
      .then((res) => res.json())
      .then((articles: Article[]) => setArticles(articles.filter(article => article.id !== 1).reverse()))
      .catch((error) => console.error('Error:', error)
      );
  }, []);

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
import React, { useState, useEffect, FC } from "react";
import { Link } from "react-router-dom";

export const Tags: FC = () => {
  const [tags, setTags] = useState<string[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/app/article/tags", { method: "GET" })
      .then((res) => res.json())
      .then((tags: string[]) => setTags(tags.filter(tag => tag !== "ブログ")))
      .catch((error) => console.error('Error:', error)
      );
  }, []);

  return (
    <div className="container">
      <br /><br />
      {tags.map((tag, index) => (
        <ul key={index}>
          <p className="tagLink"><Link to={`/tags/${tag}`}>{tag}</Link></p>
        </ul>
      ))}
    </div>
  );
};

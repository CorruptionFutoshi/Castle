import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

export const Tags = () => {
  const [tags, setTags] = useState<string[]>([]);

  useEffect(() => {
    fetch("https://api.catsle.net/app/article/tags/", {
      method: "GET",
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
      },
    })
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

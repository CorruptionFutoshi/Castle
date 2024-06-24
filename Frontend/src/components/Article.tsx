import moment from "moment";
import { useState, useEffect, useContext } from "react";
import { Link, useParams } from "react-router-dom";
import { LoginContext } from "../index";
import { API_URL } from '../config';
import ReactMarkdown from "react-markdown";

interface Article {
    title: string;
    createDate: string;
    tag: string;
    contents: string;
}

interface Comment {
    username: string;
    contents: string;
    createDate: string;
}

export const Article = () => {
    const [article, setArticle] = useState<Article | null>(null);
    const [comments, setComments] = useState<Comment[]>([]);
    const [newComment, setNewComment] = useState("");
    const { id } = useParams<{ id: string }>();
    const loginContext = useContext(LoginContext);
    let hasSessionId: boolean = false;

    if (loginContext) {
        ({ hasSessionId } = loginContext);
    }

    useEffect(() => {
        if (id) {
            if (!/^\d+$/.test(id)) {
                console.error('Invalid ID:', id);
                return;
            }
        }

        fetch(`${API_URL}/app/article/articles/${id}`, {
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

        fetch(`${API_URL}/app/comment/${id}`, {
            method: "GET",
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
            },
        })
            .then((res) => res.json())
            .then((comments: Comment[]) => {
                setComments(comments);
            })
            .catch((error) => console.error('Error:', error)
            );
    }, [id]);

    const createComment = () => {
        if (id) {
            if (!/^\d+$/.test(id)) {
                console.error('Invalid ID:', id);
                return;
            }
        }

        fetch(`${API_URL}/app/comment/${id}`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest',
            },
            body: JSON.stringify({ contents: newComment }),
            credentials: 'include'
        })
            .then((res) => res.json())
            .then((comments: Comment[]) => {
                setComments(comments);
                setNewComment("");
            })
            .catch((error) => console.error('Error:', error));
    };

    return (
        <div className="container">
            <ul>
                <br /><br />
                {article && (
                    <>
                        <h2 className="title">{article.title}</h2>
                        <div className="dateAndTagArticle">{moment(new Date(article.createDate).toISOString()).format('YYYY/MM/DD')}</div>
                        <div className="dateAndTagArticle"><Link to={`/tags/${article.tag}`}>{article.tag}</Link></div>
                        <ReactMarkdown>{article.contents}</ReactMarkdown>
                    </>
                )}
            </ul><br /><br />
            <h3>コメント</h3>
            {comments.map((comment, index) => (
                <ul key={index} className="comment">
                    <p>{index + 1} {comment.username}</p>
                    {comment.contents.split('\n').map((line, i) => (
                        <p key={i}>{line}</p>
                    ))}
                    <div className="dateAndTag">{moment(new Date(comment.createDate).toISOString()).format('YYYY/MM/DD')}　</div>
                    {hasSessionId &&
                        <button className="replyButton" onClick={() => { setNewComment(">>" + (index + 1) + "\n" + newComment) }}>返信</button>
                    }
                </ul>
            ))}
            {hasSessionId &&
                <div className="commentForm">
                    <textarea className="commentTextArea" value={newComment} onChange={(e) => setNewComment(e.target.value)} />
                    <button className="submitCommentButton" onClick={createComment}>コメントを投稿</button>
                </div>}
        </div>
    );
};

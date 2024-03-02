import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';
import { App } from './components/App';
import { Article } from './components/Article';
import { Header } from './components/Header/Header';
import { About } from './components/Header/About';
import { Tags } from './components/Header/Tags';
import {Signin} from './components/Header/Signin' 
import {Signup} from './components/Header/Signup' 
import { ArticlesByTag } from './components/ArticlesByTag';
import { ArticlesBySearch } from './components/ArticlesBySearch';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
  <BrowserRouter>
    <Header></Header>
    <Routes>
      <Route path={`/`} element={<App />} />
      <Route path={`/articles/:id`} element={<Article />} />
      <Route path={`/tags/:tag`} element={<ArticlesByTag />} />
      <Route path={`/search/:searchText`} element={<ArticlesBySearch />} />
      <Route path={`/about`} element={<About />} />
      <Route path={`/tags`} element={<Tags />} />
      <Route path={`/signup`} element={<Signup />} />
      <Route path={`/signin`} element={<Signin />} />
    </Routes>
  </BrowserRouter>
);
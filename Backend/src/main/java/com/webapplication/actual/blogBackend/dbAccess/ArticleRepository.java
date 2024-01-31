package com.webapplication.actual.blogBackend.dbAccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapplication.actual.blogBackend.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
	List<Article> findByTag(String tag);

	List<Article> findByTitleContainingOrSummaryContainingOrContentsContaining(String title, String summary,
			String contents);
}

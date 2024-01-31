package com.webapplication.actual.blogBackend;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.actual.blogBackend.dbAccess.ArticleRepository;

@CrossOrigin(origins = "*")
@RestController
public class BlogController {
	private ArticleRepository articleRepository;

	public BlogController(ArticleRepository articleRepository) {
		super();
		this.articleRepository = articleRepository;
	}

	@GetMapping("/articles")
	public List<Article> getArtticles() {
		return articleRepository.findAll();
	}

	@GetMapping("/articles/tag/{tag}")
	public List<Article> getArtticlesByTag(@PathVariable String tag) {
		return articleRepository.findByTag(tag);
	}

	@GetMapping("/articles/{id}")
	public Article getArtticleById(@PathVariable int id) {
		return articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
	}

	@GetMapping("/articles/tags")
	@ResponseBody
	public List<String> getTags() {
		return articleRepository.findAll().stream().map(article -> article.getTag()).distinct()
				.collect(Collectors.toList());
	}

	@GetMapping("/articles/home")
	public List<ArticleForHome> getArticlesForHome() {
		return articleRepository
				.findAll().stream().map(article -> new ArticleForHome(article.getId(), article.getCreateDate(),
						article.getTitle(), article.getSummary(), article.getTag()))
				.distinct().collect(Collectors.toList());
	}

	@GetMapping("/articles/search/{keyword}")
	public List<Article> searchArticles(@PathVariable String keyword) {
		return articleRepository.findByTitleContainingOrSummaryContainingOrContentsContaining(keyword, keyword,
				keyword);
	}
}

package springbootdeveloper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springbootdeveloper.domain.Article;

public interface BlogRepository extends JpaRepository<Article,Long> {}

package springbootdeveloper.dto;

import lombok.Getter;
import springbootdeveloper.domain.Article;

@Getter
// 블로그 글 목록 뷰 구현을 위한 객체 생성.
public class ArticleListViewResponse {
    private Long id;
    private String title;
    private String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}

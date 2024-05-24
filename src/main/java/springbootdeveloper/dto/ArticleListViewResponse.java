package springbootdeveloper.dto;

import lombok.Getter;
import lombok.ToString;
import springbootdeveloper.domain.Article;

@Getter
@ToString
// 블로그 글 목록 뷰 구현을 위한 객체 생성.
public class ArticleListViewResponse {
    private Long ano;
    private String title;
    private String content;

    public ArticleListViewResponse(Article article) {
        this.ano = article.getAno();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}

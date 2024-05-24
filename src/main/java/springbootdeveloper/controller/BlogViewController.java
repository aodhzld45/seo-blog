package springbootdeveloper.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import springbootdeveloper.domain.Article;
import springbootdeveloper.dto.ArticleListViewResponse;
import springbootdeveloper.dto.ArticleViewResponse;
import springbootdeveloper.service.BlogService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BlogViewController {

    @Autowired
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) // View로 데이터를 넘겨주는 Model 객체 생성.
    {
        List<ArticleListViewResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles); // 블로그 글 리스트 저장

        log.info("articles = {} ", articles.toString());
        return "articleList"; // articleList.html 조회
    }

    @GetMapping("/articles/{ano}")
    public String getArticle(@PathVariable long ano, Model model){
        Article article = blogService.findById(ano);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";

    }

//  새로운 글 수정 및 생성.
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long ano, Model model) {
        System.out.println("#### 현재 id의 상태는 : " + ano);
        if (ano == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(ano);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
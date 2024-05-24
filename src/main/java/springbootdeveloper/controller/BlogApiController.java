package springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springbootdeveloper.domain.Article;
import springbootdeveloper.dto.AddArticleRequest;
import springbootdeveloper.dto.ArticleResponse;
import springbootdeveloper.dto.UpdateArticleRequest;
import springbootdeveloper.service.BlogService;

import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형태로 반환하는 컨트롤러
public class BlogApiController {
    @Autowired
    private BlogService blogService;
    //HTTP 메서드가 POST일 때 전달 받은 URL과 동일하면 메서드로 매핑
    
//  블로그 글 작성 API
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        //요청한 자원이 성공적으로 생성되었으며, 저장된 블로그 글 정보를 응답 객체에 담아 전송.
        return ResponseEntity.status(HttpStatus.CREATED) // Response 응답 : 201 요청이 성공적으로 수행되었고, 새로운 Resource가 생성되었음.
                .body(savedArticle);
    }

// 블로그 글 조회 (전체) API
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);

    }
    
// 블로그 글 조회 (글 상세) API
    @GetMapping("/api/articles/{ano}")
    //@PathVariable URI값에 가변형 변수를 전달해서 처리하는 방식
    public ResponseEntity<ArticleResponse> findById(@PathVariable long ano) {
        Article articles = blogService.findById(ano);
        return ResponseEntity.ok()
                .body(new ArticleResponse(articles));
    }

// 블로그 글 삭제 API
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long ano) {
        blogService.delete(ano);

        return ResponseEntity.ok()
                .build();

    }

//  블로그 글 수정 API
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long ano,
                                                 @RequestBody UpdateArticleRequest request){
        Article updateArticle = blogService.update(ano, request);

        return ResponseEntity.ok()
                .body(updateArticle);

    }
}
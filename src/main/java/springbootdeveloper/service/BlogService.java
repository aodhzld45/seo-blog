package springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springbootdeveloper.domain.Article;
import springbootdeveloper.dto.AddArticleRequest;
import springbootdeveloper.dto.UpdateArticleRequest;
import springbootdeveloper.repository.BlogRepository;

import java.util.List;

@Service // Service 빈으로 등록
@RequiredArgsConstructor // final이 붙거나, @NotNull이 붙은 필드의 생성자 추가.
public class BlogService {
    @Autowired
    private final BlogRepository blogRepository;

    // 블로그 글 추가(작성) 메서드
    // save메서드는 JpaRepository에서 지원하는 메서드로 AddArtcleRequest 클래스에서 저장된 값들을 artcle DB에 저장
    public Article save(AddArticleRequest request){
        // 객체 정보를 Return함.
        // 실패한 케이스를 예외처리로 추가해야함. (편의성)
        return blogRepository.save(request.toEntity()); // save는 사용자가 직접 생성한 메서드가 아니라 blogRepository 의 Jpa가 생성한 메서드
    }

    // 블로그 글 조회(전체) 메서드
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    // 블로그 글 조회(글 하나 상세) 메서드
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id));
    }

    // 블로그 글 삭제 메서드
    public void delete(long id){
        blogRepository.deleteById(id);
    }

    // 블로그 글 수정 메서드
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        article.update(request.getTitle(),request.getContent());

        return article;

    }



}

package springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springbootdeveloper.domain.Article;
import springbootdeveloper.dto.AddArticleRequest;
import springbootdeveloper.dto.UpdateArticleRequest;
import springbootdeveloper.repository.BlogRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc // MocMvc생성
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 메서드

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }
//  PostMapping("/api/articles")
    @DisplayName("addArticle: 아티클 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(/*id,*/title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);
        System.out.println("################requestBody");
        System.out.println(requestBody); // Json 형태
        System.out.println("################requestBody");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }
//  GetMapping("/api/articles")
    @DisplayName("findAllArticles: 블로그 글 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        //given - 블로그 글을 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());
        //when - 목록 조회 API를 호출
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //Then - 응답 코드가 200 OK이고, 반환받은 값 중에 0번째 요소의 content와 title이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

//  GetMapping("/api/articles/{id}")
    @DisplayName("findById : 블로그 글 상세보기에 성공한다.")
    @Test
    public void findById() throws Exception {
//      given - 블로그 글을 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

//      when - 저장한 블로그 글의 id 값으로 API를 호출한다.
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

//      then - 응답 코드가 200 ok이고, 반환받은 content와 title이 저장된 값과 같은지 확인.
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    //DeleteMapping("/api/articles/{id}")
    @DisplayName("deleteArticles : 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticles() throws Exception{
//      given - 블로그 글을 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

//        when - 저장한 블로그 글의 id 값으로 삭제 API를 호출한다.
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());
//        then - 응답 코드가 200 Ok 이고, 블로그 글 리스트를 전체 조회해 조회한 배열 크기가 0인지 확인
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

// PutMapping("/api/articles/{id}")
    @DisplayName("updateArticle : 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception{
        // given - 블로그 글을 저장하고, 글 수정에 필요한 객체를 생성한다.
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle = "new Title";
        final String newContent = "new Content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when - Update Api로 수정 요청을 보냄, 이 때 요청 Type은 JSON이며 given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보냄
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then - 응답 코드가 200인지 확인, 블로그 글 Id로 조회한 후에 값이 수정되었는지 확인.
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }



}
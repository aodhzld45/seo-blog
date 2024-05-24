package springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "article")
@Entity // 엔티티로 지정
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED )
@SequenceGenerator(
        name="ARTICLE_SEQ_GEN", //시퀀스 제너레이터이름
        sequenceName="article_seq", //시퀀스 이름
        initialValue=1,
        allocationSize=1
)

public class Article {
    @Id //id 필드를 기본키로 지정
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq_generator")
//    @SequenceGenerator(name = "article_seq_generator", sequenceName = "article_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                        generator = "ARTICLE_SEQ_GEN") // 기본키 자동으로 1증가
    @Column(name = "ano", updatable = false)
    private Long ano;

    @Column(name = "title", nullable = false) // 'title'이라는 not null 컬럼과 매핑
    private String title;
    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Builder // 빌더 패턴으로 객체 생성 -> 생성자 위에 입력하면, 빌더 패턴 방식으로 객체를 생성.
    public Article(/*Long id,*/  String title, String content) {
//        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Update를 위한 Method 추가
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    protected Article() {} //기본 생성자

//    public Long getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContent() {
//        return content;
//    }
}

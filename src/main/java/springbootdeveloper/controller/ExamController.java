package springbootdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExamController {
    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){ // View로 데이터를 넘겨주는 Model 객체 생성
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(20);
        examplePerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examplePerson); // Person 객체 생성
        model.addAttribute("today", LocalDate.now());

        return "example"; //example.html파일 매핑
    }

    @Getter
    @Setter
    class Person{
        private long id;
        private String name;
        private int age;
        private List<String> hobbies;

    }




}

package hello.springmvc.basic;


import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelloData implements Serializable {

    private String username;
    private int age;


    public HelloData(String username, int age) {
        this.username = username;
        this.age = age;
    }
}

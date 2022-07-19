import com.iscas.pm.auth.utils.BCrypt;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * created By lichang on
 */
@SpringBootTest
@Transactional()
public class codetest {
@Test
public  void Test1(){
    System.out.println(new BCryptPasswordEncoder().encode("22"));
    System.out.println(BCrypt.checkpw("22", "$2a$10$h1qDDjYOoiRnRgTQGV.byugpY3ig607NXxNxTnNoKObiW/4oqNF.y"));
}
}


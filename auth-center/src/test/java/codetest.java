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
    System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    System.out.println(BCrypt.checkpw("pmservice", "pmservice"));
}
}


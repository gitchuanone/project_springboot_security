import com.auth0.jwt.interfaces.Claim;
import com.jwt.JwtApplicationRun;
import com.jwt.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author yangchuan
 * @date 2023/3/24
 */
@SpringBootTest(classes = {JwtApplicationRun.class})
public class JwtTest {
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void jwtTest01() {
        try {
            String token = jwtUtils.createToken("12345", "wangbo");
            System.out.println("token=" + token);
            //Thread.sleep(5000);
            System.out.println("=======>verifyToken");
            Map<String, Claim> verifyToken = jwtUtils.verifyToken(token);
            for (Map.Entry<String, Claim> entry : verifyToken.entrySet()){
                if (entry.getValue().asString() != null){
                    System.out.println(entry.getKey() + "===" + entry.getValue().asString());
                }else {
                    System.out.println(entry.getKey() + "===" + entry.getValue().asDate());
                }
            }
            System.out.println("=======>parseToken");
            Map<String, Claim> parseToken = jwtUtils.parseToken(token);
            for (Map.Entry<String, Claim> entry : parseToken.entrySet()){
                if (entry.getValue().asString() != null){
                    System.out.println(entry.getKey() + "===" + entry.getValue().asString());
                }else {
                    System.out.println(entry.getKey() + "===" + entry.getValue().asDate());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}

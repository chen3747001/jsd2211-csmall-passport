package cn.tedu.csmall.passport;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@SpringBootTest
public class JwtTests {
    @Value("${csmall.jwt.secret-key}")
    String secretKey;
    @Test
    public void testGenerate(){
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",12);
        claims.put("username","zhuzhu");
        claims.put("password","i am bad pig");

        //盐值

        //过期时间
        Date expirationDate=new Date(System.currentTimeMillis()+100*60*1000);
        System.out.println("过期时间："+expirationDate);

        String jwt = Jwts.builder()
                /* Header*/
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload
                /*数据*/
                .setClaims(claims)
                //过期时间
                .setExpiration(expirationDate)
                /* Signature */
                .signWith(SignatureAlgorithm.HS256, secretKey)
                /* 整合 */
                .compact();

        //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6ImkgYW0gYmFkIHBpZyIsImV4cCI6MTY4MDYwMTQzNywidXNlcm5hbWUiOiJ6aHV6aHUifQ.y3owzwiUYNBhYVKdSvbIGGLzcPlFyiSkFX2opz5RQRQ
        System.out.println(jwt);
    }

    @Test
    public void testParse(){
        String jwt="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6ImkgYW0gYmFkIHBpZyIsImlkIjoxMiwiZXhwIjoxNjgwNjA3Nzg0LCJ1c2VybmFtZSI6InpodXpodSJ9.0_e1eo7ka4UHyTST0_BR4ZLstmlxmCF37ZzYJ52GfyY";
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        Integer id=claims.get("id",Integer.class);
        System.out.println("id is:"+id);
        String username=claims.get("username",String.class);
        System.out.println("username is:"+username);
        String password=claims.get("password",String.class);
        System.out.println("password is:"+password);


    }
}

package cn.tedu.csmall.passport.filter;

import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.web.JsonResult;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT认证过滤器
 * Spring Security框架会自动从SecurityContext读取认证信息，如果存在有效信息，则视为已登录，否则，视为未登录
 * 前过滤器应该尝试解析客户端可能携带的JWT，如果解析成功，则创建对应的认证信息，并存储到SecurityContext中
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    public static final int JWT_MIN_LENGTH = 100;
    @Value("${csmall.jwt.secret-key}")
    String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*清除将认证信息存储到SecurityContext中的jwt(认证信息)*/
        SecurityContextHolder.clearContext();

        /*尝试获取客户端提交的可能携带的JWT*/
        String jwt=request.getHeader("Authorization");
        log.debug("接收到jwt数据：{}",jwt);

        /*判断是否获取到有效的JWT*/
        if(!StringUtils.hasText(jwt) || jwt.length()<JWT_MIN_LENGTH){
            //直接放行
            log.debug("未获取到有效的JWT数据，将放行");
            filterChain.doFilter(request,response);
            return;
        }

        /*设置响应的文档类型*/
        response.setContentType("application/json; charset=utf-8");

        /*尝试解析JWT，从中获取用户的相关数据，例如id，username*/
        log.debug("尝试解析JWT");
        Claims claims=null;
        /*由于过滤器是最先接受到请求（在SpringMVC之前），所以异常处理类无法处理过滤器抛出的异常，需要自己try，catch处理*/
        //判断输入jwt是否正确，如果错误则返回异常
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        }catch (SignatureException e){
            //密钥错误或者jwt数据错误
            String message="非法访问";
            log.debug("SignatureException: 密钥错误或者jwt数据错误");
            printCatchException(response,ServiceCode.ERR_JWT_SIGNATURE,message);
            return;
        }catch (MalformedJwtException e){
            //jwt格式错误
            String message="非法访问";
            log.debug("MalformedJwtException: jwt格式错误");
            printCatchException(response,ServiceCode.ERR_JWT_MALFORMED,message);
            return;
        }catch (ExpiredJwtException e){
            //jwt过期
            String message="登录已过期，请重新登录";
            log.debug("ExpiredJwtException: 登录过期，请重新登录");
            printCatchException(response,ServiceCode.ERR_JWT_EXPIRED,message);
            return;
        }catch (Throwable e){
            //兜底的异常处理，防止出现白屏
            e.printStackTrace();

            String message="服务器忙，请稍后再试";
            log.debug("验证jwt时发生未知错误，稍后再试");
            printCatchException(response,ServiceCode.ERR_UNKNOWN,message);
            return;
        }

        Long id=claims.get("id",Long.class);
        String username=claims.get("username",String.class);
        String tempAuthoritiesList=claims.get("authorities",String.class);
        //使用JSON 将封装为String的权限列表重新转换为List<SimpleGrantedAuthority>格式
        List<SimpleGrantedAuthority> authorities = JSON.parseArray(tempAuthoritiesList, SimpleGrantedAuthority.class);

        log.debug("jwt-id is: {}",id);
        log.debug("jwt-username is: {}",username);
        log.info("tempAuthoritiesList={}",tempAuthoritiesList);
        log.info("tempAuthoritiesList数据类型={}",tempAuthoritiesList.getClass().getName());
//        log.info("tempAuthoritiesList元素数据类型={}",((ArrayList)tempAuthoritiesList).get(0).getClass().getName());

        /*将根据从JWT中解析得到数据来创建认证信息*/
        //设置当事人
        LoginPrincipal loginPrincipal=new LoginPrincipal();
        loginPrincipal.setId(id);
        loginPrincipal.setUsername(username);
        //设置权限

        Authentication authentication=new UsernamePasswordAuthenticationToken(loginPrincipal,null,authorities);

        /*将认证信息存储到SecurityContext中*/
        log.debug("即将向SecurityContext中存入认证信息：{}",authentication);
        SecurityContext securityContext= SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        /*放行*/
        filterChain.doFilter(request,response);
    }

    private void printCatchException(HttpServletResponse response,ServiceCode serviceCode,String message) throws IOException {
        PrintWriter printWriter=response.getWriter();
        JsonResult<Void> jsonResult=JsonResult.fail(serviceCode,message);
        String jsonResultString= JSON.toJSONString(jsonResult);
        printWriter.write(jsonResultString);
        printWriter.close();
    }
}

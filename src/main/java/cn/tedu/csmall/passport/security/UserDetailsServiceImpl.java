package cn.tedu.csmall.passport.security;

import cn.tedu.csmall.passport.filter.JwtAuthorizationFilter;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.pojo.vo.AdminLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AdminMapper mapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
        log.debug("Spring Security 调用了 loadUserByUsername()方法，参数：{}",s);

        AdminLoginInfoVO loginInfo=mapper.getLoginInfoByUsername(s);
        log.debug("从数据库查询与用户名【{}】匹配的管理员信息：{}",s,loginInfo);

        if(loginInfo==null){
            String message="登录失败，该用户名的用户不存在";
            log.debug(message);
            throw new BadCredentialsException(message);
        }

        //该名名称对应的管理员确实存在时，代码才会执行到此处
        List<GrantedAuthority> authorities=new ArrayList<>();
        for(String permission : loginInfo.getPermissions()){
            GrantedAuthority grantedAuthority=new SimpleGrantedAuthority(permission);
            authorities.add(grantedAuthority);
        }

        AdminDetails adminDetails=new AdminDetails(
                loginInfo.getUsername(), loginInfo.getPassword(),
                loginInfo.getEnable()==1,authorities
        );
        adminDetails.setId(loginInfo.getId());

//        UserDetails userDetails = User.builder()
//                .username(loginInfo.getUsername())
//                .password(loginInfo.getPassword())
//                .accountExpired(false)
//                .accountLocked(false)
//                .disabled(loginInfo.getEnable()==0)
//                .authorities("这是一个山寨的权限标识") // 权限，注意，此方法的参数不可以为null，在不处理权限之前，可以写一个随意的字符串值
//                .build();
        log.debug("即将向Spring Security返回adminDetails对象：{}", adminDetails);
        return adminDetails;
    }
}

package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.mapper.AdminRoleMapper;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import cn.tedu.csmall.passport.security.AdminDetails;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    AdminMapper mapper;
    @Autowired
    AdminRoleMapper adminRoleMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${csmall.jwt.duration-in-minute}")
    Long durationInMinute;

    /**
     * 管理员登录
     * @param dto 封装了管理员的登录信息
     * @return JWT数据
     */
    @Override
    public String login(AdminLoginDTO dto){
        log.info("impl-开始处理【管理员登录】的请求，参数：{}",dto);

        /*调用AuthenticationManager对象的authenticate()方法处理认证*/
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                dto.getUsername(), dto.getPassword());
        Authentication authenticateResult = authenticationManager.authenticate(authentication);
        log.debug("管理员登录执行认证成功,认证结果：{}",authenticateResult);
        Object principal=authenticateResult.getPrincipal();
        log.debug("认证结果中的Principle数据类型：{}",principal.getClass().getName());
        log.debug("认证结果中的Principle数据：{}",principal);
        AdminDetails adminDetails= (AdminDetails) principal;

        /*生成JWT数据*/
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",adminDetails.getId());
        claims.put("username",adminDetails.getUsername());
        claims.put("authorities", JSON.toJSONString(adminDetails.getAuthorities()));

        Date expirationDate=new Date(System.currentTimeMillis()+durationInMinute*60*1000);
        String secretKey="sd>/fd{}s1:2S_S+DF213sad12sas12ca2,2";
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
        //返回JWT数据
        log.debug("JWT is: {}",jwt);
        return jwt;
    }

    /**
     * 添加新管理员
     * @param dto 管理员的数据
     */
    @Override
    public void addNew(AdminAddNewDTO dto) {
        String username=dto.getUsername();
        String email=dto.getEmail();
        String phone=dto.getPhone();

        /*检查添加管理员时角色列表是否合理*/
        log.debug("检查选择的角色是否合适");
        long[] postRoleIds=dto.getRoleIds();
        for (int i = 0; i < postRoleIds.length; i++) {
            if(postRoleIds[i]==1){
                String message="添加管理员失败，非法添加角色列表";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_INSERT,message);
            }
        }

        int countByUserName = mapper.countByUserName(username);
        int countByEmail = mapper.countByEmail(email);
        int countByPhone = mapper.countByPhone(phone);

        if(countByUserName!=0){
            throw new ServiceException(ServiceCode.ERR_CONFLICT,"用户名为： 【"+username+"】 的数据已经存在");
        }
        if(countByPhone!=0){
            throw new ServiceException(ServiceCode.ERR_CONFLICT,"电话号码为： 【"+phone+"】 的数据已经存在");
        }
        if(countByEmail!=0){
            throw new ServiceException(ServiceCode.ERR_CONFLICT,"电子邮箱为： 【"+email+"】 的数据已经存在");
        }

        /*满足以上条件后再执行插入操作*/
        Admin admin=new Admin();
        BeanUtils.copyProperties(dto,admin);
        //密码加密
        String rawPassword=admin.getPassword();
        String encodePassword=passwordEncoder.encode(rawPassword);
        admin.setPassword(encodePassword);

        //补全admin中的属性值 loginCount=0
        admin.setLoginCount(0);
        int insertCount = mapper.insert(admin);
        //判断插入操作是否成功
        if(insertCount != 1){
            String message="添加管理员失败，服务器忙，请再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT,message);
        }
        log.debug("impl-新建管理员成功");

        //调用adminRoleMapper的insertBatch()方法插入相关数据
        long[] roleIds = dto.getRoleIds();
        List<AdminRole> list=new ArrayList<>();
        for (int i = 0; i < roleIds.length; i++) {
            AdminRole adminRole=new AdminRole();
            /*插入数据后，此处id值已经存在*/
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(roleIds[i]);
            list.add(adminRole);
        }
        insertCount=adminRoleMapper.insertBatch(list);

        /*判断插入用户和角色数据是否成功*/
        if(insertCount!=roleIds.length){
            String message="添加管理员失败，服务器忙，请再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT,message);
        }
        }

    /**
     * 根据id删除用户数据
     *
     * @param id 用户id
     * @return
     */
    @Override
    public int deleteById(long id){
        log.info("impl-删除用户，id是：{}",id);

        //判断管理员id是否为1
        if(id==1){
            String message="删除管理员失败，该管理员不存在";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        AdminStandardVO standardVO = mapper.getStandardById(id);
        if (standardVO == null) {
            String message="删除用户失败，id为 "+id+" 的用户不存在";
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        int rows = mapper.deleteById(id);
        if(rows!=1){
            String message="删除管理员失败，服务器忙，请再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_DELETE,message);
        }

        /*执行删除此管理员与角色的关联数据*/
        rows = adminRoleMapper.deleteByAdminId(id);
        if(rows < 1){
            String message="删除管理员失败，服务器忙，请再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_DELETE,message);
        }
        return rows;
    }

    /**
     * 设置该管理员是否启用
     * @param id 管理员的id
     * @param enable 是否启用的状态码 0禁用 1启用
     */
    private void updateEnableById(Long id,Integer enable){
        //String[] tip={"禁用","启用"};
        String tip= enable==0 ? "禁用" : "启用";
        log.info("开始修改id为[{}]的管理员的[{}]操作",id,tip);

        //判断管理员id是否为1
        if(id==1){
            String message=tip+"管理员失败，该管理员不存在";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        AdminStandardVO vo=mapper.getStandardById(id);
        //判断该id的管理员是否存在
        if (vo == null) {
            String message="id为 "+id+" 的管理员不存在，修改启用操作失败";
            log.info(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        //判断状态是否冲突,当前已经是目标状态
        if(vo.getEnable()==enable){
            String message=tip+"管理员失败，管理员已经处于"+tip+"状态";
            log.info(message);
            throw new ServiceException(ServiceCode.ERR_CONFLICT,message);
        }

        //修改启用状态
        Admin admin=new Admin();
        admin.setId(id);
        admin.setEnable(enable);
        int rows = mapper.updateById(admin);
        //判断是否修改成功
        if(rows!=1){
            String message="修改管理员启用状态失败，服务器忙，请再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE,message);
        }
    }

    @Override
    public void setEnable(Long id) {
        updateEnableById(id,1);
    }

    @Override
    public void setDisable(Long id) {
        updateEnableById(id,0);
    }

    /**
     * 查询管理员列表
     * @return 管理员列表
     */
    public List<AdminListItemVO> list(){
        //查询到数据
        List<AdminListItemVO> list=mapper.list();
        //删除list中id=1的数据
        Iterator<AdminListItemVO> iterator = list.iterator();
        while(iterator.hasNext()){
            AdminListItemVO item=iterator.next();
            if(item.getId()==1){
                iterator.remove();
                break;
            }
        }
        //返回数据
        return list;
    }
}

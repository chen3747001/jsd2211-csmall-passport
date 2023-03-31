package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.mapper.AdminRoleMapper;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.ServiceCode;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    AdminMapper mapper;
    @Autowired
    AdminRoleMapper adminRoleMapper;

    /**
     * 添加新管理员
     * @param dto 管理员的数据
     */
    @Override
    public void addNew(AdminAddNewDTO dto) {
        String username=dto.getUsername();
        String email=dto.getEmail();
        String phone=dto.getPhone();

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
        //TODO 密码加密
        Admin admin=new Admin();
        BeanUtils.copyProperties(dto,admin);
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
        //判断插入用户和角色数据是否成功
        if(insertCount!=roleIds.length){
            String message="添加管理员失败，服务器忙，请再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT,message);
        }
    }

    /**
     * 根据id删除用户数据
     * @param id 用户id
     */
    @Override
    public void deleteById(long id){
        log.info("impl-删除用户，id是：{}",id);
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
    }

    private void updateEnableById(Long id,Integer enable){
        //String[] tip={"禁用","启用"};
        String tip= enable==0 ? "禁用" : "启用";
        log.info("开始修改id为[{}]的管理员的[{}]操作",id,tip);
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
        return mapper.list();
    }
}

package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import cn.tedu.csmall.passport.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class AdminTests {
    @Autowired
    AdminMapper mapper;
    @Autowired
    IAdminService service;

    @Test
    void testInsert(){
        Admin admin=new Admin();
        admin.setAvatar("test chen");
        int i = mapper.insert(admin);
        log.info("受影响的行数是：{}",i);
    }

    @Test
    void testAddNew(){
        AdminAddNewDTO dto=new AdminAddNewDTO();
        dto.setUsername("zhuzhu");
        dto.setEmail("zhuzhu@qq.com");
        dto.setPhone("182");

        try{
            service.addNew(dto);
        }catch (ServiceException e){
            log.warn("test-{}",e.getMessage());
        }
    }

    @Test
    void testAddNewWithRoles(){
        AdminAddNewDTO dto=new AdminAddNewDTO();
        dto.setUsername("zhuzhuchen");
        dto.setEmail("zhuzhuchen@qq.com");
        dto.setPhone("1822");
        dto.setRoleIds(new long[]{3L,5L});

        try{
            service.addNew(dto);
        }catch (ServiceException e){
            log.warn("test-{}",e.getMessage());
        }
    }



    @Test
    void testDeleteById(){
        Long id=13L;
        int i = mapper.deleteById(id);
        log.info("受影响的行数是：{}",i);
    }

    @Test
    void testUpdateById(){
        Admin admin=new Admin();
        admin.setId(18L);
        admin.setEnable(1);
        admin.setLoginCount(200);
        int rows = mapper.updateById(admin);
        log.info("受影响的行数是：{}",rows);
    }

    @Test
    void testServiceDeleteById(){
        long id=12L;
        try {
            service.deleteById(id);
            log.info("tests-删除用户数据成功，id为{}",id);
        } catch (ServiceException e) {
            log.info("tests-{}",e.getMessage());
        }
    }

    @Test
    void testServiceEnable(){
        Long id=18L;
        try {
            service.setEnable(id);
            log.info("id为{}的管理员修改为启用状态成功",id);
        } catch (ServiceException e) {
            log.info("tests-{}",e.getMessage());
        }
    }

    @Test
    void testServiceDisable(){
        Long id=18L;
        try {
            service.setDisable(id);
            log.info("id为{}的管理员修改为不启用状态成功",id);
        } catch (ServiceException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    void testCountByUsername(){
        String username="test1";
        int countByUserName = mapper.countByUserName(username);
        log.info("{}对应的用户个数是：{}",username,countByUserName);
    }

    @Test
    void testCountByEmail(){
        String email="z2x@qq.com";
        int countByEmail = mapper.countByEmail(email);
        log.info("{}对应的用户个数是：{}",email,countByEmail);
    }

    @Test
    void testCountByPhone(){
        String phone="1331";
        int countByPhone = mapper.countByPhone(phone);
        log.info("{}对应的用户个数是：{}",phone,countByPhone);
    }

    @Test
    void testStandardById(){
        Long id=2l;
        AdminStandardVO vo=mapper.getStandardById(id);
        log.info("data is:{}",vo.toString());
    }

    @Test
    void testList(){
        List<AdminListItemVO> list=mapper.list();
        for(AdminListItemVO vo : list){
            log.info("data is:{}",vo.toString());
        }
    }

    @Test
    void testServiceList(){
        List<AdminListItemVO> list=service.list();
        for(AdminListItemVO vo : list){
            log.info("data is:{}",vo.toString());
        }
    }
}

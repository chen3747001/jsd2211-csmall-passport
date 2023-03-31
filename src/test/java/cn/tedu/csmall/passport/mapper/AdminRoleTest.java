package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class AdminRoleTest {
    @Autowired
    AdminRoleMapper mapper;

    @Test
    void testInsertBatch(){
        List<AdminRole> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdminRole role=new AdminRole();
            role.setRoleId(11L);
            role.setAdminId(i+0L);
            list.add(role);
        }
        int rows = mapper.insertBatch(list);
        log.info("受影响的行数：{}",rows);
    }

    @Test
    void testDeleteByAdminId(){
        long adminId=14L;
        int rows = mapper.deleteByAdminId(adminId);
        log.info("受影响的行数：{}",rows);
    }
}

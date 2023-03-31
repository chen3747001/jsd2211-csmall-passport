package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class RoleTests {
    @Autowired
    RoleMapper mapper;
    @Autowired
    IRoleService service;

    @Test
    public void testList(){
        List<RoleListItemVO> list = mapper.list();
        for(RoleListItemVO vo : list){
            log.info("data is: {}",vo.toString());
        }
    }

    @Test
    public void testServiceList(){
        List<RoleListItemVO> list = service.list();
        for(RoleListItemVO vo : list){
            log.info("data is: {}",vo.toString());
        }
    }
}

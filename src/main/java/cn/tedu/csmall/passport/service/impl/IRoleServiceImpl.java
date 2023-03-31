package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.mapper.RoleMapper;
import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IRoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper mapper;

    /**
     * 查询角色列表
     * @return 角色列表
     */
    @Override
    public List<RoleListItemVO> list() {
        return mapper.list();
    }
}

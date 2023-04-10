package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.mapper.RoleMapper;
import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper mapper;

    /**
     * 查询角色列表
     * @return 角色列表
     */
    @Override
    public List<RoleListItemVO> list() {
        //记得删除"系统管理员选项"
        List<RoleListItemVO> list = mapper.list();
        Iterator<RoleListItemVO> iterator = list.iterator();
        while(iterator.hasNext()){
            RoleListItemVO item = iterator.next();
            if(item.getId()==1){
                iterator.remove();
                break;
            }
        }
        return list;
    }
}

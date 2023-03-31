package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理角色列表的Mapper接口
 */
@Repository
public interface RoleMapper {
    /**
     * 查询角色列表
     * @return 角色列表
     */
    List<RoleListItemVO> list();
}

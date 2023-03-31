package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处理角色的业务接口
 */
@Transactional
public interface IRoleService {
    /**
     * 查询角色列表
     * @return 角色列表
     */
    List<RoleListItemVO> list();
}

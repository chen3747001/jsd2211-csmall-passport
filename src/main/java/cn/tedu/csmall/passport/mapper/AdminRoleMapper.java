package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理管理员和角色之间关联数据的mapper接口
 */
@Repository
public interface AdminRoleMapper {
    /**
     * 给管理员绑定角色
     */
    int insertBatch(List<AdminRole> list);

    /**
     * 根据管理员id删除角色数据
     * @param id 输入的管理员id
     * @return 受影响的行数
     */
    int deleteByAdminId(long adminId);
}

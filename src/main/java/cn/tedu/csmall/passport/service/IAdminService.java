package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员的处理接口
 */
@Transactional
public interface IAdminService {
    /**
     * 管理员登录
     * @param dto 封装了管理员的登录信息
     * @return JWT数据
     */
    String login(AdminLoginDTO dto);

    /**
     * 添加新管理员
     * @param dto 管理员的数据
     */
    void addNew(AdminAddNewDTO dto);

    /**
     * 根据id删除用户数据
     *
     * @param id 用户id
     * @return
     */
    int deleteById(long id);

    /**
     * 启用管理员
     * @param id 管理员id
     */
    void setEnable(Long id);

    /**
     * 禁用管理员
     * @param id 管理员id
     */
    void setDisable(Long id);

    /**
     * 查询管理员列表
     * @return 管理员列表
     */
    List<AdminListItemVO> list();


}

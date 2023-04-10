package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminLoginInfoVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  处理管理员的mapper接口
 */
@Repository
public interface AdminMapper {
    /**
     * 新建管理员
     * @param admin 管理员的数据
     * @return 受影响的行数
     */
    int insert(Admin admin);

    /**
     * 根据id删除管理员数据
     * @param id 输入id
     * @return 受影响的行数
     */
    int deleteById(long id);

    /**
     * 修改管理员数据
     * @param admin 封装了管理员id和新的数据的对象
     * @return 受影响的行数
     */
    int updateById(Admin admin);

    /**
     * 查找对应用户名的用户个数
     * @param UserName 输入的用户名
     * @return 用户个数
     */
    int countByUserName(String UserName);

    /**
     * 查找对应邮箱的用户个数
     * @param Email 输入的邮箱
     * @return 用户个数
     */
    int countByEmail(String Email);

    /**
     * 查找对应手机号的用户个数
     * @param phone 输入的手机号
     * @return 用户个数
     */
    int countByPhone(String phone);

    /**
     * 根据id查询管理员数据
     * @param id 输入id
     * @return 管理员数据
     */
    AdminStandardVO getStandardById(long id);

    /**
     * 查询管理员列表
     * @return 管理员列表
     */
    List<AdminListItemVO> list();

    /**
     * 根据用户名查找用户的登录信息
     * @param username 输入的用户名
     * @return 用户名登录信息
     */
    AdminLoginInfoVO getLoginInfoByUsername(String username);

}

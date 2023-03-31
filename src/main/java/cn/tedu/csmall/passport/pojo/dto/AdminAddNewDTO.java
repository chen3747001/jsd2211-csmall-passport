package cn.tedu.csmall.passport.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 添加管理员的类
 */
@Data
public class AdminAddNewDTO implements Serializable {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",example = "chenchen",required = true)
    private String username;

    /**
     * 密码（密文）
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱")
    private String email;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 是否启用，1=启用，0=未启用
     */
    @ApiModelProperty(value = "是否启用",example = "1")
    private Integer enable;

    /**
     * 尝试添加的管理员id列表
     */
    @ApiModelProperty(value = "角色列表")
    private long[] roleIds;

}

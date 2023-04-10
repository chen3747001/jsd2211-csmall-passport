package cn.tedu.csmall.passport.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加管理员的类
 */
@Data
public class AdminLoginDTO implements Serializable {
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

}

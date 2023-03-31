package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.service.IRoleService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 处理角色列表的Controller
 */
@Slf4j
@Api(tags = "02. 角色管理模块")
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    IRoleService service;

    @ApiOperation("查询角色列表")
    @ApiOperationSupport(order = 110)
    @GetMapping("")
    public JsonResult<List<RoleListItemVO>> list(){
        return JsonResult.ok(service.list());
    }
}

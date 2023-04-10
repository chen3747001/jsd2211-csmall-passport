package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 处理admin的Controller
 */
@Slf4j
@Api(tags = "01. 管理员管理模块")
@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    IAdminService service;

    @ApiOperation("管理员登录")
    @ApiOperationSupport(order = 300)
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginDTO dto){
//        service.addNew(dto);
        log.info("开始处理【管理员登录】的请求，参数：{}",dto);
        String loginJWT = service.login(dto);
        return JsonResult.ok(loginJWT);
    }

    /**
     * 添加新的管理员
     * @param dto 管理员的数据
     * @return 添加后的结果
     */
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PostMapping("/add-new")
    public JsonResult<Void> addNew(AdminAddNewDTO dto){
        service.addNew(dto);
        log.info("controller-添加管理员成功");
        return JsonResult.ok();
    }

    @ApiOperation("删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult<Void> deleteById(@PathVariable Long id, @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal){
        log.info("当前负责人的id是:{},username is:{}",loginPrincipal.getId(),loginPrincipal.getUsername());
        service.deleteById(id);
        log.info("controller-删除管理员成功");
        return JsonResult.ok();
    }

    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/setEnable")
    public JsonResult<Void> setEnableById(@PathVariable Long id){
        service.setEnable(id);
        log.info("controller-启用管理员成功");
        return JsonResult.ok();
    }

    @ApiOperation("禁用管理员")
    @ApiOperationSupport(order = 320)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PostMapping("/{id:[0-9]+}/setDisable")
    public JsonResult<Void> setDisableById(@PathVariable Long id){
        service.setDisable(id);
        log.info("controller-禁用管理员成功");
        return JsonResult.ok();
    }



    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 410)
    @GetMapping("")
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    public JsonResult<List<AdminListItemVO>> list(){
        log.info("controller-查看管理员列表成功");
        return JsonResult.ok(service.list());
    }
}

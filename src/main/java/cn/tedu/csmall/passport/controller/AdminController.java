package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 处理admin的Controller
 */
@Slf4j
@Api(tags = "01. 管理员管理模块")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    IAdminService service;

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
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        log.info("controller-删除管理员成功");
        return JsonResult.ok();
    }

    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 410)
    @GetMapping("")
    public JsonResult<List<AdminListItemVO>> list(){
        log.info("controller-查看管理员列表成功");
        return JsonResult.ok(service.list());
    }
}

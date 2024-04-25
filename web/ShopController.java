package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Shop;
import com.company.project.service.ShopService;
import com.company.project.common.BaseController;
import io.swagger.annotations.Api;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.List;
import java.util.Date;

@RestController
@RequestMapping("/shop")
@Api(tags = {"/shop"}, description = "店铺管理模块")
public class ShopController extends BaseController{
    @Resource
    private ShopService shopService;

    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Result add(@RequestBody Shop shop) {
        shop.setCreatedAt(new Date());
        shop.setIsDelete(false);
        shop.setCreatedBy(super.getUserId());
        shopService.save(shop);
        Result result=ResultGenerator.genSuccessResult();
        result.setData(shop);
        return result;
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Result delete(@RequestParam Long id) {
        Shop shop=new Shop();
        shop.setId(id);
        shop.setIsDelete(true);
        shopService.update(shop);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public Result update(@RequestBody Shop shop) {
        shop.setUpdatedAt(new Date());
        shopService.update(shop);
        Result result=ResultGenerator.genSuccessResult();
        result.setData(shop);
        return result;
    }

    @ApiOperation(value = "获取单个详情", notes = "获取单个详情")
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public Result detail(@RequestParam(value = "id", required = false) Long id) {
        return shopService.detail(id);
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @RequestMapping(value = "/findByModal", method = {RequestMethod.POST})
    public Result list(@RequestBody(required =false) Shop shop) {
        return shopService.list(super.getUserId(),shop);
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @RequestMapping(value = "/findAllByModal", method = {RequestMethod.POST})
    public Result findAllByModal(@RequestBody(required =false) Shop shop) {
        return shopService.findAllByModal(super.getUserId(),shop);
    }

}

package com.company.project.web;

import com.company.project.common.BaseController;
import com.company.project.core.Result;
import com.company.project.model.Goods;
import com.company.project.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods")
@Api(tags = {"/goods"}, description = "商品管理模块")
public class GoodsController extends BaseController {
    @Resource
    private GoodsService goodsService;

    @ApiOperation(value = "新增商品", notes = "新增商品")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Result add(@RequestBody Goods goods) {
        goods.setCreatedBy(super.getUserId());
        return goodsService.add(goods);
    }

    @ApiOperation(value = "删除商品", notes = "删除商品")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Result delete(@RequestParam Long id) {
        return goodsService.delete(id);
    }

    @ApiOperation(value = "修改商品", notes = "修改商品")
    @RequestMapping(value = "/updateGoods", method = {RequestMethod.POST})
    public Result updateGoods(@RequestBody Goods goods) {
        return goodsService.updateGoods(goods);
    }

    @ApiOperation(value = "获取电影单个详情", notes = "获取电影单个详情")
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public Result detail(@RequestParam Long id, @RequestParam(required = false) String userId) {
        return goodsService.detail(id,userId);
    }

    @ApiOperation(value = "分页查询商品", notes = "分页查询商品")
    @RequestMapping(value = "/findByModal", method = {RequestMethod.POST})
    public Result list(@RequestBody(required =false) Goods goods) {
        return goodsService.list(super.getUserId(),goods);
    }

    @ApiOperation(value = "为你推荐商品", notes = "为你推荐商品")
    @RequestMapping(value = "/findByNumber", method = {RequestMethod.POST})
    public Result findByNumber(@RequestBody(required =false) Goods goods) {
        return goodsService.findByNumber(goods);
    }

    @ApiOperation(value = "根据ids集合查询商品", notes = "根据ids集合查询商品")
    @RequestMapping(value = "/findByAllIds", method = {RequestMethod.POST})
    public Result findByAllIds(@RequestBody(required =false) Goods goods) {
        return goodsService.findByAllIds(goods);
    }

    @ApiOperation(value = "分页查询全部商品销量", notes = "分页查询全部商品销量")
    @RequestMapping(value = "/findAllByModal", method = {RequestMethod.POST})
    public Result findAllByModal(@RequestBody(required =false) Goods goods) {
        return goodsService.findAllByModal(goods);
    }

    @ApiOperation(value = "分页查询推荐商品", notes = "分页查询推荐商品")
    @RequestMapping(value = "/recommendByUser", method = {RequestMethod.POST})
    public Result recommendByUser(@RequestBody(required = false) Goods goods) {
        goods.setCreatedBy(super.getUserId());
        return goodsService.recommendByUser(goods);
    }

}

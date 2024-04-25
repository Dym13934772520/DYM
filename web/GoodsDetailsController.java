package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.GoodsDetails;
import com.company.project.service.GoodsDetailsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/goods/details")
@Api(tags = {"/goods/details"}, description = "采集用户数据详情管理模块")
public class GoodsDetailsController {
    @Resource
    private GoodsDetailsService goodsDetailsService;

    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Result add(@RequestBody GoodsDetails goodsDetails) {
        goodsDetails.setCreatedAt(new Date());
        goodsDetails.setIsDelete(false);
        goodsDetailsService.save(goodsDetails);
        Result result= ResultGenerator.genSuccessResult();
        result.setData(goodsDetails);
        return result;
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Result delete(@RequestParam Long id) {
        GoodsDetails goodsDetails=new GoodsDetails();
        goodsDetails.setId(id);
        goodsDetails.setIsDelete(true);
        goodsDetailsService.update(goodsDetails);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public Result update(@RequestBody GoodsDetails goodsDetails) {
        goodsDetails.setUpdatedAt(new Date());
        goodsDetailsService.update(goodsDetails);
        Result result= ResultGenerator.genSuccessResult();
        result.setData(goodsDetails);
        return result;
    }

    @ApiOperation(value = "获取单个详情", notes = "获取单个详情")
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public Result detail(@RequestParam Long id) {
        GoodsDetails goodsDetails = goodsDetailsService.findById(id);
        return ResultGenerator.genSuccessResult(goodsDetails);
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @RequestMapping(value = "/findByModal", method = {RequestMethod.POST})
    public Result list(@RequestBody(required =false) GoodsDetails goodsDetails) {
        PageHelper.startPage(goodsDetails.getPage(), goodsDetails.getLimit());
        goodsDetails.setIsDelete(false);
        List<GoodsDetails> list = goodsDetailsService.findByModel(goodsDetails);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

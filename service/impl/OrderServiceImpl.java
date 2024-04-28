package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.OrderMapper;
import com.company.project.model.Goods;
import com.company.project.model.Order;
import com.company.project.model.OrderDetails;
import com.company.project.model.User;
import com.company.project.service.GoodsService;
import com.company.project.service.OrderDetailsService;
import com.company.project.service.OrderService;
import com.company.project.service.UserService;
import com.company.project.utils.DigitUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDetailsService orderDetailsService;

    @Resource
    private UserService userService;

    @Resource
    private GoodsService goodsService;

    @Override
    public Result add(Order order) {

        Goods goods = goodsService.findById(order.getOrderDetailsList().get(0).getGoodsId());
        if (null == goods){
            return ResultGenerator.genFailResult(ResultCode.GOODS_NULL_ERROR,"Item does not exist, please re-order");
        }

        if (null == goods.getRepertory()){
            return ResultGenerator.genFailResult(ResultCode.GOODS_REPERTORY_ERROR,"Goods are insufficient, please contact the administrator to replenish");
        }

        int count = orderDetailsService.getCountByGoodsId(order.getOrderDetailsList().get(0).getGoodsId());
        if (goods.getRepertory().intValue() < count){
            return ResultGenerator.genFailResult(ResultCode.GOODS_REPERTORY_ERROR,"Goods are insufficient, please contact the administrator to replenish");
        }

        order.setId(DigitUtil.generatorLongId());

        //批量添加订单详情
        if (order.getOrderDetailsList().size() > 0){
            for (OrderDetails d: order.getOrderDetailsList()) {
                d.setOrderId(order.getId());
                d.setCreatedAt(new Date());
                d.setIsDelete(false);
            }
            orderDetailsService.save(order.getOrderDetailsList());
        }

        order.setCreatedAt(new Date());
        order.setIsDelete(false);
        order.setStatus(1);
        save(order);

        //下单成功库存-1
        goods.setRepertory(goods.getRepertory().subtract(BigDecimal.valueOf(1)));
        goodsService.update(goods);

        Result result= ResultGenerator.genSuccessResult();
        result.setData(order);
        return result;
    }

    @Override
    public Result list(String userId,Order order) {

        if (null != order.getCreatedBy()){
            User user = userService.findByIdAndValidDelete(Long.valueOf(order.getCreatedBy()));
            if (user != null){
                if (0 == user.getRoleId() || 2 == user.getRoleId()){
                    order.setCreatedBy(null);
                }
            }
        }

        User user = userService.findByIdAndValidDelete(Long.valueOf(userId));
        if (null == user){
            return ResultGenerator.genFailResult("User information does not exist [account may be deactivated or deleted]");
        }

        if (0 == user.getRoleId() || 3 == user.getRoleId()){
            order.setCreatedBy(null);
        }else {
            order.setCreatedBy(userId);
        }

        PageHelper.startPage(order.getPage() == null ? 0 : order.getPage(), order.getLimit() == null ? 10 : order.getLimit());
        order.setIsDelete(false);
        List<Order> list = orderMapper.list(order);
        for (Order d: list) {
            d.setOrderDetailsList(orderDetailsService.selectByOrderId(d.getId()));
            if (null != d.getOrderDetailsList()){
                if (d.getOrderDetailsList().size() > 0){
                    d.setGoodsName(d.getOrderDetailsList().get(0).getGoodsName());
                }
            }
            if (d.getCreatedBy() != null){
                User newUser = userService.findByIdAndValidDelete(Long.valueOf(d.getCreatedBy()));
                if (newUser != null){
                    d.setUser(newUser);
                    d.setUserName(newUser.getUserName());
                }
            }
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Override
    public Result detail(Long id) {
        Order order = orderMapper.detail(id);
        if (null != order){
            order.setOrderDetailsList(orderDetailsService.selectByOrderId(order.getId()));
            order.setUser(userService.findByIdAndValidDelete(Long.valueOf(order.getCreatedBy())));
        }
        return ResultGenerator.genSuccessResult(order);
    }

    @Override
    public BigDecimal getMarketCount() {
        return orderMapper.getMarketCount();
    }

    @Override
    public BigDecimal getTodayCount(Date date) {
        return orderMapper.getTodayCount(date);
    }

    @Override
    public BigDecimal getMarketOrderCount() {
        return orderMapper.getMarketOrderCount();
    }

    @Override
    public BigDecimal getTodayOrderCount(Date date) {
        return orderMapper.getTodayOrderCount(date);
    }

    @Override
    public Result updateGoods(Order order) {

        Goods goods = goodsService.findById(orderDetailsService.selectByOrderId(order.getId()).get(0).getGoodsId());
        if (null == goods){
            return ResultGenerator.genFailResult(ResultCode.GOODS_NULL_ERROR,"Item does not exist, please re-select");
        }

        order.setUpdatedAt(new Date());
        update(order);

        //如果归还成功，影碟库存+1
        if (7 == order.getStatus()){
            //归还成功，影碟库存+1
            goods.setRepertory(goods.getRepertory().add(BigDecimal.valueOf(1)));
            goodsService.update(goods);
        }

        Result result= ResultGenerator.genSuccessResult();
        result.setData(order);
        return result;
    }

}
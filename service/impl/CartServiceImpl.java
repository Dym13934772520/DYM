package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.CartMapper;
import com.company.project.model.Cart;
import com.company.project.service.CartService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl extends AbstractService<Cart> implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Override
    public Result list(Cart cart) {
        if (cart == null){
            cart = new Cart();
        }

        PageHelper.startPage(cart.getPage() == null ? 0 : cart.getPage(), cart.getLimit() == null ? 10 : cart.getLimit());
        cart.setIsDelete(false);
        List<Cart> list = cartMapper.list(cart);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Override
    public Result add(Cart cart) {

        Cart newCart = cartMapper.getByGoodsIdAndUserId(cart);
        if (newCart != null){
            newCart.setCreatedBy(cart.getCreatedBy());
            newCart.setNumber(new BigDecimal(newCart.getNumber()).add(new BigDecimal(1)).intValue());
            update(newCart);
        }else {
            cart.setNumber(1);
            save(cart);
        }

        Result result= ResultGenerator.genSuccessResult();
        result.setData(cart);
        return result;
    }

    @Override
    public Result batchDelete(Cart cart) {
        String[] array = cart.getIds().split(",");
        for (int i = 0; i <array.length; i++){
            cart.setIsDelete(true);
            cart.setId(Long.valueOf(array[i]));
            update(cart);
        }
        return ResultGenerator.genSuccessResult();
    }

}

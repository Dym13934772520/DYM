package com.company.project.service.impl;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.ShopMapper;
import com.company.project.model.Shop;
import com.company.project.model.User;
import com.company.project.service.ShopService;
import com.company.project.core.AbstractService;
import com.company.project.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ShopServiceImpl extends AbstractService<Shop> implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private UserService userService;

    @Override
    public Result findAllByModal(String userId, Shop shop) {

        if (null == shop){
            shop = new Shop();
        }

        PageHelper.startPage(shop.getPage() == null ? 0 : shop.getPage(), shop.getLimit() == null ? 10 : shop.getLimit());
        shop.setIsDelete(false);
        List<Shop> list = shopMapper.findAllByModal(shop);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Override
    public Result detail(Long id) {

        if (null == id){
            return ResultGenerator.genSuccessResult(new Shop());
        }else {
            Shop shop = shopMapper.detail(id);
            if (null == shop){
                return ResultGenerator.genSuccessResult(new Shop());
            }else {
                return ResultGenerator.genSuccessResult(shop);
            }
        }
    }

    @Override
    public Result list(String userId, Shop shop) {

        if (null == shop){
            shop = new Shop();
        }

        User user = userService.findByIdAndValidDelete(Long.valueOf(userId));
        if (null == user){
            return ResultGenerator.genFailResult("User information does not exist [Account may be deactivated or deleted");
        }

        if (0 == user.getRoleId()){
            shop.setCreatedBy(null);
        }else {
            shop.setCreatedBy(userId);
        }

        PageHelper.startPage(shop.getPage() == null ? 0 : shop.getPage(), shop.getLimit() == null ? 10 : shop.getLimit());
        shop.setIsDelete(false);
        List<Shop> list = shopMapper.list(shop);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

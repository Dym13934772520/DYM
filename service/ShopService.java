package com.company.project.service;
import com.company.project.core.Result;
import com.company.project.model.Shop;
import com.company.project.core.Service;

public interface ShopService extends Service<Shop> {

    Result list(String userId, Shop shop);

    Result findAllByModal(String userId, Shop shop);

    Result detail(Long id);
}

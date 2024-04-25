package com.company.project.service.impl;

import com.company.project.dao.ShippingAddressMapper;
import com.company.project.model.ShippingAddress;
import com.company.project.service.ShippingAddressService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ShippingAddressServiceImpl extends AbstractService<ShippingAddress> implements ShippingAddressService {

    @Resource
    private ShippingAddressMapper tShippingAddressMapper;

}

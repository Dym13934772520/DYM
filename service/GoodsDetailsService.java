package com.company.project.service;

import com.company.project.core.Service;
import com.company.project.model.GoodsDetails;

import java.util.List;

public interface GoodsDetailsService extends Service<GoodsDetails> {

    List<GoodsDetails> findByUser(String uid);
}

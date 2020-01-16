package com.kiki.skill.service.impl;

import com.kiki.skill.dao.GoodsDao;
import com.kiki.skill.service.GoodsService;
import com.kiki.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public List<GoodsVo> getGoodsVoList() {
        return goodsDao.getGoodsVoList();
    }

    @Override
    public GoodsVo getGoodsById(long goodsId) {
        return goodsDao.getGoodsVoById(goodsId);
    }
}

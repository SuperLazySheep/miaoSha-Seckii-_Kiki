package com.kiki.skill.service;

import com.kiki.skill.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    List<GoodsVo> getGoodsVoList();

    GoodsVo getGoodsById(long goodsId);
}

package com.kiki.skill.service.impl;

import com.kiki.skill.dao.SkillGoodsDao;
import com.kiki.skill.domain.SkillGoods;
import com.kiki.skill.service.SkillGoodsService;
import com.kiki.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillGoodsServiceImpl implements SkillGoodsService {

    @Autowired
    SkillGoodsDao skillGoodsDao;

    @Override
    public boolean reduceStock(GoodsVo goods) {
        SkillGoods skillGoods = new SkillGoods();
        skillGoods.setGoodsId(goods.getId());
        int res = skillGoodsDao.reduceStock(skillGoods);
        return res > 0;
    }
}

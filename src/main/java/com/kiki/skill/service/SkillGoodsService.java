package com.kiki.skill.service;

import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.vo.GoodsVo;

public interface SkillGoodsService {
    boolean reduceStock( GoodsVo goods);
}

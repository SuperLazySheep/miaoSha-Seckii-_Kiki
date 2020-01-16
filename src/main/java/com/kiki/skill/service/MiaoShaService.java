package com.kiki.skill.service;

import com.kiki.skill.domain.OrderInfo;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.vo.GoodsVo;

public interface MiaoShaService {
    OrderInfo miaoSha(SkillUser user, GoodsVo goods);

    long getMiaoShaResult(Long userId, Long goodsId);

    String creatPath(SkillUser user, Long goodsId);

    boolean checkPath(SkillUser user, Long goodsId, String path);
}

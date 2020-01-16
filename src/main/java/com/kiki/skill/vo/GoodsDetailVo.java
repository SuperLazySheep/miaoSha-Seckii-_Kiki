package com.kiki.skill.vo;

import com.kiki.skill.domain.SkillUser;
import lombok.Data;

/**
 * 为了给页面传值
 */
@Data
public class GoodsDetailVo {
    // 秒杀状态量
    private int status = 0;
    // 开始时间倒计时
    private int remailSeconds = 0;
    private GoodsVo goodsVo;
    private SkillUser user;
}

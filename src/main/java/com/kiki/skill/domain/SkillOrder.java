package com.kiki.skill.domain;

import lombok.Data;

@Data
public class SkillOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;

}

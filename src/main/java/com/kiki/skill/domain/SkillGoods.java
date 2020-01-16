package com.kiki.skill.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SkillGoods {
    private Long id;
    private Long goodsId;
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}

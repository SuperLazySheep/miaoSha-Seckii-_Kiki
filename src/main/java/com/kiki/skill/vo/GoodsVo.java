package com.kiki.skill.vo;

import com.kiki.skill.domain.Goods;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

// 将goods表和miaoshaGoods表合并
@Data
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}

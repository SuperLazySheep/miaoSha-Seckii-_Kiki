package com.kiki.skill.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId; // 收获地址id
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer orderStatus;
    private Date createDate;
    private Date payDate;

}

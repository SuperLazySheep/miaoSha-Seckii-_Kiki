package com.kiki.skill.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SkillUser implements Serializable {
    private Long id;
    private String nickname; // 称号
    private String pwd;
    private String salt; // 盐值
    private String head;
    private Date registerDate;  // 注册时间
    private Date lastLoginDate; // 最后登陆时间
    private Integer loginCount; // 登陆次数
}

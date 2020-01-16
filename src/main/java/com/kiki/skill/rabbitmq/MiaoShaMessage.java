package com.kiki.skill.rabbitmq;

import com.kiki.skill.domain.SkillUser;
import lombok.Data;

import java.io.Serializable;

@Data
public class MiaoShaMessage implements Serializable {
    private SkillUser user;
    private Long goodId;
}

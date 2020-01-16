package com.kiki.skill.service;

import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

public interface SkillUserService {

    SkillUser getById(Long id);

   boolean login(HttpServletResponse response, LoginVo loginVo);

    SkillUser getByToken(HttpServletResponse response, String token);
}

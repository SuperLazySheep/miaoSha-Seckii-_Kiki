package com.kiki.skill.dao;

import com.kiki.skill.domain.SkillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SkillUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    SkillUser getById(Long id);

    @Update("update miaosha_user set pwd = #{pwd} where id = #{id}")
    void update(SkillUser toBeanUpdate);
}

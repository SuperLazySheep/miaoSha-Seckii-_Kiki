package com.kiki.skill.dao;

import com.kiki.skill.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Select("select * from t_user where id = #{id} ")
    User findUserById(int id);
}

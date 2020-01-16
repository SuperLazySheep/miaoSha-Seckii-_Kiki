package com.kiki.skill.service.impl;

import com.kiki.skill.dao.UserDao;
import com.kiki.skill.domain.User;
import com.kiki.skill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserById(int id) {
        return userDao.findUserById(id);
    }
}

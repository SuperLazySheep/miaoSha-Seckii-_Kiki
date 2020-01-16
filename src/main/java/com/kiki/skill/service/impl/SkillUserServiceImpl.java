package com.kiki.skill.service.impl;

import com.kiki.skill.dao.SkillUserDao;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.exception.MyGlobalException;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.redis.SkillUserKey;
import com.kiki.skill.result.MsgCode;
import com.kiki.skill.service.SkillUserService;
import com.kiki.skill.util.MD5Util;
import com.kiki.skill.util.UUIDUtil;
import com.kiki.skill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kiKi
 */
@Service
public class SkillUserServiceImpl implements SkillUserService {

    public static final String COOKIE_TOKEN_NAME = "token";

    @Autowired
    SkillUserDao skillUserDao;
    @Autowired
    RedisService redisService;

    @Override
    public SkillUser getById(Long id) {
        // 取缓存
      SkillUser user = redisService.get(SkillUserKey.getById,""+id,SkillUser.class);
      if(user != null){
          return user;
      }
      //取数据库
      user = skillUserDao.getById(id);
      if(user != null){
          redisService.set(SkillUserKey.getById,""+id,user);
      }
      return user;
    }

    public boolean updatePassword(String token, Long id,String passwoedNew){
        // 取user
        SkillUser user = skillUserDao.getById(id);
        if(user == null){
            throw new MyGlobalException(MsgCode.MOBILE_EMPTY);
        }
        // 更新数据库
        SkillUser toBeanUpdate = new SkillUser();
        user.setId(id);
        user.setPwd(MD5Util.fromPassToDBPass(passwoedNew,user.getSalt()));
        skillUserDao.update(toBeanUpdate);
        //处理缓存
        //删除对象
        redisService.del(SkillUserKey.getById,""+id);
        user.setPwd(toBeanUpdate.getPwd());
        //更新token缓存
        redisService.set(SkillUserKey.token,token,user);
        return true;
    }

    /**
     * 判断redis中token是否存在
     * @param token
     * @return
     */
    @Override
    public SkillUser getByToken(HttpServletResponse response, String token) {
       if(StringUtils.isEmpty(token)){
           return null;
       }
       //判断redis是否存在用户
        SkillUser user = redisService.get(SkillUserKey.token, token, SkillUser.class);
       if(user != null){
           //更新用户的有效期和token
           addCookie(response, user,token);
       }
        return user;
    }

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null){
            throw new MyGlobalException(MsgCode.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String fromPassword = loginVo.getPassword();
        SkillUser skillUser = skillUserDao.getById(Long.parseLong(mobile));
        //判断手机号
        if(skillUser == null){
            throw new MyGlobalException(MsgCode.MOBILE_ERROR);
        }
        String skillUserPwd = skillUser.getPwd();
        String skillUserSalt = skillUser.getSalt();
        //判断密码
        String dbPass = MD5Util.fromPassToDBPass(fromPassword, skillUserSalt);
        if(!dbPass.equals(skillUserPwd)){
           throw new MyGlobalException(MsgCode.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, skillUser,token);
        return true;
    }

    public void addCookie(HttpServletResponse response, SkillUser skillUser, String token) {
        //存入redis中
        redisService.set(SkillUserKey.token,token,skillUser);
        Cookie cookie = new Cookie(COOKIE_TOKEN_NAME,token);
        //设置有效时间
        cookie.setMaxAge(SkillUserKey.token.expireSeconds());
        //设置访问域
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}

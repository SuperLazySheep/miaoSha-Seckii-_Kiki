package com.kiki.skill.controller;

import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.redis.GoodsKey;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.result.ResultData;
import com.kiki.skill.service.GoodsService;
import com.kiki.skill.service.SkillUserService;
import com.kiki.skill.service.impl.SkillUserServiceImpl;
import com.kiki.skill.vo.GoodsDetailVo;
import com.kiki.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SkillUserService skillUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    // 页面渲染
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;

    /**
     *  未缓存页面
     *  1000*10
     *  QPS 784.9293563579279
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/to_list_noCache")
    public String toListNoCache(Model model, SkillUser user){
       // System.out.println(user);
        List<GoodsVo> goodsList = goodsService.getGoodsVoList();
        /*if(user == null){
            return "login2";
        }*/
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }

    /**
     *   做页面缓存，防止同一时间巨大的用户量访问数据，     如果缓存时间过长，会导致数据一致性不强
     *  1000*10
     *
     *  QPS 1201.923076923077
     */
    @RequestMapping(value = "/to_list", produces = "text/html") // 传入user对象啊，不然怎么取user的值，${user.nickname}
    @ResponseBody
    public String toList(Model model, SkillUser user, HttpServletResponse response, HttpServletRequest request){
        // 取缓存    public <T>T get(KeyPrefix prefix, String key, Class<T> clazz)
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        // 没有 去查询数据，
        List<GoodsVo> goodsList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList",goodsList);
         // 手动渲染， 使用模板引擎 templateName: 模板名称  String templateName = "goods_List"
        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale() , model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",context);
        // 保存至缓存
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }


    /**
     * 页面静态化，实现前后分离
     * 页面存的是html
     * 动态数据通过接口从服务端获取
     */
    @RequestMapping("/to_detail/{goodsId}")
    @ResponseBody
    public ResultData<GoodsDetailVo> toDetailCache(Model model, SkillUser user, HttpServletRequest request, HttpServletResponse response,
                                                   @PathVariable("goodsId") long goodsId
    ){
        GoodsVo goods = goodsService.getGoodsById(goodsId);
     //   model.addAttribute("user",user);
     //   model.addAttribute("goods",goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //设置秒杀的状态
        int status = 0;
        //设置秒杀的剩余时间
        int remainSeconds = 0;
        if(now < startAt){ // 秒杀未开始
            status = 0;
            remainSeconds = (int) ((startAt - now)/1000);
        }else if(now > endAt){// 秒杀已结束
            status = 2;
            remainSeconds = -1;
        }else{ // 秒杀进行中
            status = 1;
            remainSeconds = 0;
        }
     //   model.addAttribute("status",status);
      //  model.addAttribute("remailSeconds",remainSeconds);
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setRemailSeconds(remainSeconds);
        goodsDetailVo.setStatus(status);
        goodsDetailVo.setUser(user);
        return ResultData.success(goodsDetailVo);
    }

//    /**
//     * 做了页面缓存的to_detail商品详情页。
//     */
//    @RequestMapping("/to_detail/{goodsId}")
//    @ResponseBody
//    public String toDetailCache(Model model, SkillUser user, HttpServletRequest request, HttpServletResponse response,
//                           @PathVariable("goodsId") long goodsId
//    ){
//        // 取缓存
//        String html = redisService.get(GoodsKey.getGetGoodsDetail, ""+goodsId, String.class);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
//        GoodsVo goods = goodsService.getGoodsById(goodsId);
//        model.addAttribute("user",user);
//        model.addAttribute("goods",goods);
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//        //设置秒杀的状态
//        int status = 0;
//        //设置秒杀的剩余时间
//        int remainSeconds = 0;
//        if(now < startAt){ // 秒杀未开始
//            status = 0;
//            remainSeconds = (int) ((startAt - now)/1000);
//        }else if(now > endAt){// 秒杀已结束
//            status = 2;
//            remainSeconds = -1;
//        }else{ // 秒杀进行中
//            status = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("status",status);
//        model.addAttribute("remailSeconds",remainSeconds);
//       // 设置模板引擎
//        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(),
//                request.getLocale(), model.asMap(), applicationContext);
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",context);
//        // 存缓存
//       if(!StringUtils.isEmpty(html)){
//           redisService.set(GoodsKey.getGetGoodsDetail,""+goodsId,html);
//       }
//        return html;
//    }

    /**
     *  未缓存页面
     *  QPS 746.923076923077
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/to_detail_noCache/{goodsId}")
    public String toDetailNoCache(Model model, SkillUser user,
        @PathVariable("goodsId") long goodsId
    ){
//        GoodsVo goods = goodsService.getGoodsById(goodsId);
//        model.addAttribute("user",user);
//        model.addAttribute("goods",goods);
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//        //设置秒杀的状态
//        int status = 0;
//        //设置秒杀的剩余时间
//        int remainSeconds = 0;
//        if(now < startAt){ // 秒杀未开始
//            status = 0;
//            remainSeconds = (int) ((startAt - now)/1000);
//        }else if(now > endAt){// 秒杀已结束
//            status = 2;
//            remainSeconds = -1;
//        }else{ // 秒杀进行中
//            status = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("status",status);
//        model.addAttribute("remailSeconds",remainSeconds);
//        return "goods_detail";
        return null;
    }

    @RequestMapping("/to_listDemo")
    public String toListDemo(Model model, HttpServletResponse response,
                         @CookieValue(value = SkillUserServiceImpl.COOKIE_TOKEN_NAME, required = false) String cookieToToken,
                         @RequestParam(value = SkillUserServiceImpl.COOKIE_TOKEN_NAME, required = false) String paramToToken
    ){
        if(StringUtils.isEmpty(cookieToToken) && StringUtils.isEmpty(paramToToken)){
            return "login2";
        }
        String token = StringUtils.isEmpty(paramToToken)?cookieToToken:paramToToken;
        SkillUser user = skillUserService.getByToken(response, token);
        model.addAttribute("user",user);
        return "goods_list";
    }
}

package com.kiki.skill.controller;

import com.kiki.skill.domain.OrderInfo;
import com.kiki.skill.domain.SkillOrder;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.rabbitmq.MQSender;
import com.kiki.skill.rabbitmq.MiaoShaMessage;
import com.kiki.skill.redis.GoodsKey;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.result.MsgCode;
import com.kiki.skill.result.ResultData;
import com.kiki.skill.service.GoodsService;
import com.kiki.skill.service.MiaoShaService;
import com.kiki.skill.service.OrderService;
import com.kiki.skill.vo.GoodsVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/miaosha")
@Api("秒杀接口")
public class MiaoShaController implements InitializingBean {
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MiaoShaService miaoShaService;
    @Autowired
    OrderService orderService;
    @Autowired
    MQSender mqSender;
    //标记
   // Map<Long,Boolean> localMap = new HashMap<Long,Boolean>();

    /**
     *系统初始化的时候做的事情。
     * 在容器启动时候，检测到了实现了接口InitializingBean之后，
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if(goodsVoList == null){
            return;
        }
        for (GoodsVo goodsVo : goodsVoList ){
            //如果不是null的时候，将库存加载到redis里面去 prefix---GoodsKey:gs ,	 key---商品id,	 value
           // System.out.println(goodsVo .getId()+"---------------------"+goodsVo.getStockCount());
            redisService.set(GoodsKey.getMiaoShaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
           // localMap.put(goodsVo.getId(),false);
        }
    }

    /**
     * 秒杀地址隐藏接口
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<Integer> doMiaoShaPath(Model model, SkillUser user,
                                           @RequestParam("goodsId") Long goodsId,
                                             @PathVariable("path")String path
    ){
        if( user == null ){
            return ResultData.error(MsgCode.SESSION_EMPTY);
        }
        // 从redis中取出path验证
        boolean check = miaoShaService.checkPath(user,goodsId,path);
        if(!check){
            return ResultData.error(MsgCode.PATH_ERROR);
        }
        // 判断是否秒杀过
        SkillOrder order = orderService.getOrderByUseridAndGoodsid_cache(user.getId(), goodsId);
        if(order != null){
           return ResultData.error(MsgCode.REPECT_MIAOSHA);
        }
        // 用本地  内存 来减少对 redis的访问
        //Boolean over =  localMap.get(goodsId);
//        if(over){
//            return ResultData.error(MsgCode.MIAO_SHA_OVER);
//        }
        // 减redis的库存
       Long stock = redisService.decr(GoodsKey.getMiaoShaGoodsStock, "" + goodsId);
        if(stock <= 0){
           // localMap.put(goodsId,true);
           return ResultData.error(MsgCode.MIAO_SHA_OVER);
       }
        MiaoShaMessage miaoShaMessage = new MiaoShaMessage();
        miaoShaMessage.setUser(user);
        miaoShaMessage.setGoodId(goodsId);
        // 发送消息到队列中  入队
        mqSender.send(miaoShaMessage);
        // 0 排队中
        return ResultData.success(0);
    }

    /**
     * 判断是否生成订单 ， 做轮询操作，
     * 下单成功返回  订单编号
     * 秒杀失败 返回-1
     * 排队中 返回0
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<Long> doMiaoShaResult(Model model, SkillUser user,
                                         @RequestParam("goodsId") Long goodsId
    ) {
        if (user == null) {
            return ResultData.error(MsgCode.SESSION_EMPTY);
        }
        // 查询 是否 下单成功
        long result = miaoShaService.getMiaoShaResult(user.getId(),goodsId);
        return ResultData.success(result);
    }

    /**
     *
     *对秒杀接口地址进行隐藏
     */
    @RequestMapping("/getPath")
    @ResponseBody
    public ResultData getMiaoShaPath(
            SkillUser user, @RequestParam("goodsId") Long goodsId
    ){
        if (user ==null) {
            return ResultData.error(MsgCode.SESSION_EMPTY);
        }
        String path = miaoShaService.creatPath(user,goodsId);
        return ResultData.success(path);
    }

//    /**
//     * 秒杀接口
//     * @param model
//     * @param user
//     * @param goodsId
//     * @return
//     */
//    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultData<Integer> doMiaoSha(Model model, SkillUser user,
//                                         @RequestParam("goodsId") Long goodsId
//    ){
//        if( user == null ){
//            return ResultData.error(MsgCode.SESSION_EMPTY);
//        }
//
//        // 判断是否秒杀过
//        SkillOrder order = orderService.getOrderByUseridAndGoodsid_cache(user.getId(), goodsId);
//        if(order != null){
//            return ResultData.error(MsgCode.REPECT_MIAOSHA);
//        }
//        // 用本地  内存 来减少对 redis的访问
//        //Boolean over =  localMap.get(goodsId);
////        if(over){
////            return ResultData.error(MsgCode.MIAO_SHA_OVER);
////        }
//        // 减redis的库存
//        Long stock = redisService.decr(GoodsKey.getMiaoShaGoodsStock, "" + goodsId);
//        if(stock <= 0){
//            // localMap.put(goodsId,true);
//            return ResultData.error(MsgCode.MIAO_SHA_OVER);
//        }
//        MiaoShaMessage miaoShaMessage = new MiaoShaMessage();
//        miaoShaMessage.setUser(user);
//        miaoShaMessage.setGoodId(goodsId);
//        // 发送消息到队列中  入队
//        mqSender.send(miaoShaMessage);
//        // 0 排队中
//        return ResultData.success(0);
//    }

//    /**
//     * 1000*10
//     * QPS 703.4822370735138
//     * @param model
//     * @param user
//     * @param goodsId
//     * @return
//     */
//    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultData<OrderInfo> doMiaoSha_1(Model model, SkillUser user,
//                                           @RequestParam("goodsId") Long goodsId
//    ){
//        // 判断库存是否有货 ,多线程下会出错
//        GoodsVo goods = goodsService.getGoodsById(goodsId);
//        //库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
//        if(goods.getStockCount() <= 0){
//           // model.addAttribute("errorMessage", MsgCode.MIAO_SHA_OVER.getMsg()); // 秒杀已经结束
//            return ResultData.error(MsgCode.MIAO_SHA_OVER);
//        }
//
//        // 判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
//        SkillOrder order = orderService.getOrderByUseridAndGoodsid(user.getId(), goodsId);
//        System.out.println(order);
//        // 说明秒杀过了
//        if(order != null){
//           // model.addAttribute("errorMessage",MsgCode.REPECT_MIAOSHA.getMsg());
//            return ResultData.error(MsgCode.REPECT_MIAOSHA);
//        }
//
//        // 可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
//        OrderInfo orderInfo = miaoShaService.miaoSha(user,goods);
//       // System.out.println(orderInfo);
//        // 秒杀成功返回详情页
//       // model.addAttribute("orderinfo",orderInfo);
//       // model.addAttribute("goods",goods);
//       // System.out.println(orderInfo.toString());
//        return ResultData.success(orderInfo);
//    }

}

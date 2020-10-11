package top.aolie.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aolie.pay.dao.PayInfoMapper;
import top.aolie.pay.enums.PayPlatformEnum;
import top.aolie.pay.pojo.PayInfo;
import top.aolie.pay.service.IpayService;

import java.math.BigDecimal;

@Slf4j
@Service
public class PayServiceImpl implements IpayService {

    @Autowired
    BestPayService bestPayService;
    @Autowired
    PayInfoMapper payInfoMapper;
    public final static String QUEUE_PAY_NOTIFY= "payNotify";


    @Autowired
    AmqpTemplate amqpTemplate;
    @Override
    /**
     * 创建订单方法
     * @Param String orderId
     * @Param BigDecimal amount
     *
     */
    public PayResponse create(String orderId, BigDecimal amount,BestPayTypeEnum bestPayTypeEnum) {
        //将订单存入数据库
        PayInfo payInfo =new PayInfo(Long.parseLong(orderId)
                ,PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),amount);
        payInfoMapper.insertSelective(payInfo);

        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName("6399460-最好的支付sdk");
        payRequest.setOrderId(orderId);
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(bestPayTypeEnum);
        PayResponse payResponse= bestPayService.pay(payRequest);
        log.info("发起支付 payResponse={}",payResponse);
        return payResponse;
    }

    @Override
    public String asyncNotify(String notifyData) {
        //签名校验
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);
        log.info("异步通知 responses{}",payResponse);

        //金额校验(通过数据库查询)
        //比较严重（正常情况下不会发生的情况）发出告警:钉钉，短信
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo==null){
            //告警
            throw new RuntimeException("通过OderNo查询结果为null");
        }
        //如果不是已支付
        //Double类型比较大小，精度问题
        if(!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            //金额校对查询数据库比对金额是否一致
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount()))!=0){
                //发出告警
                throw new RuntimeException("数据库金额和异步通知里的不一致 orderNo="+payResponse.getOrderId());
            }
            //修改订单状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            //交易流水号(由交易平台产生的)
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            //设置更改时间
            payInfo.setUpdateTime(null);
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }
        //TODO 发送MQ消息通知，pay项目发送mall项目接收MQ消息通知
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY, JSON.toJSON(payInfo));

        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }


    public PayInfo queryByOrderNo(String orderId){
        return  payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
    }
}

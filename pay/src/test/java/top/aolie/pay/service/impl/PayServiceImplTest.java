package top.aolie.pay.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import top.aolie.pay.PayApplicationTests;
import top.aolie.pay.dao.PayInfoMapper;
import top.aolie.pay.service.IpayService;

import java.math.BigDecimal;

public class PayServiceImplTest extends PayApplicationTests {

    @Autowired
    IpayService ipayService;

    @Autowired
    PayInfoMapper mapper;
    @Test
    public void create() {

        ipayService.create("139868484789384783", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);

    }
    @Test
    public void inset(){

//        PayInfo info = new PayInfo();
//        info.setPayAmount(BigDecimal.valueOf(0.01));
//        info.setOrderNo(Long.parseLong("11122738232872782"));
//        info.setId(001);
//        mapper.insert(info);
    }

    @Autowired
    private AmqpTemplate amqpTemplate ;
    @Test
    public void sendMqMsg(){

        //第一个参数填写队列的名字，第二个填消息
        amqpTemplate.convertAndSend("payNotify" ,"hello");
    }


}
package top.aolie.pay.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.aolie.pay.pojo.PayInfo;
import top.aolie.pay.service.IpayService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    WxPayConfig wxPayConfig;
    @Autowired
    private IpayService ipayService;
    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType")BestPayTypeEnum payType){
        PayResponse response= ipayService.create(orderId,amount,payType);
        Map<String,String> map =new HashMap<>();
        if(payType==BestPayTypeEnum.WXPAY_NATIVE){
            map.put("codeUrl",response.getCodeUrl());
            map.put("orderId" ,orderId);
            map.put("returnUrl",wxPayConfig.getReturnUrl());
            return new ModelAndView("createForWxnative",map);
        }else if (payType==BestPayTypeEnum.ALIPAY_PC){
            map.put("body",response.getBody());
            return  new ModelAndView("createForAlipayPc",map);
        }

        throw new RuntimeException("暂不支持其他支付方式");

    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData){

        String mq= ipayService.asyncNotify(notifyData);
       return mq;
    }

    @RequestMapping("/queryByOrdrtId")
    @ResponseBody
    //根据订单号查询支付状态
    public PayInfo queryByOrderNo( @RequestParam String orderId){
        return ipayService.queryByOrderNo(orderId);
    }
}

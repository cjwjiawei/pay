package top.aolie.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BestPayConfig {

    @Autowired
    private WxAccountConfig wxAccountConfig;

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 返回微信、支付宝配置
     * @param wxPayConfig
     * @return
     */
    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig){
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(aliPayConfig.getAppId());
        aliPayConfig.setNotifyUrl(aliPayConfig.getNotifyUrl());
        aliPayConfig.setReturnUrl(alipayConfig.getReturnUrl());
        aliPayConfig.setPrivateKey(alipayConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(alipayConfig.getAliPayPublicKey());
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setAliPayConfig(aliPayConfig);
        bestPayService.setWxPayConfig(wxPayConfig);

        return bestPayService;
    }

    /**
     * 返回微信配置
     * @return
     */
    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        wxPayConfig.setAppId(wxAccountConfig.getAppId());
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
        wxPayConfig.setMchId(wxAccountConfig.getMchId());
        return wxPayConfig;
    }
}

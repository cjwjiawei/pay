package top.aolie.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import top.aolie.pay.pojo.PayInfo;

import java.math.BigDecimal;

public interface IpayService {

    /**
     * 创建支付
     *
     */

    PayResponse create(String orderId , BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);


    String asyncNotify(String notifyData);

    PayInfo queryByOrderNo(String OrderId);

}

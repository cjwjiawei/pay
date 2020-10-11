package top.aolie.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

@Getter
public enum  PayPlatformEnum {

    //1-支付宝,2-微信
    ALIPAY(1),
    WX(2);
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum){

        /*if (bestPayTypeEnum.getPlatform().getName().equals(PayPlatformEnum.ALIPAY.name())){
            return PayPlatformEnum.ALIPAY;
        }
        if (bestPayTypeEnum.getPlatform().getName().equals(PayPlatformEnum.WX.name())){
            return PayPlatformEnum.WX;
        }*/
        System.out.println(bestPayTypeEnum.getPlatform().getCode());

        for (PayPlatformEnum platformEnum : PayPlatformEnum.values()) {
            System.out.println(platformEnum.name());
            if (bestPayTypeEnum.getPlatform().name().equals(platformEnum.name())){

                return platformEnum;
            }
        }
        throw new RuntimeException("错误支付方式 "+bestPayTypeEnum.name());
    }
    public Integer getCode() {
        return code;
    }
}

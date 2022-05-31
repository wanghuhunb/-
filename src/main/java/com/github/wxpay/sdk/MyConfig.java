package com.github.wxpay.sdk;

import java.io.InputStream;

public class MyConfig extends WXPayConfig{
    /**
     * appID
     * @return
     */
    @Override
    public String getAppID() {
        return "wx8397f8696b538317";
    }
    /**
     * 商户ID
     * @return
     */
    @Override
    public String getMchID() {
        return "1473426802";
    }
    /**
     * 授权密钥
     * @return
     */
    @Override
    public String getKey() {
        return "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb";
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    /**
     * 微信支付的域名配置
     * @return
     */
    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String s, long l, Exception e) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig wxPayConfig) {
                return new DomainInfo("api.mch.weixin.qq.com",true);
            }
        };
    }
}

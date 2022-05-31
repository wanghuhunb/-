package com.itheima;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;

import java.util.HashMap;
import java.util.Map;

public class Demo01 {
    public static void main(String[] args) throws Exception {
        // 测试微信的统一下单接口
        MyConfig myConfig = new MyConfig();
        //微信支付对象
        WXPay wxPay = new WXPay(myConfig);
        //设置支付参数
        HashMap<String, String> resultMap = new HashMap<>();
        //商品的描述
        resultMap.put("body","我爱吃大米");
        //订单号
        resultMap.put("out_trade_no","199904085211344");
        //金额
        resultMap.put("total_fee","1");
        //设备ip
        resultMap.put("spbill_create_ip","127.0.0.111");
        //交易类型
        resultMap.put("trade_type","NATIVE");
        //通知地址
        resultMap.put("notify_url","http://ks996.xyz:8080/order/notify");
        Map<String, String> responseMap = wxPay.unifiedOrder(resultMap);
        System.out.println(responseMap);

    }
}

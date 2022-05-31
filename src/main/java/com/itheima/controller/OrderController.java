package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.itheima.entity.Orders;
import com.itheima.entity.R;
import com.itheima.service.OrderService;
import com.itheima.service.ShoppingCartService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单相关请求
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 发送订单请求
     *
     * @param order
     * @return
     * @throws Exception
     */
    @PostMapping("/submit")
    public R submit(@RequestBody Orders order) throws Exception {
        R submit = orderService.submit(order);
        Orders orders = (Orders) submit.getData();
        Map<String, String> ordermap = createOrderWx("我爱吃大米", orders.getId(), orders.getAmount().intValue());

        return R.success(ordermap);
    }

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    /**
     * 你发送给微信后,微信会调取你的这个方法给你返回值
     */
    @RequestMapping ("/notify")
    public void notifyWX(){
        try {
            ServletInputStream inputStream = request.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes=new byte[1024];
            while (true){
                int len = inputStream.read(bytes);
                if(len==-1){
                    break;
                }
                byteArrayOutputStream.write(bytes,0,len);
            }
            String s = new String(byteArrayOutputStream.toByteArray(), "utf-8");
            //调用微信的一个方法解析为一个map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(s);
            //对微信的返回值进行判断
            if(!resultMap.get("return_code").equals("SUCCESS")){
             //表示没有支付成功,抛出异常
                throw new RuntimeException("支付失败，等待人工处理...");
            }
            //成功了就进行订单的状态修改  从微信返回的订单号查找出这个订单,修改他的状态
            String orderId = resultMap.get("out_trade_no");
            Orders byId = orderService.getById(orderId);
            byId.setStatus(2);
            boolean b = orderService.saveOrUpdate(byId);//更新订单状态
            //告诉微信我收到了
            response.setContentType("text/xml");
            String data = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            response.getWriter().write(data);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 调取微信的api支付接口 ,把金额,商品信息发给他,他返还给你一个map,里面的有个url是支付二维码,
     * 还会一直调取你的notify_url来通知你支付的状态
     *
     * @param name
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private Map<String, String> createOrderWx(String name, Long orderId, int money) throws Exception {
        // 测试微信的统一下单接口
        MyConfig myConfig = new MyConfig();
        //微信支付对象
        WXPay wxPay = new WXPay(myConfig);
        //设置支付参数
        HashMap<String, String> resultMap = new HashMap<>();
        //商品的描述
        resultMap.put("body", name);
        //订单号
        resultMap.put("out_trade_no", orderId + "");
        //金额
        resultMap.put("total_fee","1");
        //设备ip
        resultMap.put("spbill_create_ip", "127.0.0.11");
        //交易类型
        resultMap.put("trade_type", "NATIVE");
        //通知地址
        resultMap.put("notify_url", "http://ks996.xyz:8080/order/notify");
        Map<String, String> responseMap = wxPay.unifiedOrder(resultMap);
        System.out.println(responseMap);
        return resultMap;
    }
    /**
     * 更具id查询订单状态
     */
    @GetMapping("/findStatusById")
    public R findStatusById(Long orderId){
        if(orderId==null){
            return R.error("订单不存在");
        }
        //根据id查找出这个订单
        Orders order = orderService.getById(orderId);
        if(order.getStatus()==1){
           return R.error("订单未支付");
        }else {
            return R.success("订单已支付");
        }

    }


}

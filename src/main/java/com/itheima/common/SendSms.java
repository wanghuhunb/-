package com.itheima.common;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSms {
    public static void sendMsg(String phone,String code) {
        //修改及格东西
        /**
         * <accessKeyId>  LTAIkgTcRMp5Nutt
         *     <accessSecret>  wfuZPV0r9G1t30FoKp1CgYQPClJYqf
         *          TemplateCode  SMS_158491775
         *              SignName   品优购
         */
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIkgTcRMp5Nutt", "wfuZPV0r9G1t30FoKp1CgYQPClJYqf");//自己账号的AccessKey信息
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");//短信服务的服务接入地址
        request.setSysVersion("2017-05-25");//API的版本号
        request.setSysAction("SendSms");//API的名称
        request.putQueryParameter("PhoneNumbers", phone);//接收短信的手机号码
        request.putQueryParameter("SignName", "品优购");//短信签名名称
        request.putQueryParameter("TemplateCode", "SMS_158491775");//短信模板ID
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");//短信模板变量对应的实际值
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SendSms.sendMsg("17682314260","11111");
        
    }
}
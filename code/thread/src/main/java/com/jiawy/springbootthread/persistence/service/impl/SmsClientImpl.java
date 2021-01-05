package com.jiawy.springbootthread.persistence.service.impl;

import com.jiawy.springbootthread.persistence.service.SmsClient;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SmsClientImpl implements SmsClient {

    private static final String SEND_URL="https//sms.**.com/send/content";

    @Value("${yp.apiKey}")
    private String apiKey;

    @Override
    public String sendSms(String mobile) {
        Map<String , String > params = new HashMap<>();
        params.put("apiKey" , apiKey);
        params.put("mobile" , mobile);
        params.put("text" , "content==========================");

        return post(SEND_URL , params);
    }

    private static String post(String url , Map<String  , String > params){

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost method = new HttpPost(url);
        if(params != null ){
            List<NameValuePair> pairList = new ArrayList<>();
            params.forEach((key,value) ->{
                NameValuePair nameValuePair= new BasicNameValuePair(key, value);
                pairList.add(nameValuePair);
            });

            try {
                method.setEntity(new UrlEncodedFormEntity(pairList , "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String responseStr = "";
        CloseableHttpResponse closeableHttpResponse = null;
        try {

            closeableHttpResponse = closeableHttpClient.execute(method);
            HttpEntity entity = closeableHttpResponse.getEntity();
            if(entity !=null ){
                responseStr  =  EntityUtils.toString(entity,"UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(closeableHttpResponse !=null){
                try {
                    closeableHttpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }
}

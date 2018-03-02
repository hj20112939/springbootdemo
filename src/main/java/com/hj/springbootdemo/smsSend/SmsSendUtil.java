package com.hj.springbootdemo.smsSend;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SmsSendUtil {
    private static final Logger logger = Logger.getLogger(SmsSendUtil.class);
    @Value("${nexmo.message.key}")
    private String API_KEY;
    @Value("${nexmo.message.secret}")
    private String API_SECRET;
    @Value("${nexmo.message.send.retry}")
    private int SEND_RETRY_TIMES;
    @Async
    public void sendMessage(String from,String phoneNumber, String message){
        int status = 1;
        int sendTimes = 0;
        while(sendTimes < SEND_RETRY_TIMES && status == 1){
            status = sendMessageWithMessageApi(from, phoneNumber, message);
            sendTimes++;
        }
    }

    /**
     *
     * @param from
     * @param phoneNumber
     * @param message
     * @return status 0、success 1、retry 2、failed
     */
    public int sendMessageWithMessageApi(String from,String phoneNumber, String message){
        NexmoClient client = new NexmoClient(
                new TokenAuthMethod(API_KEY, API_SECRET));
        SmsSubmissionResult[] smsResults = new SmsSubmissionResult[0];
        try {
            smsResults = client.getSmsClient().submitMessage(new TextMessage(
                    from,
                    phoneNumber,
                    message));
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (NexmoClientException e) {
            logger.info(e.getMessage());
        }
        int status = smsResults[0].getStatus();
        if(status == 0){
            return 0;
        } else if(status == 1 || status == 5){
            return 1;
        } else{
            return 2;
        }
    }
}

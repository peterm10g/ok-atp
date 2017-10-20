package com.lsh.atp.consumer.demo;

/**
 * Created by fuhao on 16/1/20.
 */
import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.service.hold.IHoldRpcService;
import com.lsh.atp.api.service.sms.ISmsService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


public class DemoConsumer {
    public static void main(String[] args) {
        final String port = "8888";

        //测试Rest服务
//        getUser("http://localhost:" + port + "/services/users/1.json");
//        getUser("http://localhost:" + port + "/services/users/1.xml");

        //测试常规服务
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
        context.start();
//        UserService userService = context.getBean(UserService.class);
        HoldRequest holdRequest = new HoldRequest();
        holdRequest.setZoneCode("1000");
        holdRequest.setChannel("1");
        holdRequest.setHoldEndTime(1488654534L);
        holdRequest.setSequence("12343430987");
       
        context.getBean(IHoldRpcService.class).preHoldInventory(holdRequest);


        System.out.println(context.getBean(ISmsService.class).sendMsg("1", "2"));
//        System.out.println(context.getBean(ISmsRestService.class).sendMsg("1", "2"));
    }


    private static void getUser(String url) {
        System.out.println("Getting user via " + url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        try {
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            System.out.println("Successfully got result: " + response.readEntity(String.class));
        } finally {
            response.close();
            client.close();
        }
    }
}

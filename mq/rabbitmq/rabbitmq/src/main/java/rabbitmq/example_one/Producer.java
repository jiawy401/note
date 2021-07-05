package rabbitmq.example_one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {


    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        //设置RabbitMQ地址
        factory.setHost("39.97.163.78");
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        //获取信道
        Channel channel = conn.createChannel();
        //声明交换器
        String exchangeNmae = "hello-exchange";

        channel.exchangeDeclare(exchangeNmae , "direct"  , true);

        String routingKey = "hola";
        //发布消息
        byte[] messageBodyBytes = "quit".getBytes();
        channel.basicPublish(exchangeNmae , routingKey , null , messageBodyBytes);
        channel.close();
        conn.close();
    }
}

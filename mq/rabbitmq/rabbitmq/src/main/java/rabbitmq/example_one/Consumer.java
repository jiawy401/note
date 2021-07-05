package rabbitmq.example_one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
         factory.setUsername("admin");
         factory.setPassword("admin");
         factory.setHost("39.97.163.78");

         //建立到代理服务器到连接
        Connection conn  = factory.newConnection();

        //获得信道
        final Channel channel = conn.createChannel();

        //声明交换器
        String exchangName = "hello-exchange";
        channel.exchangeDeclare(exchangName , "direct" , true);

        //声明队列
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "hola";

        //绑定队列，通过键 hola 将队列和交换器帮顶起来
        channel.queueBind(queueName , exchangName , routingKey);

        while(true){
            //消费消息
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName , autoAck , consumerTag , new DefaultConsumer(channel){

                public void hangleDelivery(String consumerTag , Envelope envelope , AMQP.BasicProperties properties , byte[] body) throws IOException{

                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("消费的路由键：" + routingKey);
                    System.out.println("消费的内容类型：" + contentType);
                    long deliveryTag = envelope.getDeliveryTag();

                    //确认消息

                    channel.basicAck(deliveryTag , false);
                    System.out.println("消费的消息体内容");
                    String bodyStr = new String(body , "UTF-8");
                    System.out.println(bodyStr);
                }
            });
        }

    }
}

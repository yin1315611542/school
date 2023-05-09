package com.yanda.school.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.yanda.school.utils.EmosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class MessageTask {
    @Autowired
    private ConnectionFactory factory;
//    @Autowired
//    private MessageService messageService;

    /**
     * 同步发送消息
     *
     * @param topic  主题
     * @param entity 消息对象
     */
    public void send(String topic, MessageEntity entity) {
        //向RabbitMQ发送消息
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //连接到某个Topic
            channel.queueDeclare(topic, true, false, false, null);
            HashMap header = new HashMap(); //存放属性数据
            //创建AMQP协议参数对象，添加附加属性
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().headers(header).build();
            channel.basicPublish("", topic, properties, entity.getMsg().getBytes());
            log.debug("消息发送成功");
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new EmosException("向MQ发送消息失败");
        }
    }
    /**
     * 异步发送消息
     *
     * @param topic  主题
     * @param entity
     */
    @Async
    public void sendAsync(String topic, MessageEntity entity) {
        send(topic, entity);
    }
    /**
     * 同步接收数据
     *
     * @param topic 主题
     * @return 接收消息数量
     */
    public int receive(String topic) {
        int i = 0;
        try (//接收消息数据
             Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 从队列中获取消息，不自动确认
            channel.queueDeclare(topic, true, false, false, null);
            //Topic中有多少条数据未知，所以使用死循环接收数据，直到接收不到消息，退出死循环
            while (true) {
                //创建响应接收数据，禁止自动发送Ack应答
                GetResponse response = channel.basicGet(topic, false);
                if (response != null) {
                    AMQP.BasicProperties properties = response.getProps();
                    byte[] body = response.getBody();//获取消息正文
                    String message = new String(body);
                    log.info("从RabbitMQ接收的消息：" + message);
                    MessageRefEntity entity = new MessageRefEntity();
                    entity.setReceiverId(Integer.parseInt(topic));
                    entity.setReadFlag(false);
                    entity.setLastFlag(true);
                    log.info("MessageRefEntity{}",entity.toString());
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    i++;
                } else {
                    break; //接收不到消息，则退出死循环
                }
            }
        } catch (Exception e) {
            log.error("执行异常", e);
        }
        return i;
    }
    /**
     * 异步接收数据
     *
     * @param topic
     * @return
     */
    @Async
    public int receiveAysnc(String topic) {
        return receive(topic);
    }
    /**
     * 同步删除消息队列
     *
     * @param topic 主题
     */
    public void deleteQueue(String topic) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDelete(topic);
            log.debug("消息队列成功删除");
        } catch (Exception e) {
            log.error("删除队列失败", e);
            throw new EmosException("删除队列失败");
        }
    }
    /**
     * 异步删除消息队列
     *
     * @param topic 主题
     */
    @Async
    public void deleteQueueAsync(String topic) {
        deleteQueue(topic);
    }
}

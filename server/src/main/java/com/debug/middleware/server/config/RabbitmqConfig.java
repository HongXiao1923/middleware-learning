package com.debug.middleware.server.config;

import com.debug.middleware.server.rabbitmq.consumer.KnowledgeManualConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName RabbitmqConfig
 *@Description TODO
 *@Author 鸿都
 *@Date 22/5/30 0030 20:34
 **/
@Configuration
public class RabbitmqConfig {
    //日志
    private static final Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);
    //自动设置RabbitMQ的链接工厂实例、自动设置消息监听器所在的容器工厂配置类实例
    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;


    /**
     * 单一消费者实例的配置（一般情况下使用单消费者模式）
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        //设置消息传送的格式，在这里采用JSON格式传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置并发消费者实例的初始数量，在这里为1个
        factory.setConcurrentConsumers(1);
        //设置并发消费者实例的最大数量，在这里为1个
        factory.setMaxConcurrentConsumers(1);
        //设置并发消费者实例中每个实例拉取的消息数量，在这里为一个
        factory.setPrefetchCount(1);

        return factory;
    }

    /**
     * 多个消费者实例的配置（主要针对高并发业务的场景的配置）
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        //定义消息工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //工厂实例
        factoryConfigurer.configure(factory, connectionFactory);
        //传输格式，此处为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置消息的确认消费模式，在这里为NONE，表示不需要确认消费者
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        //设置并发消费者实例的初始数量，在这里为10个
        factory.setConcurrentConsumers(10);
        //设置并发消费者实例的最大数量，在这里为15个
        factory.setMaxConcurrentConsumers(15);
        //设置并发消费者实例中每个实例拉取的消息数量，在这里为10个
        factory.setPrefetchCount(10);

        return factory;
    }

    /**
     * 自定义配置RabbitMQ发送消息的操作组件RabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(){
        //设置“发消息后进行确认”
        connectionFactory.setPublisherConfirms(true);
        //设置“发送消息后返回确认信息”
        connectionFactory.setPublisherReturns(true);
        //构造发送消息组件的实例对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //发送消息后，如果发送成功，则输出“消息发送成功”的反馈信息
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("发送消息成功：correlationData({}), ack({}), cause({})", correlationData, ack, cause);
            }
        });
        //消息发送后，如果发送失败，则输出“消息丢失”的反馈信息
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失：exchange({}), route({}), replyCode({}), replyText({}), message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        //返回组件实例
        return rabbitTemplate;
    }

    //定义读取配置文件的环境变量实例
    @Autowired
    private Environment env;

    //创建队列
    @Bean(name = "basicQueue")
    public Queue basicQueue(){
        return new Queue(env.getProperty("mq.basic.info.queue.name"), true);
    }
    //创建交换机
    @Bean
    public DirectExchange basicExchange(){
        return new DirectExchange(env.getProperty("mq.basic.info.exchange.name"), true, false);
    }
    //创建绑定
    @Bean
    public Binding basicBinding(){
        return BindingBuilder.bind(basicQueue()).to(basicExchange()).with(env.getProperty("mq.basic.info.routing.key.name"));
    }

    //创建简单的对象消息模型：队列、交换机、绑定
    @Bean(name = "objectQueue")
    public Queue objectQueue(){
        return new Queue(env.getProperty("mq.object.info.queue.name"), true);
    }
    @Bean
    public DirectExchange objectExchange(){
        return new DirectExchange(env.getProperty("mq.object.info.exchange.name"), true,false);
    }
    @Bean
    public Binding objectBinding(){
        return BindingBuilder.bind(objectQueue()).to(objectExchange()).with(env.getProperty("mq.object.info.routing.key.name"));
    }

    /**
     * 创建基于FanoutExchange的消息模型
     * @return
     */
    @Bean(name ="fanoutQueueOne")
    public Queue fanoutQueueOne(){
        return new Queue(env.getProperty("mq.fanout.queue.one.name"), true);
    }
    @Bean(name = "fanoutQueueTwo")
    public Queue fanoutQueueTwo(){
        return new Queue(env.getProperty("mq.fanout.queue.two.name"), true);
    }
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(env.getProperty("mq.fanout.exchange.name"), true, false);
    }
    @Bean
    public Binding fanoutBindingOne(){
        return BindingBuilder.bind(fanoutQueueOne()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBindingTwo(){
        return BindingBuilder.bind(fanoutQueueTwo()).to(fanoutExchange());
    }

    /**
     * 创建基于DirectExchange的消息模型
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(env.getProperty("mq.direct.exchange.name"), true, false);
    }
    @Bean(name = "directQueueOne")
    public Queue directQueueOne(){
        return new Queue(env.getProperty("mq.direct.queue.one.name"), true);
    }
    @Bean(name = "directQueueTwo")
    public Queue directQueueTwo(){
        return new Queue(env.getProperty("mq.direct.queue.two.name"), true);
    }
    @Bean
    public Binding directBindingOne(){
        return BindingBuilder.bind(directQueueOne()).to(directExchange()).with(env.getProperty("mq.direct.routing.key.one.name"));
    }
    @Bean
    public Binding directBindingTwo(){
        return BindingBuilder.bind(directQueueTwo()).to(directExchange()).with(env.getProperty("mq.direct.routing.key.two.name"));
    }

    /**
     * 创建基于TopicExchange的消息模型
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(env.getProperty("mq.topic.exchange.name"), true, false);
    }
    @Bean(name = "topicQueueOne")
    public Queue topicQueueOne(){
        return new Queue(env.getProperty("mq.topic.queue.one.name"),true);
    }
    @Bean(name = "topicQueueTwo")
    public Queue topicQueueTwo(){
        return new Queue(env.getProperty("mq.topic.queue.two.name"),true);
    }
    @Bean
    public Binding topicBindingOne(){
        return BindingBuilder.bind(topicQueueOne()).to(topicExchange()).with(env.getProperty("mq.topic.routing.key.one.name"));
    }
    @Bean
    public Binding topicBindingTwo(){
        return BindingBuilder.bind(topicQueueTwo()).to(topicExchange()).with(env.getProperty("mq.topic.routing.key.two.name"));
    }

    /**
     * 单一消费者，确认模式为AUTO
     * @return
     */
    @Bean(name = "singleListenerContainerAuto")
    public SimpleRabbitListenerContainerFactory listenerContainerAuto(){
        //创建消息监听器所在的容器工厂实例
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //为容器工厂实例设置链接工厂
        factory.setConnectionFactory(connectionFactory);
        //设置消息传输格式为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置消费者并发实例，在这里采用单一的模式
        factory.setConcurrentConsumers(1);
        //设置消费者并发最大数量的实例
        factory.setMaxConcurrentConsumers(1);
        //设置消费者每个并发的实例预拉取的消息数据量
        factory.setPrefetchCount(1);
        //设置确认消费模式为自动确认消费
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //返回监听容器工厂实例
        return factory;
    }
    @Bean(name = "autoQueue")
    public Queue autoQueue(){
        return new Queue(env.getProperty("mq.auto.knowledge.queue.name"), true);
    }
    @Bean
    public DirectExchange autoExchange(){
        return new DirectExchange(env.getProperty("mq.auto.knowledge.exchange.name"), true,false);
    }
    @Bean
    public Binding autoBinding(){
        return BindingBuilder.bind(autoQueue()).to(autoExchange()).with(env.getProperty("mq.auto.knowledge.routing.key.name"));
    }

    /**
     * 单一消费者，确认模式为MANUAL
     * @return
     */
    @Bean(name = "manualQueue")
    public Queue manualQueue(){
        return new Queue(env.getProperty("mq.manual.knowledge.queue.name"), true);
    }
    @Bean
    public TopicExchange manualExchange(){
        return new TopicExchange(env.getProperty("mq.manual.knowledge.exchange.name"), true, false);
    }
    @Bean
    public Binding manualBinding(){
        return BindingBuilder.bind(manualQueue()).to(manualExchange()).with(env.getProperty("mq.manual.knowledge.routing.key.name"));
    }
    @Autowired
    private KnowledgeManualConsumer knowledgeManualConsumer;

    /**
     * 创建消费者监听器容器工厂实例，确认模式为MANUAL，并指定监听的队列和消费者
     * @param manualQueue
     * @return
     */
    @Bean(name = "simpleContainerManual")
    public SimpleMessageListenerContainer simpleContainer(@Qualifier("manualQueue") Queue manualQueue){
        //创建消息监听器容器工厂实例
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        //设置链接工厂
        container.setConnectionFactory(connectionFactory);
        //设置JSON消息传输格式
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        //单一消费者实例配置
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setPrefetchCount(1);
        //设置消息的确认消费模式，采用手动确认消费机制
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //指定该容器中监听的队列
        container.setQueues(manualQueue);
        //指定该容器中消息监听器，即消费者
        container.setMessageListener(knowledgeManualConsumer);
        //返回容器工厂实例
        return container;
    }

    /**
     * 用户登录成功后写日志消息模型配置
     */
    @Bean(name = "loginQueue")
    public Queue loginQueue(){
        return new Queue(env.getProperty("mq.login.queue.name"), true);
    }
    @Bean
    public TopicExchange loginExchange(){
        return new TopicExchange(env.getProperty("mq.login.exchange.name"), true, false);
    }
    @Bean
    public Binding loginBinding(){
        return BindingBuilder.bind(loginQueue()).to(loginExchange()).with((env.getProperty("mq.login.routing.key.name")));
    }

    /**
     * 死信队列消息模型构建配置
     * 创建死信队列
     * @return
     */
    @Bean
    public Queue basicDeadQueue(){
        //创建死信队列的组成成分 Map，用于存放组成成分的相关成员
        Map<String, Object> args = new HashMap<>();
        //创建死信交换机
        args.put("x-dead-letter-exchange", env.getProperty("mq.dead.exchange.name"));
        //创建死信路由
        args.put("x-dead-letter-routing.key", env.getProperty("mq.dead.routing.key.name"));
        //设置TTL，单位为ms
        args.put("x-message-ttl", 10000);
        //创建并范围返回死信队列实例
        return new Queue(env.getProperty("mq.dead.queue.name"), true, false,false, args);
    }

    /**
     * 创建基本消息模型的基本交换机，面向消费者
     * @return
     */
    @Bean
    public TopicExchange basicProducerExchange(){
        return new TopicExchange(env.getProperty("mq.producer.basic.exchange.name"), true, false);
    }

    /**
     * 创建基本消息模型的绑定，面向消费者
     * @return
     */
    @Bean
    public Binding basicProducerBinding(){
        return BindingBuilder.bind(basicDeadQueue()).to(basicProducerExchange()).with(env.getProperty("mq.producer.basic.routing.key.name"));
    }

    /**
     * 创建真正队列，面向消费者
     * @return
     */
    @Bean
    public Queue realConsumerQueue(){
        return new Queue(env.getProperty("mq.consumer.real.queue.name"), true);
    }

    /**
     * 创建死信交换机
     * @return
     */
    @Bean
    public TopicExchange basicDeadExchange(){
        return new TopicExchange(env.getProperty("mq.dead.exchange.name"), true, false);
    }

    /**
     * 创建死信队列的绑定
     * @return
     */
    @Bean
    public Binding basicDeadBinding(){
        return BindingBuilder.bind(realConsumerQueue()).to(basicDeadExchange()).with(env.getProperty("mq.dead.routing.key.name"));
    }


    /**
     * 构建下单超时应用的RabbitMQ死信队列模型
     *
     * 创建死信队列
     */
    @Bean
    public Queue orderDeadQueue(){
        //创建映射
        Map<String, Object> args = new HashMap<>();
        //添加死信交换机
        args.put("x-dead-letter-exchange", env.getProperty("mq.order.dead.exchange.name"));
        //添加死信路由
        args.put("x-dead-letter-routing-key", env.getProperty("mq.order.dead.routing.key.name"));
        //设置TTL（10s）
        args.put("x-message-ttl", 10000);

        return new Queue(env.getProperty("mq.order.dead.queue.name"), true, false, false, args);
    }

    /**
     * 基本交换机及绑定——面向生产者
     * @return
     */
    @Bean
    public TopicExchange orderProducerExchange(){
        return new TopicExchange(env.getProperty("mq.producer.order.exchange.name"),true,false);
    }

    @Bean
    public Binding orderProducerBinding(){
        return BindingBuilder.bind(orderDeadQueue()).to(orderProducerExchange()).with(env.getProperty("mq.producer.order.routing.key.name"));
    }

    /**
     * 真正队列——面向消费者
     */
    @Bean
    public Queue realOrderConsumerQueue(){
        return new Queue(env.getProperty("mq.consumer.order.real.queue.name"), true);
    }
    
    /**
     * 死信队列及绑定
     * @return
     */
    @Bean
    public TopicExchange basicOrderDeadExchange(){
        return new TopicExchange(env.getProperty("mq.order.dead.exchange.name"), true,false);
    }
    @Bean
    public Binding basicOrderDeadBinding(){
        return BindingBuilder.bind(realOrderConsumerQueue()).to(basicOrderDeadExchange()).with(env.getProperty("mq.order.dead.routing.key.name"));
    }

}

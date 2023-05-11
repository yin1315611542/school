package com.yanda.school.message;
/**
 * 消息通知模块
 * 功能介绍：用于系统消息通知的发起和接收，用户登录成功，修改信息，接单，退单，完成单子都会生成一条通知消息
 *         消息区分为已读和未读
 * 技术栈 ：RabbitMQ 消息中间件（消息队列）
 *                简介：是在消息的传输过程中保存消息的容器。在消息队列中，通常有生产者和消费者两个角色。生产者只负责发送数据到消息队列，谁从消息队列中取出数据处理，他不管。消费者只负责从消息队列中取出数据处理，他不管这是谁发送的数据。
 * 功能实现讲解：message模块中有两个类 MessageTask(消息接收和发送的实现类)，MessageEntity（消息实体类），两者的关系即：消息实体类 由 MessageTask 类发送给RabbitMQ
 *            MessageTask发送消息讲解：
 *             代码展示：
 *              channel.basicPublish("", topic, properties,MessageEntity);  此段代码是MessageTask类中发送消息的主体部分，第二个参数为要发送给RabbitMQ的哪个列（RabbitMQ不止一个对垒
 *              第三个参数表示消息属性（优先级，存活时间等；第四个参数是消息体
 *            MessageTask接收消息讲解
 *             代码展示
 *             channel.basicGet(topic, false); 第一个参数是从哪个队列接收消息
 **/


 //结构图见此包下的text包

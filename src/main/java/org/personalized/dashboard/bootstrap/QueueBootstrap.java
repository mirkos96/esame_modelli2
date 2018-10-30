package org.personalized.dashboard.bootstrap;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.personalized.dashboard.queue.ESIndexConsumer;
import org.personalized.dashboard.utils.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by sudan on 4/7/15.
 */
public class QueueBootstrap {

    private static QueueBootstrap queueBootstrap;
    private static boolean isInitialized = false;
    private final Logger LOGGER = LoggerFactory.getLogger(QueueBootstrap.class);
    private ESIndexConsumer esIndexConsumer;
    private MessageProducer messageProducer;
    private MessageConsumer messageConsumer;
    private Session session;

    private QueueBootstrap() {

    }

    public static void init() throws JMSException {
        if (!isInitialized) {
            queueBootstrap = new QueueBootstrap();
            queueBootstrap.setUp();
        }
    }

    public static QueueBootstrap getInstance() {
        return queueBootstrap;
    }

    private void setUp() throws JMSException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory
                (
                        ConfigKeys.QUEUE_USERNAME,
                        ConfigKeys.QUEUE_PASSWORD,
                        ConfigKeys.QUEUE_URL
                );
        Connection connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(ConfigKeys.QUEUE_DEST);
        messageProducer = session.createProducer(destination);
        messageConsumer = session.createConsumer(destination);
        new ESIndexConsumer();
        session.setMessageListener(esIndexConsumer);
        connection.start();
        isInitialized = true;

    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public MessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    public Session getSession() {
        return session;
    }

}

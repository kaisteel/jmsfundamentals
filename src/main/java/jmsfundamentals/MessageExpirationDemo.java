package jmsfundamentals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageExpirationDemo {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		Queue expiryQueue = (Queue) context.lookup("queue/ExpiryQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext();) {
			TextMessage message = jmsContext
					.createTextMessage("Arise awake and don't stop till you reach your destination");
			JMSProducer jmsProducer = jmsContext.createProducer();
			jmsProducer.setTimeToLive(2000L);
			jmsProducer.send(queue, message);
			//Added delay so that message can be expired
			Thread.sleep(5000L);
			JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
			String msgReceived = jmsConsumer.receiveBody(String.class,5000L);
			System.out.println(msgReceived);
			JMSConsumer expiryConsumer = jmsContext.createConsumer(expiryQueue);
			String expiryMsgReceived = expiryConsumer.receiveBody(String.class,5000L);
			System.out.println(expiryMsgReceived);
		}
	}

}

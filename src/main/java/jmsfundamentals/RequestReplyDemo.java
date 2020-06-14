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

public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		Queue queue = (Queue)context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue)context.lookup("queue/replyQueue");
		try(ActiveMQConnectionFactory cf=new ActiveMQConnectionFactory();JMSContext jmsContext = cf.createContext();){
			JMSProducer jmsProducer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("Arise awake and don't stop till you reach your destination");
			message.setJMSReplyTo(replyQueue);
			jmsProducer.send(queue, message);
			System.out.println(message.getJMSMessageID());
			
			JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
			TextMessage msgReceived =(TextMessage) jmsConsumer.receive();
			System.out.println(msgReceived.getText());
			System.out.println(msgReceived.getJMSMessageID());
			
			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("you are awesome");
			replyProducer.send(msgReceived.getJMSReplyTo(),replyMessage);
			replyProducer.setJMSCorrelationID(msgReceived.getJMSMessageID());
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			TextMessage receiveMessage = (TextMessage)replyConsumer.receive();
			System.out.println(receiveMessage.getText());
			System.out.println(receiveMessage.getJMSMessageID());
		}
	}

}

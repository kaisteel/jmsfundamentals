package jmsfundamentals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePriority {

	public static void main(String[] args) throws NamingException {
		InitialContext context = new InitialContext();
		Queue queue = (Queue)context.lookup("queue/myQueue");
		try(ActiveMQConnectionFactory cf=new ActiveMQConnectionFactory();JMSContext jmsContext = cf.createContext()){
			JMSProducer jmsProducer = jmsContext.createProducer();
			String[] messages=new String[3];
			messages[0]="Message 1";
			messages[1]="Message 2";
			messages[2]="Message 3";
			jmsProducer.setPriority(3);
			jmsProducer.send(queue, messages[0]);
			jmsProducer.setPriority(9);
			jmsProducer.send(queue, messages[1]);
			jmsProducer.setPriority(6);
			jmsProducer.send(queue, messages[2]);
			JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
			for (int i = 0; i < 4; i++) {
				String message = jmsConsumer.receiveBody(String.class);
				System.out.println("Received:"+message);
			}
		}
	}

}

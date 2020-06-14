package jmsfundamentals;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {

	public static void main(String[] args) {

		InitialContext initialContext = null;
		Connection connection = null;
		try {
			initialContext = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession();
			Topic topic = (Topic) initialContext.lookup("topic/myTopic");
			MessageProducer producer = session.createProducer(topic);
			TextMessage message = session
					.createTextMessage("All the power is within me. I can do anything and everything.");
			connection.start();

			MessageConsumer consumer1 = session.createConsumer(topic);
			MessageConsumer consumer2 = session.createConsumer(topic);
			producer.send(message);
			TextMessage message1 = (TextMessage) consumer1.receive();
			System.out.println("Consumer1 Message Received:" + message1.getText());
			TextMessage message2 = (TextMessage) consumer2.receive();
			System.out.println("Consumer2 Message Received:" + message2.getText());

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}

}


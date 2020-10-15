package rms.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class RMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(RMSApplication.class, args);
	}

	@Autowired
	private ConnectionFactory connectionFactory;
/*
	@Bean(name = "topicConnectionFactory")
	public DefaultJmsListenerContainerFactory topicConnectionFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setSessionTransacted(true);
		factory.setPubSubDomain(true);



		return factory;
	}
*/

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


}

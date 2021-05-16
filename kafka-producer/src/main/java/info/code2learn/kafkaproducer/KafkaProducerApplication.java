package info.code2learn.kafkaproducer;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class KafkaProducerApplication {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaProducerApplication.class);
	private static final String TOPIC = "topic1";
	
	private Integer counter = 0;
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args);
	}
	
	@Bean
    public NewTopic topic() {
        return TopicBuilder.name(TOPIC)
                .partitions(10)
                .replicas(1)
                .build();
    }
	
	@Scheduled(fixedDelay = 1000)
	public void produce() {
		String message = "Hello " + counter++;
		log.info("Sending message to topic : {}, message: {}", TOPIC, message);
		kafkaTemplate.send(TOPIC, String.valueOf(message.hashCode()), message);
	}

}

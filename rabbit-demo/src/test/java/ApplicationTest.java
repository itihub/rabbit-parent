import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.api.MessageBuilder;
import com.itihub.rabbit.api.MessageType;
import com.itihub.rabbit.producer.broker.ProducerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Autowired
    private ProducerClient producerClient;

    @Test
    public void testProducerClient() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            String uniqueId = UUID.randomUUID().toString();
            HashMap<String, Object> attributes = new HashMap<>();
            attributes.put("name", "张三");
            attributes.put("age", 18);
            Message message = MessageBuilder.create()
                    .withMessageId(uniqueId)
                    .withAttributes(attributes)
                    .withTopic("exchange-1")
                    .withRoutingKey("springboot.abc")
                    .withMessageType(MessageType.RELIANT)
                    .withDelayMills(5000)
                    .build();
            producerClient.send(message);
        }

        Thread.sleep(100000L);

    }

}

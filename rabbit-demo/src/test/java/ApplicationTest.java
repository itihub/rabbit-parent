import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.api.MessageBuilder;
import com.itihub.rabbit.api.MessageType;
import com.itihub.rabbit.api.SendCallback;
import com.itihub.rabbit.demo.DemoApplication;
import com.itihub.rabbit.producer.broker.ProducerClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest(classes = DemoApplication.class)
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

    @Test
    public void testProducerSendCallbackMessage() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            String uniqueId = UUID.randomUUID().toString();
            HashMap<String, Object> attributes = new HashMap<>();
            attributes.put("name", "带回调的消息测试");
            attributes.put("age", 23);
            Message message = MessageBuilder.create()
                    .withMessageId(uniqueId)
                    .withAttributes(attributes)
                    .withTopic("exchange-1")
                    .withRoutingKey("springboot.abc")
                    .withMessageType(MessageType.RELIANT)
                    .withDelayMills(5000)
                    .build();
            producerClient.send(message, new SendCallback() {
                @Override
                public void onSuccess() {
                    log.info("触发了成功回调参数");
                }

                @Override
                public void onFailure() {
                    log.info("触发了失败回调参数");
                }
            });
        }

        Thread.sleep(100000L);
    }

    @Test
    public void testProducerSendMessages(){

        List<Message> messageList = new ArrayList<Message>() {
            {
                add(MessageBuilder.create()
                        .withMessageId(UUID.randomUUID().toString())
                        .withTopic("exchange-1")
                        .withRoutingKey("springboot.abc")
                        .build());
                add(MessageBuilder.create()
                        .withMessageId(UUID.randomUUID().toString())
                        .withTopic("exchange-1")
                        .withRoutingKey("springboot.abc")
                        .build());
            }
        };
        producerClient.send(messageList);

    }

}

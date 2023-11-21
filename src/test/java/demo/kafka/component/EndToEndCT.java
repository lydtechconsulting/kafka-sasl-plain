package demo.kafka.component;

import java.util.List;

import demo.kafka.event.DemoInboundEvent;
import demo.kafka.util.TestData;
import dev.lydtech.component.framework.client.kafka.KafkaClient;
import dev.lydtech.component.framework.extension.ComponentTestExtension;
import dev.lydtech.component.framework.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(ComponentTestExtension.class)
public class EndToEndCT {

    private static final String GROUP_ID = "EndToEndCT";

    private Consumer consumer;

    @BeforeEach
    public void setup() {
        consumer = KafkaClient.getInstance().initConsumer(GROUP_ID, "demo-outbound-topic", 3L);
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    /**
     * Send in multiple events and ensure an outbound event is emitted for each.
     */
    @Test
    public void testFlow() throws Exception {
        int totalMessages = 100;
        for (long counter=1; counter<=totalMessages; counter++) {
            DemoInboundEvent testEvent = TestData.buildDemoInboundEvent(counter);
            KafkaClient.getInstance().sendMessage("demo-inbound-topic", null, JsonMapper.writeToJson(testEvent));
        }
        List<ConsumerRecord<String, String>> outboundEvents = KafkaClient.getInstance().consumeAndAssert("testFlow", consumer, totalMessages, 3);
    }
}

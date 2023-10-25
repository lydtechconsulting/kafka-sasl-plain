package demo.kafka.service;

import demo.kafka.event.DemoInboundEvent;
import demo.kafka.event.DemoOutboundEvent;
import demo.kafka.producer.KafkaDemoProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemoService {

    @Autowired
    private final KafkaDemoProducer kafkaProducer;

    public void process(DemoInboundEvent event) throws Exception {
        log.info("Processing inbound event:"+event.getSequenceNumber());
        sendEvent();
    }

    /**
     * Emits an outbound event with a payload of a randomly generated name.
     */
    private void sendEvent() throws Exception {
        DemoOutboundEvent demoEvent = DemoOutboundEvent.builder()
                .sequenceNumber(1L)
                .build();
        kafkaProducer.sendMessage(demoEvent);
    }
}

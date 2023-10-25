package demo.kafka.util;

import demo.kafka.event.DemoInboundEvent;

public class TestData {

    public static DemoInboundEvent buildDemoInboundEvent(Long sequenceNumber) {
        return DemoInboundEvent.builder()
                .sequenceNumber(sequenceNumber)
                .build();
    }
}

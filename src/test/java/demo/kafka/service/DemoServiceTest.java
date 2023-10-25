package demo.kafka.service;

import demo.kafka.event.DemoInboundEvent;
import demo.kafka.event.DemoOutboundEvent;
import demo.kafka.producer.KafkaDemoProducer;
import demo.kafka.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DemoServiceTest {

    private KafkaDemoProducer kafkaDemoProducerMock;
    private DemoService service;

    @BeforeEach
    public void setUp() {
        kafkaDemoProducerMock = mock(KafkaDemoProducer.class);
        service = new DemoService(kafkaDemoProducerMock);
    }

    /**
     * Ensure the Kafka producer is called to emit a message.
     */
    @Test
    public void testProcess() throws Exception {
        DemoInboundEvent testEvent = TestData.buildDemoInboundEvent(1L);

        service.process(testEvent);

        verify(kafkaDemoProducerMock, times(1)).sendMessage(any(DemoOutboundEvent.class));
    }

    /**
     * The send to Kafka throws an exception (e.g. if Kafka is unavailable).
     */
    @Test
    public void testProcess_ProducerThrowsException() throws Exception {
        DemoInboundEvent testEvent = TestData.buildDemoInboundEvent(1L);
        doThrow(new RuntimeException("Producer failure")).when(kafkaDemoProducerMock).sendMessage(any(DemoOutboundEvent.class));

        Exception exception = assertThrows(RuntimeException.class, () -> service.process(testEvent));

        verify(kafkaDemoProducerMock, times(1)).sendMessage(any(DemoOutboundEvent.class));
        assertThat(exception.getMessage(), equalTo("Producer failure"));
    }
}

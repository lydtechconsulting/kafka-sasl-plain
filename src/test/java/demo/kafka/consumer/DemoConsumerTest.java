package demo.kafka.consumer;

import demo.kafka.event.DemoInboundEvent;
import demo.kafka.service.DemoService;
import demo.kafka.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DemoConsumerTest {

    private DemoService serviceMock;
    private DemoConsumer consumer;

    @BeforeEach
    public void setUp() {
        serviceMock = mock(DemoService.class);
        consumer = new DemoConsumer(serviceMock);
    }

    /**
     * Ensure that the JSON message is successfully passed on to the service.
     */
    @Test
    public void testListen_Success() throws Exception {
        DemoInboundEvent testEvent = TestData.buildDemoInboundEvent(1L);

        consumer.listen(testEvent);

        verify(serviceMock, times(1)).process(testEvent);
    }

    /**
     * If an exception is thrown, an error is logged but the processing completes successfully.
     *
     * This ensures the consumer offsets are updated so that the message is not redelivered.
     */
    @Test
    public void testListen_ServiceThrowsException() throws Exception {
        DemoInboundEvent testEvent = TestData.buildDemoInboundEvent(1L);

        doThrow(new Exception("Service failure")).when(serviceMock).process(testEvent);

        consumer.listen(testEvent);

        verify(serviceMock, times(1)).process(testEvent);
    }
}

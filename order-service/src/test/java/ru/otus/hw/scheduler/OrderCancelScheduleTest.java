package ru.otus.hw.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.hw.config.ScheduledConfig;
import ru.otus.hw.services.OrderService;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(ScheduledConfig.class)
class OrderCancelScheduleTest {

    @SpyBean
    private OrderCancelSchedule orderCancelSchedule;

    @MockBean
    private OrderService orderService;

    @Test
    public void shouldRunScheduler() throws InterruptedException {
        Thread.sleep(31000);
        verify(orderCancelSchedule, atLeast(1))
                .cancelUnpaidOrders();
    }
}
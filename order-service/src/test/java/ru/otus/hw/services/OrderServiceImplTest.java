package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.kafka.ProducerService;
import ru.otus.hw.mappers.ItemMapper;
import ru.otus.hw.mappers.ItemMapperImpl;
import ru.otus.hw.mappers.OrderMapper;
import ru.otus.hw.mappers.OrderMapperImpl;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.ItemRepository;
import ru.otus.hw.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка работы сервиса заказов")
@SpringBootTest(classes = {
        OrderServiceImpl.class,
        OrderMapperImpl.class,
        ItemMapperImpl.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceImplTest {

    private static Order expectedOrder;

    private static OrderDto expectedOrderDto;

    private static List<Item> expectedItems = new ArrayList<>();

    private static List<ItemDto> expectedItemsDto = new ArrayList<>();

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ItemMapper itemMapper;

    @MockBean
    private ProducerService producerService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ItemRepository itemRepository;

    @BeforeEach
    void setExpectedOrder() {
        expectedItems = List.of(
                new Item(1L,
                        "testItem_1",
                        "testCategory_1",
                        1,
                        BigDecimal.valueOf(999.99)),
                new Item(2L,
                        "testItem_2",
                        "testCategory_2",
                        1,
                        BigDecimal.valueOf(555.55)));
        expectedOrder = new Order(1L,
                1L,
                "order_number",
                OrderState.CREATED,
                LocalDateTime.now(),
                expectedItems);
        expectedOrderDto = orderMapper.toDto(expectedOrder);
        expectedItemsDto = expectedItems
                .stream()
                .map(itemMapper::toDto)
                .toList();
    }

    @DisplayName("должен находить заказ по номеру")
    @Test
    void shouldFindOrderByOrderNumber() {
        doReturn(Optional.of(expectedOrder))
                .when(orderRepository)
                .findOrderByOrderNumber(anyString());
        var actualOrder =
                orderService.findOrderByOrderNumber("order_number");

        assertEquals(expectedOrderDto, actualOrder);
    }

    @DisplayName("должен создавать заказ")
    @Test
    void shouldCreateNewOrder() {
        doReturn(expectedItems)
                .when(itemRepository)
                .findByIdIn(any());
        doReturn(expectedOrder)
                .when(orderRepository)
                .save(any());
        var newOrder = new OrderCreateDto(1L, expectedItemsDto);
        var actualOrder = orderService.create(newOrder);

        assertEquals(expectedOrderDto, actualOrder);
        verify(orderRepository, times(2)).save(any());
    }

    @DisplayName("должен получать список неоплаченных заказов")
    @Test
    void shouldGetUnpaidOrders() {
        doReturn(List.of(expectedOrder))
                .when(orderRepository)
                .findOrderByState(OrderState.PUBLISHED);
        var actualOrders = orderService.getUnpaidOrders();

        assertEquals(expectedOrder, actualOrders.get(0));
        verify(orderRepository, times(1)).findOrderByState(any());
    }

    @DisplayName("должен отменять заказ")
    @Test
    void shouldCancelByOrderNumber() {
        doReturn(Optional.of(expectedOrder))
                .when(orderRepository)
                .findOrderByOrderNumber(anyString());
        doReturn(expectedOrder)
                .when(orderRepository)
                .save(any());
        var event = new OrderEventDto(
                expectedOrder.getUserId(),
                OrderState.CANCELED.toString(),
                expectedOrder.getOrderNumber(),
                BigDecimal.ZERO);
        orderService.cancelByOrderNumber("order_number");

        verify(producerService, times(1)).sendOrderEvent(event);
        verify(orderRepository, times(1)).save(any());
    }
}
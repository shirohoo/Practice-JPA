package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.dto.web.OrderSearchDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * 주문 리스트 조회 API V1 <br>
     * Entity를 직접 사용하는 경우 <br>
     * Hibernate5Module 라이브러리를 추가하여 강제 지연로딩 옵션을 적용하였음 <br>
     * 번거롭기도 하고 모든 쿼리가 다 날아가므로 성능이슈 발생 가능성 높음 <br><br>
     * Apllication.class → <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;{@code hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);} <br>
     * 주석처리 된 상태 <br>
     *
     * @return List<Order>
     * @see GET
     * @see 이렇게_하지말자
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findOrders(new OrderSearchDto());
        orders.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName());
        });
        return orders;
    }

    /**
     * 주문 조회 API V2 <br>
     * Entity가 직접 응답되던 방식에서 응답용 DTO를 따로 작성하였음 <br>
     * 이렇게 작성할 경우 Side Effect가 적어 유지보수에 좋다.
     *
     * @return List<OrderResponseDto>
     * @see GET
     */
    @GetMapping("/api/v2/orders")
    public List<OrderResponseDto> ordersV2() {
        return orderRepository.findOrders(new OrderSearchDto()).stream()
                              .map(o -> new OrderResponseDto(o))
                              .collect(Collectors.toList());
    }

    /**
     * 주문 조회 API V3 <br>
     * N + 1 문제를 해결하기 위해 JPQL fetch join을 활용하였다. <br>
     * <p>
     * 다만 Collection fetch join 의 경우 Paging을 하면 안되고 <br>
     * (Paging을 하면 모든 데이터를 메모리에 퍼올려 메모리상에서 작업하므로 Out of Memory 가능성) <br>
     * <br>
     * fetch join도 한번만 사용해야 하기에 제약이 많다. <br>
     * (1:N:N - - - 이 되므로 데이터의 정합성이 깨질 수 있다.)
     * </p>
     *
     * @return List<OrderResponseDto>
     * @see GET
     */
    @GetMapping("/api/v3/orders")
    public List<OrderResponseDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream()
                     .map(o -> new OrderResponseDto(o))
                     .collect(Collectors.toList());
    }

    /**
     * 주문 조회 API V3.1 <br>
     * N + 1 문제를 해결하기 위해 JPQL fetch join을 활용하였다. <br>
     * 또한 xToOne 관계는 fetch join 하여 가져오고, ~ToMany 관계는 batchSize를 통해 가져오므로 <br>
     * 성능 최적화와 페이징을 같이 사용할 수 있다. <br>
     * 마지막으로, Result 클래스로 한번 더 래핑하여 확장에 유연한 구조를 만들었다.
     * </p>
     *
     * @return Result
     * @see GET
     */
    @GetMapping("/api/v3.1/orders")
    public Result ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return new Result(orders.stream()
                                .map(o -> new OrderResponseDto(o))
                                .collect(Collectors.toList()));
    }

    /**
     * 응답용 DTO를 래핑할 클래스. <br>
     * 요구사항이 변경될 경우를 대비해 확장에 용이하도록 한번 더 감쌈
     *
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Result<T> {

        private T data;

    }

    /**
     * 주문 조회를 위한 DTO
     */
    @Data
    private class OrderResponseDto {

        private Long orderId;

        private String name;

        private LocalDateTime orderDateTime;

        private OrderStatus orderStatus;

        private Address address;

        private List<OrderItemResponseDto> orderItems;

        public OrderResponseDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDateTime = order.getOrderDateTime();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                                   .map(o -> new OrderItemResponseDto(o))
                                   .collect(Collectors.toList());
        }

        @Data
        private class OrderItemResponseDto {

            private String itemName;

            private int orderPrice;

            private int count;

            public OrderItemResponseDto(OrderItem orderItem) {
                this.itemName = orderItem.getItem().getName();
                this.orderPrice = orderItem.getOrderPrice();
                this.count = orderItem.getCount();
            }

        }

    }

}

package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.web.OrderSearchDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 주문 조회 API V1 <br>
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
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findOrders(new OrderSearchDto());
        orders.forEach(order -> {
            order.getMember().getName(); // 프록시 강제 초기화 -> 호출 시(사용 시) 프록시가 초기화 되는 점을 이용
            order.getDelivery().getAddress(); // 프록시 강제 초기화 -> 호출 시(사용 시) 프록시가 초기화 되는 점을 이용
        });
        return orders;
    }

    /**
     * 주문 조회 API V2 <br>
     * Entity가 직접 응답되던 방식에서 응답용 DTO를 따로 작성하였고 한번 래핑하여 유연성을 크게 늘림 <br>
     * 이렇게 작성할 경우 Side Effect가 적어 유지보수에 좋고 <br>
     * 요구사항이 변경되더라도 확장하기 용이하다.
     *
     * @return Result
     * @see GET
     */
    @GetMapping("/api/v2/simple-orders")
    public Result membersV2() {
        List<Order> orders = orderRepository.findOrders(new OrderSearchDto());
        List<SimpleOrderDto> collect = orders.stream()
                                             .map(SimpleOrderDto :: new)
                                             .collect(Collectors.toList());
        return new Result(collect);
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
    @AllArgsConstructor
    static class SimpleOrderDto {

        private Long orderId;

        private String name;

        private LocalDateTime orderDateTime;

        private OrderStatus orderStatus;

        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDateTime = order.getOrderDateTime();
            this.orderStatus = order.getStatus();
            this.address = order.getMember().getAddress();
        }

    }

}

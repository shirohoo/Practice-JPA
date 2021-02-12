package jpabook.jpashop.dto.domain;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.type.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDto {

    private Long id;

    private Member member;

    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    private Delivery delivery;

    private LocalDateTime orderDateTime;

    private OrderStatus status;

    /**
     * DTO를 엔티티로 복사
     * @param dto OrderDto
     * @return Order
     */
    public Order toEntity(OrderDto dto) {
        return Order.builder()
                    .id(dto.getId())
                    .member(dto.getMember())
                    .orderItems(dto.getOrderItems())
                    .delivery(dto.getDelivery())
                    .orderDateTime(dto.getOrderDateTime())
                    .status(dto.getStatus())
                    .build();
    }

}

package jpabook.jpashop.dto.domain;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemDto {

    private Long id;

    private Item item;

    private Order order;

    private int orderPrice;

    private int count;

    /**
     * DTO를 엔티티로 복사
     * @param dto OrderItemDto
     * @return OrderItem
     */
    public OrderItem toEntity(OrderItemDto dto) {
        return OrderItem.builder()
                        .id(dto.getId())
                        .item(dto.getItem())
                        .order(dto.getOrder())
                        .orderPrice(dto.getOrderPrice())
                        .count(dto.getCount())
                        .build();
    }

}

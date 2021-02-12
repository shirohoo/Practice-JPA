package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.domain.OrderItemDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    private int orderPrice;

    private int count;

    /**
     * 엔티티를 DTO로 복사
     * @param entity OrderItem
     * @return OrderItemDto
     */
    public OrderItemDto toDto(OrderItem entity) {
        return OrderItemDto.builder()
                           .id(entity.getId())
                           .item(entity.getItem())
                           .order(entity.getOrder())
                           .orderPrice(entity.getOrderPrice())
                           .count(entity.getCount())
                           .build();
    }

    protected void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 주문 취소
     */
    public void orderCancle() {
        getItem().addStock(count);
    }

    /**
     * 개별 아이템의 총 가격 조회
     * @return orderPrice * count
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    /**
     * 개별 주문 정보 생성
     * @param item Item
     * @param orderPrice int
     * @param count int
     * @return OrderItem
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = OrderItem.builder()
                                       .item(item)
                                       .orderPrice(orderPrice)
                                       .count(count)
                                       .build();

        item.revertStock(count);

        return orderItem;
    }

}

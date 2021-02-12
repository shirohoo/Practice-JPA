package jpabook.jpashop.domain;

import jpabook.jpashop.dto.domain.OrderDto;
import jpabook.jpashop.type.DeliveryStatus;
import jpabook.jpashop.type.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 주문 정보
 * @see ENTITY
 */
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * Lombok 사용 시 Default 값을 선언해줘도
     * Builder 패턴을 사용하면 기본값이 null이 되므로
     *{@literal @}Builder.Default을 명시해야
     * null이 아닌 ArrayList가 생성된다.
     */
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 엔티티를 DTO로 복사
     * @param entity Order
     * @return OrderDto
     */
    public OrderDto toDto(Order entity) {
        return OrderDto.builder()
                       .id(entity.getId())
                       .member(entity.getMember())
                       .orderItems(entity.getOrderItems())
                       .delivery(entity.getDelivery())
                       .orderDateTime(entity.getOrderDateTime())
                       .status(entity.getStatus())
                       .build();
    }

    protected void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /**
     * 주문에 개별 주문 목록 등록
     * @param orderItem OrderItem
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    protected void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    protected void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * 주문정보를 받아 주문 생성
     * @param member     Member
     * @param delivery   Delivery
     * @param orderItems OrderItem...
     * @return Order
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.builder()
                           .member(member)
                           .delivery(delivery)
                           .status(OrderStatus.ORDER)
                           .orderDateTime(LocalDateTime.now())
                           .build();

        Arrays.stream(orderItems)
              .forEach(order :: addOrderItem);

        return order;
    }

    /**
     * 주문 취소
     */
    public void orderCancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료 된 상품은 주문 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        orderItems.forEach(OrderItem :: orderCancle);
    }

    /**
     * 해당 주문 건의 총 가격 조회
     * @return orderItems.sum()
     */
    public int getTotalPrice() {
        return orderItems.stream()
                         .mapToInt(OrderItem :: getTotalPrice)
                         .sum();
    }

}

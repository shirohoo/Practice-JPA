package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.web.OrderSearchDto;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    /**
     * 주문 정보 생성 및 저장
     *
     * @param memberId Long
     * @param itemId   Long
     * @param count    int
     * @return 주문 ID
     */
    @Transactional(readOnly = false)
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member findMember = memberRepository.findById(memberId);
        Item findItem = itemRepository.findById(itemId);

        // 배송정보 생성
        Delivery delivery = Delivery.builder()
                                    .address(findMember.getAddress())
                                    .build();

        // 주문정보 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);

        // 주문정보 저장
        Order order = Order.createOrder(findMember, delivery, orderItem);

        // Delivery와 OrderItem은 CascadeType.ALL로 설정되어 있으므로 Order만 persist하면 같이 persist된다.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     *
     * @param id Long
     */
    @Transactional(readOnly = false)
    public void cancelOrder(Long id) {
        Order findOrder = orderRepository.findById(id);
        findOrder.orderCancel();
    }

    /**
     * 주문 검색
     *
     * @param orderSearch OrderSearchDto
     * @return List<Order>
     */
    public List<Order> findOrders(OrderSearchDto orderSearch) {
        return orderRepository.findOrders(orderSearch);
    }

}

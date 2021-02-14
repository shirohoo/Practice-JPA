package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /**
     * fetch join으로 N + 1문제 해결
     *
     * @return List<Order>
     * @see fetch
     */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("SELECT o FROM Order o " +
                              "JOIN FETCH o.member m " +
                              "JOIN FETCH o.delivery d",
                              Order.class)
                 .getResultList();
    }

    /**
     * join하여 DTO 생성자에 필요한 인자만 전달
     *
     * @return List<OrderSimpleQueryDto>
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("SELECT new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(" +
                              "o.id, " +
                              "m.name, " +
                              "o.orderDateTime, " +
                              "o.status, " +
                              "d.address) " +
                              "FROM Order o " +
                              "JOIN o.member m " +
                              "JOIN o.delivery d",
                              OrderSimpleQueryDto.class)
                 .getResultList();
    }

}

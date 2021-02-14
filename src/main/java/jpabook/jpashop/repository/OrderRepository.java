package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.web.OrderSearchDto;
import jpabook.jpashop.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 검색 기능
     * 조건 입력에 따라 분기
     *
     * @param ordersearch OrderSearchDto
     * @return List<Order>
     */
    public List<Order> findOrders(OrderSearchDto ordersearch) {
        String memberName = ordersearch.getMemberName();
        OrderStatus orderStatus = ordersearch.getOrderStatus();

        if(!("".equals(memberName)) && orderStatus != null) {
            return em.createQuery("SELECT o FROM Order o JOIN o.member m WHERE o.status = :status AND m.name LIKE :name", Order.class)
                     .setParameter("status", orderStatus)
                     .setParameter("name", memberName)
                     .setMaxResults(1000)
                     .getResultList();
        }
        else if("".equals(memberName) && orderStatus != null) {
            return em.createQuery("SELECT o FROM Order o JOIN o.member m WHERE o.status = :status", Order.class)
                     .setParameter("status", orderStatus)
                     .setMaxResults(1000)
                     .getResultList();
        }
        else {
            return em.createQuery("SELECT o FROM Order o JOIN o.member m", Order.class)
                     .setMaxResults(1000)
                     .getResultList();
        }
    }

    public List<Order> findAllWithItem() {
        return em.createQuery("SELECT DISTINCT o FROM Order o " +
                              "JOIN FETCH o.member m " +
                              "JOIN FETCH o.delivery d " +
                              "JOIN FETCH o.orderItems oi " +
                              "JOIN FETCH oi.item i",
                              Order.class)
                 .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "SELECT o FROM Order o " +
                "JOIN FETCH o.member m " +
                "JOIN FETCH o.delivery d", Order.class)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

}

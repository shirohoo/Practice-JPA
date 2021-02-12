package jpabook.jpashop.service;

import jpabook.jpashop.ApplicationTests;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.web.OrderSearchDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.type.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class OrderServiceTest extends ApplicationTests {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceTest.class);

    @Test
    @DisplayName("상품주문_신규")
    public void productOrders() throws Exception {
        // given
        Member member = createMember();
        Item book = createItem(10);

        em.persist(member);
        em.persist(book);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findById(orderId);
        assertThat(OrderStatus.ORDER).isEqualTo(findOrder.getStatus());
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(findOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(8);

    }

    @Test
    @DisplayName("상품주문_취소")
    public void orderCancel() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem(10);

        em.persist(member);
        em.persist(item);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findById(orderId);
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품주문_재고수량초과")
    public void inventoryQuantityExceeded() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem(10);

        em.persist(member);
        em.persist(item);

        int orderCount = 11;

        // when
        Exception exception = assertThrows(NotEnoughStockException.class,
                                           () -> orderService.order(member.getId(), item.getId(), orderCount));

        // then
        log.error(() -> "ERROR MESSAGE = " + exception.getMessage());
    }

    @Test
    @DisplayName("주문검색")
    public void orderSearch() throws Exception {
        // given
        Member member = createMember();
        Item book = createItem(10);

        em.persist(member);
        em.persist(book);

        int orderCount = 2;

        OrderSearchDto orderSearch = new OrderSearchDto();
        orderSearch.setMemberName("한창훈");
        orderSearch.setOrderStatus(OrderStatus.ORDER);

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        List<Order> orders = orderService.findOrders(orderSearch);

        // then
        assertThat(orders.get(0).getMember().getName()).isEqualTo("한창훈");
        assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    //<!-- 테스트용 생성 메서드 -->//
    private Member createMember() {
        Address address = Address.builder()
                                 .city("서울시")
                                 .street("은평구")
                                 .zipcode("15212")
                                 .build();

        Member member = Member.builder()
                              .name("한창훈")
                              .address(address)
                              .build();
        return member;
    }

    private Item createItem(int stockQuantity) {
        Item book = Book.builder()
                        .name("JPA")
                        .price(10000)
                        .stockQuantity(stockQuantity)
                        .build();
        return book;
    }

}
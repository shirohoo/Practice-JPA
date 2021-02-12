package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.web.OrderSearchDto;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final MemberService memberService;

    private final ItemService itemService;

    /**
     * 주문 생성 페이지
     * @return orderForm.html
     * @see GET
     */
    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findAll();
        List<Item> items = itemService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    /**
     * 주문 생성
     * @param memberId Long
     * @param itemId Long
     * @param count int
     * @return 주문요청 페이지로 리다이렉트
     * @see POST
     */
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/order";
    }

    /**
     * 주문 목록 조회 페이지
     * @param orderSearch 주문자 이름, 주문 상태
     * @return orderList.html
     * @see GET
     */
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearchDto orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    /**
     * 주문 취소
     * @param orderId Long
     * @return 주문 목록 조회 페이지로 리다이렉트
     * @see POST
     */
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}

package jpabook.jpashop.dto.web;

import jpabook.jpashop.type.OrderStatus;
import lombok.*;

/**
 * 주문 조회 시 클라이언트에서 넘어오는 Form
 * @see DTO
 */
@Getter @Setter
public class OrderSearchDto {

    private String memberName;

    private OrderStatus orderStatus;

}

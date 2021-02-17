package jpabook.jpashop.dto.web;

import jpabook.jpashop.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주문 조회 시 클라이언트에서 넘어오는 Form
 *
 * @see DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDto {

    private String memberName;

    private OrderStatus orderStatus;

}

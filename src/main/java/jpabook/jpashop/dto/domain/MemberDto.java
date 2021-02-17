package jpabook.jpashop.dto.domain;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;

    private String name;

    private Address address;

    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    /**
     * DTO를 엔티티로 복사
     * @param dto MemberDto
     * @return Member
     */
    public Member toEntity(MemberDto dto) {
        return Member.builder()
                     .id(dto.getId())
                     .name(dto.getName())
                     .address(dto.getAddress())
                     .orders(dto.getOrders())
                     .build();
    }

}

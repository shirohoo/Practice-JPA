package jpabook.jpashop.dto.domain;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.type.DeliveryStatus;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDto {

    private Long id;

    private Order order;

    private Address address;

    private DeliveryStatus status;

    /**
     * DTO를 엔티티로 복사
     * @param dto DeliveryDto
     * @return Delivery
     */
    public Delivery toEntity(DeliveryDto dto) {
        return Delivery.builder()
                       .id(dto.getId())
                       .order(dto.getOrder())
                       .address(dto.getAddress())
                       .status(dto.getStatus())
                       .build();
    }

}

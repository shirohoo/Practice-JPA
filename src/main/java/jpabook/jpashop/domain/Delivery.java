package jpabook.jpashop.domain;

import jpabook.jpashop.dto.domain.DeliveryDto;
import jpabook.jpashop.type.DeliveryStatus;
import lombok.*;

import javax.persistence.*;

/**
 * 배달 정보
 * @see ENTITY
 */
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    /**
     * 엔티티를 DTO로 복사
     *
     * @param entity Delivery
     * @return DeliveryDto
     */
    public DeliveryDto toDto(Delivery entity) {
        return DeliveryDto.builder()
                          .id(entity.getId())
                          .order(entity.getOrder())
                          .address(entity.getAddress())
                          .status(entity.getStatus())
                          .build();
    }

    protected void setOrder(Order order) {
        this.order = order;
    }

}

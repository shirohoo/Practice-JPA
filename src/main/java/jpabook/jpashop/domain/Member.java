package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.dto.domain.MemberDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보
 *
 * @see ENTITY
 */
@Entity
@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    /**
     * 엔티티를 DTO로 복사
     *
     * @param entity Member
     * @return MemberDto
     */
    public MemberDto toDto(Member entity) {
        return MemberDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .address(entity.getAddress())
                        .orders(entity.getOrders())
                        .build();
    }

}

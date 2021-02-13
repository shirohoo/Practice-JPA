package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * 회원 주소 데이터 내장 객체 (컬렉션)
 * @see Embeddable
 */
@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    private String city;

    private String street;

    private String zipcode;

}
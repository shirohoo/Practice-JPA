package jpabook.jpashop.dto.web;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * 회원 가입 시 클라이언트에서 넘어오는 Form
 *
 * @see DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFormDto {

    @NotEmpty(message = "이름을 입력하세요 !")
    private String name;

    private String city;

    private String street;

    private String zipcode;

}
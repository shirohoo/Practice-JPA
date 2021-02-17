package jpabook.jpashop.dto.web;

import lombok.*;

/**
 * 상품 등록 시 클라이언트에서 넘어오는 Form
 *
 * @see DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFormDto {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private String author;

    private String isbn;

}
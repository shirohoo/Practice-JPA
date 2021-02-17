package jpabook.jpashop.dto.domain;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.item.Item;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    @Builder.Default
    private List<Item> items = new ArrayList<>();

    private Category parent;

    @Builder.Default
    private List<Category> child = new ArrayList<>();

    /**
     * DTO를 엔티티로 복사
     * @param dto CategoryDto
     * @return Category
     */
    public Category toEntity(CategoryDto dto) {
        return Category.builder()
                       .id(dto.getId())
                       .name(dto.getName())
                       .items(dto.getItems())
                       .parent(dto.getParent())
                       .child(dto.getChild())
                       .build();
    }

}

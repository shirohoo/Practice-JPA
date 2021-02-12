package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.domain.CategoryDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 카테고리
 * @see ENTITY
 */
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "category_item",
               joinColumns = @JoinColumn(name = "category_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    /**
     * 엔티티를 DTO로 복사
     * @param entity Category
     * @return CategoryDto
     */
    public CategoryDto toDto(Category entity) {
        return CategoryDto.builder()
                          .id(entity.getId())
                          .name(entity.getName())
                          .items(entity.getItems())
                          .parent(entity.getParent())
                          .child(entity.getChild())
                          .build();
    }

    protected void setParent(Category parent) {
        this.parent = parent;
    }

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}

package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

/**
 * {@literal @}DiscriminatorValue 상속관계 표시를 위한 컬럼, <br>
 * 따로 정의하지 않을 경우 테이블 명으로 정해짐
 */
@Entity
@DiscriminatorValue("BOOK")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

    private String author;

    private String isbn;

    @Builder
    public Book(Long id, String name, int price, int stockQuantity, List<Category> categories, String author, String isbn) {
        super(id, name, price, stockQuantity, categories);
        this.author = author;
        this.isbn = isbn;
    }

}

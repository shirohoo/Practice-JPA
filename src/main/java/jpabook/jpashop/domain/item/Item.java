package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@literal @}Inheritance 상속관계 매핑시 이를 선언하여 상속관계 전략을 설정해야 함
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 재고 수량 증가
     *
     * @param quantity int
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     * 재고 없을 시 예외 발생
     *
     * @param quantity int
     */
    public void revertStock(int quantity) {
        int freeStock = this.stockQuantity - quantity;
        if(freeStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = freeStock;
    }

}

package jpabook.jpashop.service;

import jpabook.jpashop.ApplicationTests;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ItemServiceTest extends ApplicationTests {

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("상품등록")
    public void save() throws Exception {
        // given
        Item book = Book.builder()
                        .id(1L)
                        .name("JPA")
                        .price(10000)
                        .stockQuantity(10)
                        .build();

        // when
        Long savedId = itemService.save(book);

        // then
        assertThat(book.getId()).isEqualTo(savedId);
        assertThat(book.getName()).isEqualTo("JPA");
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품검색_단건")
    public void findById() throws Exception {
        // given
        Item book = Book.builder()
                        .name("JPA")
                        .price(10000)
                        .stockQuantity(10)
                        .build();

        Long savedId = itemService.save(book);

        // when
        Item findItem = itemService.findById(savedId);

        // then
        assertThat(findItem.getId()).isEqualTo(savedId);
        assertThat(findItem.getName()).isEqualTo("JPA");
        assertThat(findItem.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품검색_리스트")
    public void findItems() throws Exception {
        // given
        Item book = Book.builder()
                        .name("JPA")
                        .price(10000)
                        .stockQuantity(10)
                        .build();

        Item album = Album.builder()
                          .name("별이빛나는밤에")
                          .price(10000)
                          .stockQuantity(10)
                          .build();

        itemService.save(book);
        itemService.save(album);

        // when
        List<Item> items = itemService.findAll();

        // then
        assertThat(items.size()).isEqualTo(6);
    }

}
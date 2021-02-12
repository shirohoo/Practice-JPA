package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.web.BookFormDto;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품등록 페이지
     * @return createItemForm.html
     * @see GET
     */
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookFormDto());
        return "items/createItemForm";
    }

    /**
     * 상품등록
     * @return 상품등록 페이지로 리다이렉트
     * @see POST
     */
    @PostMapping("/items/new")
    public String create(BookFormDto form) {
        Book book = Book.builder()
                        .name(form.getName())
                        .price(form.getPrice())
                        .stockQuantity(form.getStockQuantity())
                        .author(form.getAuthor())
                        .isbn(form.getIsbn())
                        .build();

        itemService.save(book);
        return "redirect:/items";
    }

    /**
     * 상품목록 조회페이지
     * @return itemList.html
     * @see GET
     */
    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    /**
     * 상품수정 페이지
     * @param itemId Long
     * @return updateItemForm.html
     * @see GET
     */
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findById(itemId);
        BookFormDto form = new BookFormDto();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    /**
     * 상품 수정
     * @param form BookForm
     * @return 상품목록 조회 페이지로 리다이렉트
     * @see POST
     */
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookFormDto form) {
        Book book = Book.builder()
                        .id(form.getId())
                        .name(form.getName())
                        .price(form.getPrice())
                        .stockQuantity(form.getStockQuantity())
                        .author(form.getAuthor())
                        .isbn(form.getIsbn())
                        .build();

        itemService.save(book);
        return "redirect:/items";
    }

}
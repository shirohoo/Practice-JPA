package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.web.MemberFormDto;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 페이지
     * @return createMemberForm.html
     * @see GET
     */
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberFormDto());
        return "members/createMemberForm";
    }

    /**
     * 회원가입
     * @param form MemberFormDto
     * @param result BindingResult
     * @return 회원가입 성공 시 메인페이지로 리다이렉트 <br>
     * 회원가입 실패 시 회원가입 페이지로 리다이렉트
     * @see POST
     */
    @PostMapping("/members/new")
    public String create(@Valid MemberFormDto form, BindingResult result) {
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = Address.builder()
                                 .city(form.getCity())
                                 .street(form.getStreet())
                                 .zipcode(form.getZipcode())
                                 .build();

        Member member = Member.builder()
                              .name(form.getName())
                              .address(address)
                              .build();

        memberService.signUp(member);
        return "redirect:/";
    }

    /**
     * 회원목록 조회 페이지
     * @return memberList.html
     * @see GET
     */
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
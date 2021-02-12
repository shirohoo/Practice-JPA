package jpabook.jpashop.service;

import jpabook.jpashop.ApplicationTests;
import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MemberServiceTest extends ApplicationTests {

    @Autowired MemberService memberService;

    private static final Logger log = LoggerFactory.getLogger(MemberServiceTest.class);

    @Test
    @DisplayName("회원가입")
    public void signUp() throws Exception {
        // given
        Member member = Member.builder()
                              .name("한창훈")
                              .build();

        // when
        Long savedId = memberService.signUp(member);

        // then
        assertThat(savedId).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원가입_중복검사")
    public void validateDuplicateMember() throws Exception {
        // given
        Member firstMember = Member.builder()
                                   .name("한창훈")
                                   .build();

        Member secondMember = Member.builder()
                                    .name("한창훈")
                                    .build();

        memberService.signUp(firstMember);

        // when
        Exception exception = assertThrows(IllegalStateException.class,
                                                      () -> memberService.signUp(secondMember));

        // then
        log.error(() -> "ERROR MESSAGE = " + exception.getMessage());
    }

    @Test
    @DisplayName("회원검색_단건")
    public void findById() throws Exception {
        // given
        Member member = Member.builder()
                              .name("한창훈")
                              .build();

        memberService.signUp(member);

        // when
        Member findMember = memberService.findById(member.getId());

        // then
        assertThat(findMember.getName()).isEqualTo("한창훈");
    }

    @Test
    @DisplayName("회원검색_리스트")
    public void findAll() throws Exception {
        // given
        Member firstMember = Member.builder()
                                   .name("한창훈1")
                                   .build();

        Member secondMember = Member.builder()
                                    .name("한창훈2")
                                    .build();

        memberService.signUp(firstMember);
        memberService.signUp(secondMember);

        // when
        List<Member> members = memberService.findAll();

        // then
        assertThat(members.size()).isEqualTo(2);
    }

}
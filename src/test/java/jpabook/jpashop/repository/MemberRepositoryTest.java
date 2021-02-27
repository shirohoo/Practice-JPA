package jpabook.jpashop.repository;

import jpabook.jpashop.ApplicationTests;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MemberRepositoryTest extends ApplicationTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(MemberRepositoryTest.class);

    @Test
    @DisplayName("회원가입_테스트")
    public void save() throws Exception {
        // given
        Address address = Address.builder()
                                 .city("서울시")
                                 .street("은평구")
                                 .zipcode("15212")
                                 .build();

        Member member = Member.builder()
                              .name("한창훈")
                              .address(address)
                              .build();

        // when
        Long savedId = memberRepository.save(member);

        // then
        assertThat(savedId).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원검색_ID")
    public void findById() throws Exception {
        // given
        Address address = Address.builder()
                                 .city("서울시")
                                 .street("은평구")
                                 .zipcode("15212")
                                 .build();

        Member member = Member.builder()
                              .name("한창훈")
                              .address(address)
                              .build();

        Long savedId = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(savedId);

        // then
        assertThat(findMember.getId()).isEqualTo(savedId);
    }

    @Test
    @DisplayName("회원검색_이름")
    public void findByName() throws Exception {
        // given
        Address address = Address.builder()
                                 .city("서울시")
                                 .street("은평구")
                                 .zipcode("15212")
                                 .build();

        Member member = Member.builder()
                              .name("한창훈")
                              .address(address)
                              .build();

        Long savedId = memberRepository.save(member);

        // when
        List<Member> members = memberRepository.findByName(member.getName());

        // then
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원검색_리스트")
    public void findAll() throws Exception {
        // given
        Address address = Address.builder()
                                 .city("서울시")
                                 .street("은평구")
                                 .zipcode("15212")
                                 .build();

        Member member = Member.builder()
                              .name("한창훈")
                              .address(address)
                              .build();

        Long savedId = memberRepository.save(member);

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("프록시")
    public void proxy() throws Exception {
        // given
        Member member = Member.builder()
                              .name("프록시")
                              .build();

        em.persist(member);

        em.flush();
        em.clear();

        Member find = em.find(Member.class, member.getId());
        Member reference = em.getReference(Member.class, member.getId());

        System.out.println("find = " + find);
        System.out.println("reference = " + reference);
        System.out.println("result = " + (find==reference));

        // when

        // then
    }

}
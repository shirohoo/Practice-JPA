package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByName(String name) {
        return em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class)
                 .setParameter("name", name)
                 .getResultList();
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                 .getResultList();
    }

}

package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 조회 API V1 <br>
     * Entity로 응답을 하고 있는 상태. <br>
     * Entity의 스펙이 변하면 API자체의 스펙도 변하므로 매우 좋지 않은 방식임(Side Effect). <br>
     * 또한 유연성이 매우 안좋기 때문에 요구사항이 변경되었을 경우 유지보수가 힘듬
     * @return List<Member>
     * @see GET
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findAll();
    }

    /**
     * 회원 조회 API V2 <br>
     * Entity가 직접 응답되던 방식에서 응답용 DTO를 따로 작성하였고 한번 래핑하여 유연성을 크게 늘림 <br>
     * 이렇게 작성할 경우 Side Effect가 적어 유지보수에 좋고 <br>
     * 요구사항이 변경되더라도 확장하기 용이하다.
     * @return Result
     * @see GET
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> members = memberService.findAll();
        List<MemberDto> collect = members.stream()
                                         .map(m -> new MemberDto(m.getId(), m.getName()))
                                         .collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * 응답용 DTO를 래핑할 클래스. <br>
     * 요구사항이 변경될 경우를 대비해 확장에 용이하도록 한번 더 감쌈
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Result<T> {

        private T data;

    }

    /**
     * 회원 조회를 위한 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberDto {

        private Long id;

        private String name;

    }

    /**
     * 회원 가입 API V1 <br>
     * Entity로 요청을 받고 있는 상태. <br>
     * Entity의 스펙이 변하면 API자체의 스펙도 변하므로 매우 좋지 않은 방식임(Side Effect).
     *
     * @param member Member
     * @return CreateMemberResponse
     * @see POST
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.signUp(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 가입 API V2 <br>
     * 요청을 RequestDTO로 받아 ResponseDTO로 응답하는 방식. <br>
     * 가장 일반적으로 사용되는 방식이다.
     *
     * @param request CreateMemberRequest
     * @return CreateMemberResponse
     * @see POST
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = Member.builder()
                              .name(request.getName())
                              .build();
        Long id = memberService.signUp(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 가입 요청에 대한 응답을 위한 DTO <br>
     * API 컨트롤러 내부에서 응답용 객체를 생성하기 위한 목적이므로 <br>
     * private inner class로 만들면 응집도가 올라가는 효과가 있음
     *
     * @see DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class CreateMemberResponse {

        private Long id;

    }

    /**
     * 회원 가입 요청을 위한 DTO <br>
     * 컨버터가 컨트롤러 외부에서 객체를 생성하여 매핑해야 하므로 static으로 선언해야함 <br>
     * 사실상 이너클래스가 아닌 외부 클래스로 만드는 것이 더 옳은 설계
     *
     * @see DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateMemberRequest {

        @NotEmpty
        private String name;

    }

    /**
     * 회원 정보 수정 API <br>
     * 변경 감지를 통한 수정을 위해 조회 쿼리를 한번 발생시킴
     *
     * @param id      Long
     * @param request UpdateMemberRequest
     * @return UpdateMemberResponse
     * @see PUT
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findById(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /**
     * 회원 정보 수정 요청에 대한 응답을 위한 DTO
     *
     * @see DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class UpdateMemberResponse {

        private Long id;

        private String name;

    }

    /**
     * 회원 정보 수정 요청을 위한 DTO
     *
     * @see DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateMemberRequest {

        @NotEmpty
        private String name;

    }

}

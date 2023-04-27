package com.tnc.study.tennisstore.api.member;

import com.tnc.study.tennisstore.application.member.*;
import com.tnc.study.tennisstore.domain.member.query.FindMemberCondition;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import com.tnc.study.tennisstore.framework.web.response.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class AdminMemberController {

    private final CreateMemberService createMemberService;
    private final FindMemberService findMemberService;
    private final ChangeMemberInfoService changeMemberInfoService;
    private final DeleteMemberService deleteMemberService;
    private final InitializePasswordService initializePasswordService;

//    @GetMapping
    public ResponseEntity<List<FindMemberResponse>> findMembersV1() {
        List<FindMemberResponse> members = findMemberService.findMembers();

        return ResponseEntity.ok(members);
    }

    @GetMapping
    public ResponseEntity<Content<FindMemberResponse>> findMembersV2(FindMemberCondition condition) {
        List<FindMemberResponse> members = findMemberService.findMembers(condition);
        Content<FindMemberResponse> content = Content.of(members);
        return ResponseEntity.ok(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindMemberResponse> findMember(@PathVariable Long id) {
        FindMemberResponse member = findMemberService.findMember(id);
        return ResponseEntity.ok(member);
    }

//    @PostMapping
    public ResponseEntity<String> createMemberV1(@Valid @RequestBody CreateMemberRequest request) {
        Long memberId = createMemberService.signUp(request);
        return ResponseEntity.created(URI.create("/api/admin/members/%s".formatted(memberId)))
                .body("OK");
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createMemberV2(@Valid @RequestBody CreateMemberRequest request) {
        Long memberId = createMemberService.signUp(request);
        return ResponseEntity.created(URI.create("/api/admin/members/%s".formatted(memberId)))
                .body(ApiResponse.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> changeMemberInfo(@PathVariable Long id,
                                                        @Valid @RequestBody ChangeMemberInfoRequest request) {
        changeMemberInfoService.changeMemberInfo(id, request);
        return ResponseEntity.ok(ApiResponse.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable Long id) {
        deleteMemberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.OK);
    }

    @PutMapping("/{id}/password-reset")
    public ResponseEntity<ApiResponse> initializePassword(@PathVariable Long id) {
        String initializedPassword = initializePasswordService.initializePassword(id);
        log.info("\n\n randomPassword: {}", initializedPassword);
        return ResponseEntity.ok(ApiResponse.OK);
    }

}

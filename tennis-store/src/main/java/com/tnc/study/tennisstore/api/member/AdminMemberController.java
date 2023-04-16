package com.tnc.study.tennisstore.api.member;

import com.tnc.study.tennisstore.application.member.CreateMemberRequest;
import com.tnc.study.tennisstore.application.member.CreateMemberService;
import com.tnc.study.tennisstore.application.member.FindMemberResponse;
import com.tnc.study.tennisstore.application.member.FindMemberService;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import com.tnc.study.tennisstore.framework.web.response.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class AdminMemberController {

    private final CreateMemberService createMemberService;
    private final FindMemberService findMemberService;

//    @GetMapping
    public ResponseEntity<List<FindMemberResponse>> findMembersV1() {
        List<FindMemberResponse> members = findMemberService.findMembers();

        return ResponseEntity.ok(members);
    }

    @GetMapping
    public ResponseEntity<Content<FindMemberResponse>> findMembersV2() {
        List<FindMemberResponse> members = findMemberService.findMembers();
        Content<FindMemberResponse> content = Content.of(members);
        return ResponseEntity.ok(content);
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
}

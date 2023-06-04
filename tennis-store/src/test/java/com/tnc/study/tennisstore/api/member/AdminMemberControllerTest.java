package com.tnc.study.tennisstore.api.member;

import com.epages.restdocs.apispec.*;
import com.tnc.study.tennisstore.api.utils.ApiResponseFieldDescriptor;
import com.tnc.study.tennisstore.api.utils.ApiSchema;
import com.tnc.study.tennisstore.api.utils.CodeModelDocumentation;
import com.tnc.study.tennisstore.application.member.*;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.member.MemberGrade;
import com.tnc.study.tennisstore.domain.member.query.FindMemberCondition;
import com.tnc.study.tennisstore.framework.web.ApiObjectMapper;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import com.tnc.study.tennisstore.framework.web.response.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(AdminMemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class AdminMemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiObjectMapper apiObjectMapper;

    // MockBean
    @MockBean
    FindMemberService findMemberService;
    @MockBean
    CreateMemberService createMemberService;
    @MockBean
    ChangeMemberInfoService changeMemberInfoService;
    @MockBean
    DeleteMemberService deleteMemberService;
    @MockBean
    InitializePasswordService initializePasswordService;


    @Test
    @DisplayName("GET /api/admin/members")
    void testFindMembersAPI() throws Exception {
        //given
        Address address = new Address("서울특별시 강남구 도곡로 117", "12층", "06253");
        FindMemberResponse findMemberResponse1 = new FindMemberResponse(1L, "hashi00518@tnctec.co.kr", "하승완",
                address.getAddress1(), address.getAddress2(), address.getZipcode(), MemberGrade.BRONZE
        );
        FindMemberResponse findMemberResponse2 = new FindMemberResponse(2L, "hashi00517@tnctec.co.kr", "하승완",
                address.getAddress1(), address.getAddress2(), address.getZipcode(), MemberGrade.BRONZE
        );

        List<FindMemberResponse> findMemberResponses = Arrays.asList(findMemberResponse1, findMemberResponse2);

        FindMemberCondition condition = new FindMemberCondition(null, null, null, null);

        BDDMockito.given(findMemberService.findMembers(condition)).willReturn(findMemberResponses);

        Content<FindMemberResponse> content = Content.of(findMemberResponses);
        String contentString = apiObjectMapper.writeValueAsString(content);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/admin/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(contentString))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/members/findMembers",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("member")
                                                .description("회원을 조회하는 API")
                                                .summary("회원 조회")
                                                .responseSchema(Schema.schema("FindMemberResponse"))
                                                .responseFields(
                                                        PayloadDocumentation.fieldWithPath("content[].memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].email").description("회원 이메일"),
                                                        PayloadDocumentation.fieldWithPath("content[].name").description("회원 이름"),
//                                                        PayloadDocumentation.fieldWithPath("content[].grade").description("회원 등급"),
                                                        PayloadDocumentation.fieldWithPath("content[].grade").type(CodeModelDocumentation.ENUM).description("회원 등급")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(MemberGrade.class)),
                                                        PayloadDocumentation.fieldWithPath("content[].address1").description("회원 주소1"),
                                                        PayloadDocumentation.fieldWithPath("content[].address2").description("회원 주소2"),
                                                        PayloadDocumentation.fieldWithPath("content[].zipcode").description("회원 우편번호")
                                                ).build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("POST /api/admin/members")
    void testCreateMemberAPI() throws Exception {
        //given
        Long memberId = 1L;

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                "hashi00518@tnctec.co.kr",
                "1234",
                "하승완",
                "서울특별시 강남구 도곡로 117", "12층", "06253");

        BDDMockito.given(createMemberService.signUp(BDDMockito.any(CreateMemberRequest.class)))
                .willReturn(memberId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/members")
                .content(apiObjectMapper.writeValueAsBytes(createMemberRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/members/CreateMember",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("member")
                                                .description("회원을 생성하는 API")
                                                .summary("회원 생성")
                                                .requestSchema(Schema.schema("CreateMemberRequest"))
//                                                .responseSchema(Schema.schema("ApiResponse"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("회원 패스워드"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                                        PayloadDocumentation.fieldWithPath("address1").type(JsonFieldType.STRING).description("회원 주소1"),
                                                        PayloadDocumentation.fieldWithPath("address2").type(JsonFieldType.STRING).description("회원 주소2"),
                                                        PayloadDocumentation.fieldWithPath("zipcode").type(JsonFieldType.STRING).description("회원 우편번호")
                                                )
                                                .responseFields(
//                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("Response Code"),
//                                                        PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("Response Type"),
//                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("Response Message")
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                )
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("PUT /api/admin/members/{id}")
    void testChangeMemberInfoAPI() throws Exception {
        //given
        Long memberId = 1L;

        ChangeMemberInfoRequest changeMemberInfoRequest = new ChangeMemberInfoRequest(
                "HSW",
                "서울특별시 강남구 도곡로 117",
                "12층",
                "06253",
                MemberGrade.GOLD);

        BDDMockito.given(changeMemberInfoService.changeMemberInfo(BDDMockito.eq(memberId), BDDMockito.any(ChangeMemberInfoRequest.class)))
                .willReturn(memberId);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/admin/members/{id}", memberId)
                        .content(apiObjectMapper.writeValueAsBytes(changeMemberInfoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/members/ChangeMemberInfo",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("member")
                                                .description("회원을 수정하는 API")
                                                .summary("회원 수정")
                                                .requestSchema(Schema.schema("ChangeMemberInfoRequest"))
//                                                .responseSchema(Schema.schema("ApiResponse"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("회원 ID"))
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                                        PayloadDocumentation.fieldWithPath("address1").type(JsonFieldType.STRING).description("회원 주소1"),
                                                        PayloadDocumentation.fieldWithPath("address2").type(JsonFieldType.STRING).description("회원 주소2"),
                                                        PayloadDocumentation.fieldWithPath("zipcode").type(JsonFieldType.STRING).description("회원 우편번호"),
//                                                        PayloadDocumentation.fieldWithPath("grade").type(JsonFieldType.STRING).description("회원 등급")
                                                        PayloadDocumentation.fieldWithPath("grade").type(CodeModelDocumentation.ENUM).description("회원 등급")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(MemberGrade.class))
                                                ).responseFields(
//                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("Response Code"),
//                                                        PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("Response Type"),
//                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("Response Message")
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                ).build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("DELETE /api/admin/members/{id}")
    void testDeleteMemberAPI() throws Exception {
        //given
        Long memberId = 1L;

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/admin/members/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/members/DeleteMember",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("member")
                                                .description("회원을 삭제하는 API")
                                                .summary("회원 삭제")
                                                .requestSchema(Schema.schema("DeleteMemberRequest"))
//                                                .responseSchema(Schema.schema("ApiResponse"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("회원 ID"))
                                                .responseFields(
//                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("Response Code"),
//                                                        PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("Response Type"),
//                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("Response Message")
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                ).build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("PUT /api/admin/members/{id}/password-reset")
    void testInitializePasswordAPI() throws Exception {
        //given
        Long memberId = 1L;

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/admin/members/{id}/password-reset", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/members/initializePassword",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("member")
                                                .description("회원 패스워드 초기화 API")
                                                .summary("회원 패스워드 초기화")
                                                .requestSchema(Schema.schema("InitializePasswordRequest"))
//                                                .responseSchema(Schema.schema("ApiResponse"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("회원 ID"))
                                                .responseFields(
//                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("Response Code"),
//                                                        PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("Response Type"),
//                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("Response Message")
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                ).build()
                                ))
                );
    }

    @Test
    @DisplayName("GET /api/admin/members/{id}")
    void testFindMemberAPI() throws Exception {
        //given
        Long memberId = 1L;
        Address address = new Address("서울특별시 강남구 도곡로 117", "12층", "06253");
        FindMemberResponse findMemberResponse = new FindMemberResponse(
                memberId,
                "hashi00518@tnctec.co.kr",
                "하승완",
                address.getAddress1(),
                address.getAddress2(),
                address.getZipcode(),
                MemberGrade.BRONZE);

        BDDMockito.given(findMemberService.findMember(BDDMockito.eq(memberId)))
                .willReturn(findMemberResponse);

        String contentString = apiObjectMapper.writeValueAsString(findMemberResponse);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/admin/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(contentString))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/members/findMember",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("member")
                                                .description("회원 ID로 단건 조회하는 API")
                                                .summary("회원 단건 조회")
                                                .responseSchema(Schema.schema("FindMemberResponse"))
                                                .responseFields(
                                                        PayloadDocumentation.fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("회원 Email"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
//                                                        PayloadDocumentation.fieldWithPath("grade").type(JsonFieldType.STRING).description("회원 등급"),
                                                        PayloadDocumentation.fieldWithPath("grade").type(CodeModelDocumentation.ENUM).description("회원등급")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(MemberGrade.class)),
                                                        PayloadDocumentation.fieldWithPath("address1").type(JsonFieldType.STRING).description("회원 주소1"),
                                                        PayloadDocumentation.fieldWithPath("address2").type(JsonFieldType.STRING).description("회원 주소2"),
                                                        PayloadDocumentation.fieldWithPath("zipcode").type(JsonFieldType.STRING).description("회원 우편번호")
                                                ).build()
                                )
                        )
                );
    }
}
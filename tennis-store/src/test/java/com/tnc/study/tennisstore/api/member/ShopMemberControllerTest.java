package com.tnc.study.tennisstore.api.member;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.tnc.study.tennisstore.api.utils.CodeModelDocumentation;
import com.tnc.study.tennisstore.application.order.FindOrderResponse;
import com.tnc.study.tennisstore.application.order.FindOrderService;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderLine;
import com.tnc.study.tennisstore.domain.order.OrderState;
import com.tnc.study.tennisstore.domain.order.Receiver;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import com.tnc.study.tennisstore.framework.web.ApiObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tnc.study.tennisstore.application.order.FindOrderResponse.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

@WebMvcTest(ShopMemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class ShopMemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiObjectMapper apiObjectMapper;

    @MockBean
    FindOrderService findOrderService;

    @Test
    @DisplayName("GET /api/shop/members/{id}/orders")
    void testFindOrdersByMemberIdNoOffsetAPI() throws Exception {
        //given

        // 회원 생성
        Long memberId = 1L;

        String address1 = "서울특별시 강남구 도곡로 117";
        String address2 = "12층";
        String zipcode = "06253";

        Email email = Email.of("hashi00518@tnctec.co.kr");
        Password password = Password.of("1234");
        String name = "하승완";

        Address address = new Address(address1, address2, zipcode);
        Member member = new Member(email, password, name, address);

        // 상품 생성
        Racquet racquet = new Racquet(
                "프로스태프 RF 97",
                "페더러 라켓",
                "윌슨",
                Money.of(180000),
                10,
                290,
                97,
                315
        );
        Shoes shoes = new Shoes(
                "아식스 뭐시기",
                "조코비치 신발",
                "아식스",
                Money.of(150000),
                10,
                GroundType.HARD
        );

        // 주문 정보 생성
        String receiverPhone = "010-1234-5678";
        String deliveryMessage = "안전하게 와주세요";

        List<OrderLine> orderLines = Arrays.asList(
                new OrderLine(racquet, racquet.getPrice(), 2),
                new OrderLine(shoes, shoes.getPrice(), 5)
        );
        Receiver receiver = new Receiver(member.getName(), receiverPhone);

        // 주문 생성
        Order order = new Order(member, orderLines, address, receiver, deliveryMessage);

        // OrderLineResponse
        Long orderLineId = 1L;
        Long productId = 1L;
        List<OrderLineResponse> orderLineResponses = new ArrayList<>();
        for (OrderLine orderLine : orderLines) {
            orderLineResponses.add(new OrderLineResponse(
                    orderLineId++,
                    productId++,
                    orderLine.getOrderPrice().getAmount(),
                    orderLine.getOrderCount())
            );
        }

        // DeliveryResponse
        DeliveryResponse deliveryResponse = new DeliveryResponse(
                1L,
                member.getAddress(),
                new Receiver(member.getName(), receiverPhone),
                deliveryMessage,
                order.getDelivery().getDeliveryFee().getAmount(),
                null
        );

        // 주문은 한 건이지만, List 형태로 뽑아서 Slice 진행
        List<FindOrderResponse> findOrderResponses = Arrays.asList(
                new FindOrderResponse(1L,
                        memberId,
                        "하승완",
                        LocalDateTime.now(),
                        order.getState(),
                        order.getState().getMessage(),
                        order.getTotalPrice().getAmount(),
                        orderLineResponses,
                        deliveryResponse
                )
        );

        // Paging 처리
        PageRequest pageRequest = PageRequest.of(0, 1);
        SliceImpl<FindOrderResponse> content = new SliceImpl<>(findOrderResponses, pageRequest, false);
        BDDMockito.given(findOrderService.findOrdersByMemberIdNoOffset(memberId, memberId, pageRequest)).willReturn(content);

        String contentString = apiObjectMapper.writeValueAsString(content);

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add("lastOrderId", "1");
        valueMap.add("size", "1");

        //when
        ResultActions result = mockMvc.perform(get("/api/shop/members/{id}/orders", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .params(valueMap)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(contentString))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "shop/members/findOrdersByMemberIdNoOffset",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("shop")
                                                .description("회원별 주문 조회 API")
                                                .summary("No Offset 방식 조회")
                                                .responseSchema(Schema.schema("FindOrdersByMemberIdNoOffset"))
                                                .responseFields(
                                                        PayloadDocumentation.fieldWithPath("content[].orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderDate").type(JsonFieldType.STRING).description("주문 시간"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderState").type(CodeModelDocumentation.ENUM).description("주문 상태")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(OrderState.class)),
                                                        PayloadDocumentation.fieldWithPath("content[].orderStateMessage").type(JsonFieldType.STRING).description("주문 상태 메시지"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderTotalPrice").type(JsonFieldType.NUMBER).description("총 주문 가격"),

                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].orderLineId").type(JsonFieldType.NUMBER).description("주문 정보 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].orderLinePrice").type(JsonFieldType.NUMBER).description("주문 정보 가격"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].orderQuantity").type(JsonFieldType.NUMBER).description("주문한 개수"),

                                                        PayloadDocumentation.fieldWithPath("content[].delivery.deliveryId").type(JsonFieldType.NUMBER).description("배송 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.address.address1").type(JsonFieldType.STRING).description("배송 주소1"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.address.address2").type(JsonFieldType.STRING).description("배송 주소2"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.address.zipcode").type(JsonFieldType.STRING).description("배송 우편번호"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.receiver.name").type(JsonFieldType.STRING).description("받는분 이름"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.receiver.phone").type(JsonFieldType.STRING).description("받는분 전화번호"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.deliveryMessage").type(JsonFieldType.STRING).description("배송 메시지"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.deliveryFee").type(JsonFieldType.NUMBER).description("배송비"),

                                                        PayloadDocumentation.fieldWithPath("pageable.sort.*").ignored(),
                                                        PayloadDocumentation.fieldWithPath("pageable.*").ignored(),
                                                        PayloadDocumentation.fieldWithPath("size").ignored(),
                                                        PayloadDocumentation.fieldWithPath("number").ignored(),
                                                        PayloadDocumentation.fieldWithPath("sort.*").ignored(),
                                                        PayloadDocumentation.fieldWithPath("first").ignored(),
                                                        PayloadDocumentation.fieldWithPath("last").ignored(),
                                                        PayloadDocumentation.fieldWithPath("numberOfElements").ignored(),
                                                        PayloadDocumentation.fieldWithPath("empty").ignored()
                                                ).build()
                                )
                        ));
    }


}
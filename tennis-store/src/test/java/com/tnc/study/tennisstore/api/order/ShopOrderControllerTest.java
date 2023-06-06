package com.tnc.study.tennisstore.api.order;

import com.epages.restdocs.apispec.*;
import com.tnc.study.tennisstore.api.utils.ApiResponseFieldDescriptor;
import com.tnc.study.tennisstore.api.utils.ApiSchema;
import com.tnc.study.tennisstore.application.order.CreateOrderRequest;
import com.tnc.study.tennisstore.application.order.CreateOrderService;
import com.tnc.study.tennisstore.application.order.OrderStateService;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.order.Receiver;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import com.tnc.study.tennisstore.framework.web.ApiObjectMapper;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
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
import java.util.Map;

import static com.tnc.study.tennisstore.application.order.CreateOrderRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ShopOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class ShopOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiObjectMapper apiObjectMapper;

    @MockBean
    CreateOrderService createOrderService;

    @MockBean
    OrderStateService orderStateService;

    @Test
    @DisplayName("POST /api/shop/orders")
    void placeOrderAPI() throws Exception {
        //given
        // 회원
        Long memberId = 1L;
        Email email = new Email("hashi00518@tnctec.co.kr");
        Password password = new Password("1234");
        String name = "하승완";
        Address address = new Address("서울특별시 강남구 도곡로 117", "12층", "06253");

        Member member = new Member(email, password, name, address);

        // 상품
        Long racquetId = 1L;
        Racquet racquet = new Racquet("스피드 프로",
                "조코비치 라켓",
                "헤드",
                Money.of(300_000L),
                10,
                310, 100, 315);

        Long shoesId = 2L;
        Shoes shoes = new Shoes("에어 줌 페가수스",
                "가벼움",
                "나이키",
                Money.of(149_000L),
                20,
                GroundType.HARD);

        Long ballId = 3L;
        Ball ball = new Ball("코치",
                "잘 튀김",
                "낫소",
                Money.of(2900L),
                30,
                BallType.PRACTICE);

        String receiverName = "하승완";
        String receiverPhone = "010-8408-0716";
        Receiver receiver = new Receiver(receiverName, receiverPhone);
        String deliveryMessage = "부재시 문 앞에 두고 가주세요.";

        OrderProduct orderProduct1 = new OrderProduct(racquetId, 10);
        OrderProduct orderProduct2 = new OrderProduct(ballId, 10);
        List<OrderProduct> orderProducts = Arrays.asList(orderProduct1, orderProduct2);

        Long orderId = 1L;
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                memberId,
                member.getAddress().getAddress1(),
                member.getAddress().getAddress2(),
                member.getAddress().getZipcode(),
                receiver.getName(),
                receiver.getPhone(),
                deliveryMessage,
                orderProducts
        );

        BDDMockito.given(createOrderService.placeOrder(createOrderRequest))
                .willReturn(orderId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/shop/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(apiObjectMapper.writeValueAsString(createOrderRequest))
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "shop/orders/placeOrder",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("shop - order")
                                                .description("주문을 요청하는 API")
                                                .summary("주문 요청")
                                                .requestSchema(Schema.schema("CreateOrderRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("memberId").type(JsonFieldType.NUMBER).description(""),
                                                        PayloadDocumentation.fieldWithPath("address1").type(JsonFieldType.STRING).description(""),
                                                        PayloadDocumentation.fieldWithPath("address2").type(JsonFieldType.STRING).description(""),
                                                        PayloadDocumentation.fieldWithPath("zipcode").type(JsonFieldType.STRING).description(""),
                                                        PayloadDocumentation.fieldWithPath("receiverName").type(JsonFieldType.STRING).description(""),
                                                        PayloadDocumentation.fieldWithPath("receiverPhone").type(JsonFieldType.STRING).description(""),
                                                        PayloadDocumentation.fieldWithPath("deliveryMessage").type(JsonFieldType.STRING).description(""),
                                                        PayloadDocumentation.fieldWithPath("orderProducts[].productId").type(JsonFieldType.NUMBER).description(""),
                                                        PayloadDocumentation.fieldWithPath("orderProducts[].orderCount").type(JsonFieldType.NUMBER).description("")
                                                )
                                                .responseFields(
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                )
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("POST /api/shop/orders/{id}/confirm")
    void confirmOrderAPI() throws Exception {
        //given
        Long orderId = 1L;

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/shop/orders/{id}/confirm", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "shop/orders/confirm",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("shop - order")
                                                .description("구매 확정을 요청하는 API")
                                                .summary("구매 확정")
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("주문 ID"))
                                                .responseFields(
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                )
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("POST /api/shop/orders/{id}/cancel")
    void cancelOrderAPI() throws Exception {
        //given
        Long orderId = 1L;

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/shop/orders/{id}/cancel", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "shop/orders/cancel",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("shop - order")
                                                .description("구매 취소를 요청하는 API")
                                                .summary("구매 취소")
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("주문 ID"))
                                                .responseFields(
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                )
                                                .build()
                                )
                        )
                );
    }
}
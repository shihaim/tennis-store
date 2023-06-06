package com.tnc.study.tennisstore.api.order;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.tnc.study.tennisstore.api.utils.CodeModelDocumentation;
import com.tnc.study.tennisstore.application.member.CreateMemberRequest;
import com.tnc.study.tennisstore.application.order.CreateOrderRequest;
import com.tnc.study.tennisstore.application.order.FindOrderLineResponse;
import com.tnc.study.tennisstore.application.order.FindOrderResponse;
import com.tnc.study.tennisstore.application.order.FindOrderService;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.order.*;
import com.tnc.study.tennisstore.domain.order.query.FindOrderCondition;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import com.tnc.study.tennisstore.framework.web.ApiObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tnc.study.tennisstore.application.order.CreateOrderRequest.*;
import static com.tnc.study.tennisstore.application.order.FindOrderResponse.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AdminOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class AdminOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiObjectMapper apiObjectMapper;
    
    @MockBean
    FindOrderService findOrderService;
    
    @Test
    @DisplayName("GET /api/admin/orders")
    void findOrdersByConditionAPI() throws Exception {
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

        // 회원이 상품을 주문 / Order(1) - (n)OrderLine(n) - (1)Delivery
        String receiverName = "하승완";
        String receiverPhone = "010-8408-0716";
        Receiver receiver = new Receiver(receiverName, receiverPhone);
        String deliveryMessage = "부재시 문 앞에 두고 가주세요.";

        OrderProduct orderProduct1 = new OrderProduct(racquetId, 10);
        OrderProduct orderProduct2 = new OrderProduct(ballId, 10);
        List<OrderProduct> orderProducts = Arrays.asList(orderProduct1, orderProduct2);

        // Order 생성용 Request
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

        // OrderLine 생성
        Long orderLineId1 = 1L;
        OrderLine orderLine1 = new OrderLine(
                racquet,
                racquet.getPrice(),
                orderProduct1.orderCount()
        );
        Long orderLineId2 = 2L;
        OrderLine orderLine2 = new OrderLine(
                ball,
                ball.getPrice(),
                orderProduct2.orderCount()
        );
        List<OrderLine> orderLines = Arrays.asList(orderLine1, orderLine2);

        // Order 생성
        Long orderId = 1L;
        Order order = new Order(member, orderLines, member.getAddress(), receiver, deliveryMessage);

        // 주문 조회용 FindOrderResponse 생성
        OrderLineResponse orderLineResponse1 = new OrderLineResponse(
                orderLineId1,
                racquetId,
                orderLine1.getOrderPrice().getAmount(),
                orderLine1.getOrderCount()
        );
        OrderLineResponse orderLineResponse2 = new OrderLineResponse(
                orderLineId2,
                ballId,
                orderLine2.getOrderPrice().getAmount(),
                orderLine2.getOrderCount()
        );
        List<OrderLineResponse> orderLineResponses = Arrays.asList(orderLineResponse1, orderLineResponse2);

        // DeliveryResponse
        Long deliveryId = 1L;
        Delivery delivery = order.getDelivery();
        DeliveryResponse deliveryResponse = new DeliveryResponse(
                deliveryId,
                delivery.getAddress(),
                receiver,
                deliveryMessage,
                delivery.getDeliveryFee().getAmount(),
                delivery.getTrackingNumber()
        );

        // FindOrderResponse
        FindOrderResponse findOrderResponse = new FindOrderResponse(
                orderId,
                memberId,
                order.getMember().getName(),
                LocalDateTime.now(),
                order.getState(),
                order.getState().getMessage(),
                order.getTotalPrice().getAmount(),
                orderLineResponses,
                deliveryResponse
        );

        List<FindOrderResponse> findOrderResponses = Arrays.asList(findOrderResponse);

        // FindOrderCondition
        FindOrderCondition condition = new FindOrderCondition(null, null);

        // Pageable
        int page = 0;
        int size = 1;
        String properties = "createdBy";
        Sort.Direction sorting = Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sorting, properties));
        Page<FindOrderResponse> content = PageableExecutionUtils.getPage(findOrderResponses, pageRequest, findOrderResponses::size);

        // Query Parameter
        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", String.valueOf(page));
        queryParams.add("size", String.valueOf(size));
        queryParams.add("sort", properties.concat(",").concat(sorting.name()));

        BDDMockito.given(findOrderService.findOrdersByCondition(condition, pageRequest))
                .willReturn(content);

        String contentString = apiObjectMapper.writeValueAsString(content);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/admin/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .params(queryParams)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(contentString))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/orders/FindOrdersByCondition",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("order")
                                                .description("주문 정보를 조회하는 API")
                                                .summary("주문 조회")
                                                .responseSchema(Schema.schema("FindOrdersByCondition"))
                                                .responseFields(
                                                        PayloadDocumentation.fieldWithPath("content[].orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderDate").type(JsonFieldType.STRING).description("주문일"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderState").type(CodeModelDocumentation.ENUM).description("주문 상태")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(OrderState.class)),
                                                        PayloadDocumentation.fieldWithPath("content[].orderStateMessage").type(JsonFieldType.STRING).description("주문 상태 메시지"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderTotalPrice").type(JsonFieldType.NUMBER).description("총 주문 가격"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].orderLineId").type(JsonFieldType.NUMBER).description("주문 상세 정보 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].orderLinePrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                                                        PayloadDocumentation.fieldWithPath("content[].orderLines[].orderQuantity").type(JsonFieldType.NUMBER).description("주문한 개수"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.deliveryId").type(JsonFieldType.NUMBER).description("배송 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.address.address1").type(JsonFieldType.STRING).description("배송 주소"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.address.address2").type(JsonFieldType.STRING).description("배송 상세주소"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.address.zipcode").type(JsonFieldType.STRING).description("배송 우편 번호"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.receiver.name").type(JsonFieldType.STRING).description("받는분 이름"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.receiver.phone").type(JsonFieldType.STRING).description("받는분 전화번호"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.deliveryMessage").type(JsonFieldType.STRING).description("배송 메시지"),
                                                        PayloadDocumentation.fieldWithPath("content[].delivery.deliveryFee").type(JsonFieldType.NUMBER).description("배송비"),

                                                        PayloadDocumentation.fieldWithPath("pageable.sort.*").ignored(),
                                                        PayloadDocumentation.fieldWithPath("pageable.*").ignored(),
                                                        PayloadDocumentation.fieldWithPath("last").ignored(),
                                                        PayloadDocumentation.fieldWithPath("totalPages").ignored(),
                                                        PayloadDocumentation.fieldWithPath("totalElements").ignored(),
                                                        PayloadDocumentation.fieldWithPath("size").ignored(),
                                                        PayloadDocumentation.fieldWithPath("number").ignored(),
                                                        PayloadDocumentation.fieldWithPath("sort.*").ignored(),
                                                        PayloadDocumentation.fieldWithPath("numberOfElements").ignored(),
                                                        PayloadDocumentation.fieldWithPath("first").ignored(),
                                                        PayloadDocumentation.fieldWithPath("empty").ignored()
                                                )
                                                .build()
                                )
                        )
                );
    }
}
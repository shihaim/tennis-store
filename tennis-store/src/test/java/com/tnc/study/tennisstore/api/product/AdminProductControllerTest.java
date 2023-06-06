package com.tnc.study.tennisstore.api.product;

import com.epages.restdocs.apispec.*;
import com.tnc.study.tennisstore.api.utils.ApiResponseFieldDescriptor;
import com.tnc.study.tennisstore.api.utils.ApiSchema;
import com.tnc.study.tennisstore.api.utils.CodeModelDocumentation;
import com.tnc.study.tennisstore.application.product.*;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.query.FindProductCondition;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.tnc.study.tennisstore.application.product.ChangeProductRequest.*;
import static com.tnc.study.tennisstore.application.product.CreateProductRequest.*;

@WebMvcTest(AdminProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class AdminProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiObjectMapper apiObjectMapper;

    @MockBean
    CreateProductService createProductService;
    @MockBean
    FindProductService findProductService;
    @MockBean
    ChangeProductService changeProductService;
    @MockBean
    DeleteProductService deleteProductService;

    @Test
    @DisplayName("POST /api/admin/products/racquets")
    void createRacquetAPI() throws Exception {
        //given

        Long racquetId = 1L;

        String name = "퓨어 에어로";
        String description = "스핀형 라켓";
        String brand = "바볼랏";
        long price = 300_000L;
        int stockQuantity = 10;
        int weight = 300;
        int headSize = 100;
        int balance = 315;

        CreateRacquetRequest racquetRequest = new CreateRacquetRequest(
                name,
                description,
                brand,
                price,
                stockQuantity,
                weight,
                headSize,
                balance
        );

        BDDMockito.given(createProductService.createRacquet(BDDMockito.any(CreateRacquetRequest.class))).willReturn(racquetId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/products/racquets")
                .content(apiObjectMapper.writeValueAsString(racquetRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/product/CreateRacquet",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 생성하는 API")
                                                .summary("Racquet 생성")
                                                .requestSchema(Schema.schema("CreateRacquetRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("라켓 이름"),
                                                        PayloadDocumentation.fieldWithPath("description").type(JsonFieldType.STRING).description("라켓 설명"),
                                                        PayloadDocumentation.fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                                                        PayloadDocumentation.fieldWithPath("weight").type(JsonFieldType.NUMBER).description("무게"),
                                                        PayloadDocumentation.fieldWithPath("headSize").type(JsonFieldType.NUMBER).description("헤드 사이즈"),
                                                        PayloadDocumentation.fieldWithPath("balance").type(JsonFieldType.NUMBER).description("밸런스")
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
    @DisplayName("POST /api/admin/products/shoes")
    void createShoesAPI() throws Exception {
        //given
        Long shoesId = 1L;

        String name = "zomm X";
        String description = "가볍다";
        String brand = "나이키";
        Long price = 200000L;
        Integer stockQuantity = 10;
        GroundType groundType = GroundType.ALL;

        CreateShoesRequest createShoesRequest = new CreateShoesRequest(
                name,
                description,
                brand,
                price,
                stockQuantity,
                groundType
        );

        BDDMockito.given(createProductService.createShoes(BDDMockito.any(CreateShoesRequest.class))).willReturn(shoesId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/products/shoes")
                .content(apiObjectMapper.writeValueAsString(createShoesRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/products/CreateShoes",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 생성하는 API")
                                                .summary("Shoes 생성")
                                                .requestSchema(Schema.schema("CreateShoesRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("신발 이름"),
                                                        PayloadDocumentation.fieldWithPath("description").type(JsonFieldType.STRING).description("신발 설명"),
                                                        PayloadDocumentation.fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                                                        PayloadDocumentation.fieldWithPath("groundType").type(CodeModelDocumentation.ENUM).description("바닥 유형")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(GroundType.class))
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
    @DisplayName("POST /api/admin/products/balls")
    void createBallAPI() throws Exception {
        //given
        Long ballId = 1L;

        String name = "챔피온쉽";
        String description = "잘 튀긴다.";
        String brand = "월슨";
        Long price = 4000L;
        Integer stockQuantity = 10;
        BallType ballType = BallType.COMPETITION;

        CreateBallRequest createBallRequest = new CreateBallRequest(
                name,
                description,
                brand,
                price,
                stockQuantity,
                ballType
        );

        BDDMockito.given(createProductService.createBall(BDDMockito.any(CreateBallRequest.class))).willReturn(ballId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/products/balls")
                .content(apiObjectMapper.writeValueAsString(createBallRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/products/createBall",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 생성하는 API")
                                                .summary("Ball 생성")
                                                .requestSchema(Schema.schema("CreateBallRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("공 이름"),
                                                        PayloadDocumentation.fieldWithPath("description").type(JsonFieldType.STRING).description("공 설명"),
                                                        PayloadDocumentation.fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                                                        PayloadDocumentation.fieldWithPath("ballType").type(CodeModelDocumentation.ENUM).description("공 타입")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(BallType.class))
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
    @DisplayName("PUT /api/admin/products/racquets/{id}")
    void changeRacquetInfoAPI() throws Exception {
        Long racquetId = 1L;

        //given
        String name = "퓨어 에어로 VS";
        String description = "알카라즈 라켓";
        String brand = "바볼랏";
        long price = 300_000L;
        int stockQuantity = 10;
        int weight = 400;
        int headSize = 200;
        int balance = 420;

        ChangeRacquetRequest changeRacquetRequest = new ChangeRacquetRequest(
                name,
                description,
                brand,
                price,
                stockQuantity,
                weight,
                headSize,
                balance
        );

        BDDMockito.given(changeProductService.changeRacquetInfo(BDDMockito.eq(racquetId), BDDMockito.any(ChangeRacquetRequest.class)))
                .willReturn(racquetId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/admin/products/racquets/{id}", racquetId)
                .content(apiObjectMapper.writeValueAsString(changeRacquetRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/products/ChangeRacquetInfo",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 수정하는 API")
                                                .summary("Racquet 수정")
                                                .requestSchema(Schema.schema("ChangeRacquetRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("라켓 ID"))
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("라켓 이름"),
                                                        PayloadDocumentation.fieldWithPath("description").type(JsonFieldType.STRING).description("라켓 설명"),
                                                        PayloadDocumentation.fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                                                        PayloadDocumentation.fieldWithPath("weight").type(JsonFieldType.NUMBER).description("무게"),
                                                        PayloadDocumentation.fieldWithPath("headSize").type(JsonFieldType.NUMBER).description("헤드 사이즈"),
                                                        PayloadDocumentation.fieldWithPath("balance").type(JsonFieldType.NUMBER).description("밸런스")
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
    @DisplayName("PUT /api/admin/products/shoes/{id}")
    void changeShoesInfoAPI() throws Exception {
        //given
        Long shoesId = 1L;

        String name = "zomm X2";
        String description = "가볍다, 멋있다";
        String brand = "나이키";
        Long price = 130000L;
        Integer stockQuantity = 100;
        GroundType groundType = GroundType.CLAY;

        ChangeShoesRequest changeShoesRequest = new ChangeShoesRequest(
                name,
                description,
                brand,
                price,
                stockQuantity,
                groundType
        );

        BDDMockito.given(changeProductService.changeShoesInfo(BDDMockito.eq(shoesId), BDDMockito.any(ChangeShoesRequest.class)))
                .willReturn(shoesId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/admin/products/shoes/{id}", shoesId)
                .content(apiObjectMapper.writeValueAsString(changeShoesRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/products/ChangeShoesInfo",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 수정하는 API")
                                                .summary("Shoes 수정")
                                                .requestSchema(Schema.schema("ChangeShoesRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("신발 ID"))
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("신발 이름"),
                                                        PayloadDocumentation.fieldWithPath("description").type(JsonFieldType.STRING).description("신발 설명"),
                                                        PayloadDocumentation.fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                                                        PayloadDocumentation.fieldWithPath("groundType").type(CodeModelDocumentation.ENUM).description("바닥 유형")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(GroundType.class))
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
    @DisplayName("PUT /api/admin/products/balls/{id}")
    void changeBallsInfoAPI() throws Exception {
        //given
        Long ballId = 1L;

        String name = "챔피온쉽 2";
        String description = "잘 튀긴다. 잘빠졌다";
        String brand = "월슨";
        Long price = 5000L;
        Integer stockQuantity = 100;
        BallType ballType = BallType.COMPETITION;

        ChangeBallRequest changeBallRequest = new ChangeBallRequest(
                name,
                description,
                brand,
                price,
                stockQuantity,
                ballType
        );

        BDDMockito.given(changeProductService.changeBallInfo(BDDMockito.eq(ballId), BDDMockito.any(ChangeBallRequest.class)))
                .willReturn(ballId);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/admin/products/balls/{id}", ballId)
                .content(apiObjectMapper.writeValueAsString(changeBallRequest))
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
                                "admin/products/ChangeRacquetInfo",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 수정하는 API")
                                                .summary("Ball 수정")
                                                .requestSchema(Schema.schema("ChangeBallRequest"))
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("공 ID"))
                                                .requestFields(
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("공 이름"),
                                                        PayloadDocumentation.fieldWithPath("description").type(JsonFieldType.STRING).description("공 설명"),
                                                        PayloadDocumentation.fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                                                        PayloadDocumentation.fieldWithPath("ballType").type(CodeModelDocumentation.ENUM).description("공 타입")
                                                                .attributes(CodeModelDocumentation.codeModelToAttribute(BallType.class))
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
    @DisplayName("DELETE /api/admin/products/{id}")
    void deleteProductAPI() throws Exception {
        //given
        Long productId = 1L;

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/admin/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(apiObjectMapper.writeValueAsString(ApiResponse.OK)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentationWrapper
                        .document(
                                "admin/products/DeleteProduct",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 삭제하는 API")
                                                .summary("상품 삭제")
                                                .responseSchema(ApiSchema.apiResponseSchema())
                                                .pathParameters(ResourceDocumentation.parameterWithName("id").description("상품 ID"))
                                                .responseFields(
                                                        ApiResponseFieldDescriptor.apiResponseFieldDescriptors()
                                                )
                                                .build()
                                )
                        )
                );

    }

    @Test
    @DisplayName("GET /api/admin/products")
    void findProductsAPI() throws Exception {
        //given
        Racquet racquet = new Racquet("스피드 프로",
                "조코비치 라켓",
                "헤드",
                Money.of(300_000L),
                10,
                310, 100, 315);

        Shoes shoes = new Shoes("에어 줌 페가수스",
                "가벼움",
                "나이키",
                Money.of(149_000L),
                20,
                GroundType.HARD);

        Ball ball = new Ball("코치",
                "잘 튀김",
                "낫소",
                Money.of(2900L),
                30,
                BallType.PRACTICE);

        FindProductResponse findProductResponse1 = new FindProductResponse(
                1L,
                "RACQUET",
                racquet.getName(),
                racquet.getDescription(),
                racquet.getBrand(),
                racquet.getPrice().getAmount(),
                racquet.getStockQuantity()
        );

        FindProductResponse findProductResponse2 = new FindProductResponse(
                2L,
                "SHOES",
                shoes.getName(),
                shoes.getDescription(),
                shoes.getBrand(),
                shoes.getPrice().getAmount(),
                shoes.getStockQuantity()
        );

        FindProductResponse findProductResponse3 = new FindProductResponse(
                3L,
                "BALL",
                ball.getName(),
                ball.getDescription(),
                ball.getBrand(),
                ball.getPrice().getAmount(),
                ball.getStockQuantity()
        );

        List<FindProductResponse> allProducts = Arrays.asList(findProductResponse1, findProductResponse2, findProductResponse3);
        List<FindProductResponse> findProductResponses = Arrays.asList(findProductResponse1);

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));
//        Page<FindProductResponse> content = new PageImpl<>(findProductResponses, pageRequest, allProducts.size());
        Page<FindProductResponse> content = PageableExecutionUtils.getPage(findProductResponses, pageRequest, allProducts::size);

        FindProductCondition condition = new FindProductCondition(null, null, null, null);

        BDDMockito.given(findProductService.findProductsByCondition(condition, pageRequest))
                .willReturn(content);

        String contentString = apiObjectMapper.writeValueAsString(content);

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add("page", "0");
        valueMap.add("size", "1");
        valueMap.add("sort", "name,desc");

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/admin/products")
                .content(contentString)
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
                                "admin/products/FindProductsByCondition",
                                new ResourceSnippet(
                                        new ResourceSnippetParametersBuilder()
                                                .tag("product")
                                                .description("상품을 조회하는 API")
                                                .summary("상품 조회")
                                                .responseSchema(Schema.schema("FindProductsResponse"))
                                                .responseFields(
                                                        PayloadDocumentation.fieldWithPath("content[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                                        PayloadDocumentation.fieldWithPath("content[].productType").type(JsonFieldType.STRING).description("상품 유형"),
                                                        PayloadDocumentation.fieldWithPath("content[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                                        PayloadDocumentation.fieldWithPath("content[].description").type(JsonFieldType.STRING).description("상품 설명"),
                                                        PayloadDocumentation.fieldWithPath("content[].brand").type(JsonFieldType.STRING).description("브랜드 이름"),
                                                        PayloadDocumentation.fieldWithPath("content[].price").type(JsonFieldType.NUMBER).description("가격"),
                                                        PayloadDocumentation.fieldWithPath("content[].stockQuantity").type(JsonFieldType.NUMBER).description("재고 수량"),

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
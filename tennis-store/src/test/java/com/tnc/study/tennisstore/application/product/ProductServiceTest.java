package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.ProductRepository;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.racquet.RacquetRepository;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.tnc.study.tennisstore.application.product.ChangeProductRequest.*;
import static com.tnc.study.tennisstore.application.product.CreateProductRequest.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    CreateProductService createProductService;
    @Autowired
    FindProductService findProductService;
    @Autowired
    ChangeProductService changeProductService;
    @Autowired
    DeleteProductService deleteProductService;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    RacquetRepository racquetRepository;

    @BeforeEach
    void setup() {
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

        productRepository.save(racquet);
        productRepository.save(shoes);
        productRepository.save(ball);
    }

    @Test
    @DisplayName("라켓 등록 서비스")
    void testCreateRacquet() throws Exception {
        // given
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

        // when
        Long savedRacquetId = createProductService.createRacquet(racquetRequest);
        Product findProduct = productRepository.findById(savedRacquetId).get();

        // then
        assertThat(findProduct instanceof Racquet).isTrue();
        assertThat(findProduct.getName()).isEqualTo(name);
        assertThat(findProduct.getDescription()).isEqualTo(description);
        assertThat(findProduct.getBrand()).isEqualTo(brand);
        assertThat(findProduct.getPrice()).isEqualTo(Money.of(price));
        assertThat(findProduct.getStockQuantity()).isEqualTo(stockQuantity);

        assertThat(((Racquet) findProduct).getWeight()).isEqualTo(weight);
        assertThat(((Racquet) findProduct).getHeadSize()).isEqualTo(headSize);
        assertThat(((Racquet) findProduct).getBalance()).isEqualTo(balance);
    }

    @Test
    @DisplayName("상품 조회 서비스")
    void testFindProducts() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "price"));

        // when
        Page<FindProductResponse> page = findProductService.findProducts(pageRequest);
        List<FindProductResponse> products = page.getContent();

        // then
        assertThat(products.size()).isEqualTo(2);
        assertThat(products).extracting("name")
                .containsExactly("코치", "에어 줌 페가수스");
        assertThat(products).extracting("brand")
                .containsExactly("낫소", "나이키");
        assertThat(products).extracting("price")
                .containsExactly(BigDecimal.valueOf(2900L), BigDecimal.valueOf(149_000L));
        assertThat(products).extracting("stockQuantity")
                .containsExactly(30, 20);

        assertThat(page.getTotalElements()).isEqualTo(3); // 전체 데이터 개수 반환
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지 항목이냐?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가?
    }

    @Test
    @DisplayName("라켓 정보 변경 서비스")
    void testChangeRacquetInfo() throws Exception {
        // given
        String description = "조코비치도 쓰지만, 다른 선수도 많이 씀";
        long price = 500_000L;
        int stockQuantity = 100;

        Racquet racquet = racquetRepository.findAll().get(0);

        ChangeRacquetRequest changeRacquetRequest = new ChangeRacquetRequest(
                racquet.getName(),
                description,
                racquet.getBrand(),
                price,
                stockQuantity,
                racquet.getWeight(),
                racquet.getHeadSize(),
                racquet.getBalance()
        );

        // when
        Long racquetId = changeProductService.changeRacquetInfo(racquet.getId(), changeRacquetRequest);
        Racquet findRacquet = racquetRepository.findById(racquetId).get();

        // then
        assertThat(findRacquet.getDescription()).isEqualTo(description);
        assertThat(findRacquet.getPrice()).isEqualTo(Money.of(price));
        assertThat(findRacquet.getStockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @DisplayName("상품 삭제 서비스")
    void test() throws Exception {
        // given
        Product product = productRepository.findAll().get(0);

        // when
        deleteProductService.deleteProduct(product.getId());
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("재고 수량 20개 미만 상품 가격 10% 인상")
    void testIncreasePrice() {
        Product beforeProduct = productRepository.findAll()
                .stream()
                .filter(product -> "스피드 프로".equals(product.getName()))
                .findFirst()
                .get();

        productRepository.increasePriceBy10PerByStockQuantityLessThan20();

        Product afterProduct = productRepository.findAll()
                .stream()
                .filter(product -> "스피드 프로".equals(product.getName()))
                .findFirst()
                .get();

        assertThat(beforeProduct.getPrice()).isEqualTo(Money.of(300_000L));
        assertThat(afterProduct.getPrice()).isEqualTo(Money.of(330_000L));
    }
}

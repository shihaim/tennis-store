package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.ProductRepository;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tnc.study.tennisstore.application.product.CreateProductRequest.*;

@Transactional
@Service
@RequiredArgsConstructor
public class CreateProductService {

    private final ProductRepository productRepository;

    /**
     * 라켓 등록
     * @param request
     * @return
     */
    public Long createRacquet(CreateRacquetRequest request) {
        Racquet racquet = new Racquet(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                Money.of(request.getPrice()),
                request.getStockQuantity(),
                request.getWeight(),
                request.getHeadSize(),
                request.getBalance()
        );

        Racquet savedRacquet = productRepository.save(racquet);
        return savedRacquet.getId();
    }

    /**
     * 신발 등록
     * @param request
     * @return
     */
    public Long createShoes(CreateShoesRequest request) {
        Shoes shoes = new Shoes(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                Money.of(request.getPrice()),
                request.getStockQuantity(),
                request.getGroundType()
        );

        Shoes savedShoes = productRepository.save(shoes);
        return savedShoes.getId();
    }

    /**
     * 공 등록
     * @param request
     * @return
     */
    public Long createBall(CreateBallRequest request) {
        Ball ball = new Ball(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                Money.of(request.getPrice()),
                request.getStockQuantity(),
                request.getBallType()
        );

        Ball savedBall = productRepository.save(ball);
        return savedBall.getId();
    }

}

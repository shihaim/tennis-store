package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallRepository;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.racquet.RacquetRepository;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import com.tnc.study.tennisstore.domain.product.shoes.ShoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tnc.study.tennisstore.application.product.ChangeProductRequest.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ChangeProductService {

    private final RacquetRepository racquetRepository;
    private final ShoesRepository shoesRepository;
    private final BallRepository ballRepository;

    /**
     * 라켓 정보 변경
     * @param productId
     * @param request
     * @return
     */
    public Long changeRacquetInfo(Long productId, ChangeRacquetRequest request) {
        Optional<Racquet> optionalRacquet = racquetRepository.findById(productId);
        Racquet racquet = optionalRacquet.orElseThrow(() -> new NoProductException("라켓 정보를 찾을 수 없습니다."));

        racquet.changeRacquetInfo(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                Money.of(request.getPrice()),
                request.getStockQuantity(),
                request.getWeight(),
                request.getHeadSize(),
                request.getBalance()
        );

        return racquet.getId();
    }

    public Long changeShoesInfo(Long productId, ChangeShoesRequest request) {
        Optional<Shoes> optionalShoes = shoesRepository.findById(productId);
        Shoes shoes = optionalShoes.orElseThrow(() -> new NoProductException("신발 정보를 찾을 수 없습니다."));

        shoes.changeShoesInfo(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                Money.of(request.getPrice()),
                request.getStockQuantity(),
                request.getGroundType()
        );

        return shoes.getId();
    }

    public Long changeBallInfo(Long productId, ChangeBallRequest request) {
        Optional<Ball> optionalBall = ballRepository.findById(productId);
        Ball ball = optionalBall.orElseThrow(() -> new NoProductException("공 정보를 찾을 수 없습니다."));

        ball.changeBallInfo(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                Money.of(request.getPrice()),
                request.getStockQuantity(),
                request.getBallType()
        );

        return ball.getId();
    }
}

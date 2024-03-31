package com.kampus.kbazaar.promotion;

import com.kampus.kbazaar.exceptions.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<PromotionResponse> getAll() {
        return promotionRepository.findAll().stream().map(Promotion::toResponse).toList();
    }

    public PromotionResponse getPromotionByCode(String code) {
        return promotionRepository
                .findByCode(code)
                .map(Promotion::toResponse)
                .orElseThrow(() -> new NotFoundException("Promotion not found"));
    }
    //    public ResponseEntity<CartItemResponse> calculateSpecificProductDiscount(String
    // Username,PromotionRequest promotionRequest){
    //        Promotion promotion = promotionRepository.findByCode(promotionRequest.getCode())
    //                .orElseThrow(() -> new NotFoundException("Promotion not found"));
    //        if(promotion.get)
    //
    //    }

}

package olsior.shop.mono.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequest {
    private int amount;
    private int ccy;
    private MerchantPaymInfo merchantPaymInfo;
    private String redirectUrl;
    private String webHookUrl;
    private int validity;
    private String paymentType;
    private String qrId;
    private String code;
    private SaveCardData saveCardData;


    // Getters and setters
}
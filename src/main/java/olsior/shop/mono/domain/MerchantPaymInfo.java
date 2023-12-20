package olsior.shop.mono.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MerchantPaymInfo {
    private String reference;
    private String destination;
    private String comment;
    private List<String> customerEmails;
    private List<BasketOrderItem> basketOrder;


}
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
public class BasketOrderItem {
    private String name;
    private int qty;
    private int sum;
    private String icon;
    private String unit;
    private String code;
    private String barcode;
    private String header;
    private String footer;
    private List<String> tax;
    private String uktzed;
    private List<Discount> discounts;

    // Getters and setters

}
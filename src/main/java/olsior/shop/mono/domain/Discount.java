package olsior.shop.mono.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Discount {
    private String type;
    private String mode;
    private String value;

    // Getters and setters
}
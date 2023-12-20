package olsior.shop.mono.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonobankApiResponse {

    @JsonProperty("invoiceId")
    private String invoiceId;

    @JsonProperty("pageUrl")
    private String pageUrl;

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getPageUrl() {
        return pageUrl;
    }
}
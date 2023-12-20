package olsior.shop.telegram.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TShirt {

    private final Long id;
    private final String name;
    private final int price;
    private final String material;
    private final String description;
    private final List<String> sizes;
    private final List<String> urls;
    private final String imgUrl;
    private final String additionalInfo;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public List<String> getUrls() {
        return urls;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}

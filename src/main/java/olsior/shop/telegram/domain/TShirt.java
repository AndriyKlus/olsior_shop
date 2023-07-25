package olsior.shop.telegram.domain;

import java.util.List;

public class TShirt {

    private final String name;
    private final int price;
    private final String material;
    private final String description;
    private final List<String> sizes;
    private final List<String> urls;

    public TShirt(String name, int price, String material, String description, List<String> sizes, List<String> urls) {
        this.name = name;
        this.price = price;
        this.material = material;
        this.description = description;
        this.sizes = sizes;
        this.urls = urls;
    }

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
}

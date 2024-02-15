package olsior.shop.telegram.domain;


import lombok.Data;

import java.util.List;

@Data
public class TShirtPurchase {

    private Long id;
    private String name;
    private Double price;
    private String material;
    private String size;
    private List<String> photoUrls;
    private String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return name +
                ", Ціна = " + price +
                ", Матеріал = " + material +
                ", Розмір = " + size +
                "\n";
    }

    public String getStringForTable() {
        return name +
                ", Розмір = " + size;
    }
}

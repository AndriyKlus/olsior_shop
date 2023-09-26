package olsior.shop.telegram.domain;


import java.util.List;

public class TShirtPurchase {

    private String name;
    private int price;
    private String material;
    private String size;
    private List<String> photoUrls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

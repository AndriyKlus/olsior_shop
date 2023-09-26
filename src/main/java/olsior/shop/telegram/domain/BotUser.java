package olsior.shop.telegram.domain;

import java.util.List;
import java.util.Objects;

public class BotUser {
    private Long id;
    private String username;
    private Position position;
    private TShirtPurchase tShirtPurchase;
    private List<TwitchGift> twitchGiftsCart;
    private List<TShirtPurchase> tShirtsCart;
    private String twitchNickname;
    private String country;
    private String fullName;
    private String postOffice;
    private String address;
    private String phoneNumber;
    private String InfoAboutDelivery;
    private String paymentMethod;
    private boolean paymentConfirmation;
    private Long userId;

    public String getUsername() {
        if(Objects.isNull(username))
            return "unknown...";
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TShirtPurchase gettShirtPurchase() {
        return tShirtPurchase;
    }

    public void settShirtPurchase(TShirtPurchase tShirtPurchase) {
        this.tShirtPurchase = tShirtPurchase;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfoAboutDelivery() {
        return InfoAboutDelivery;
    }

    public void setInfoAboutDelivery(String infoAboutDelivery) {
        InfoAboutDelivery = infoAboutDelivery;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<TShirtPurchase> gettShirtsCart() {
        return tShirtsCart;
    }

    public void settShirtsCart(List<TShirtPurchase> tShirtsCart) {
        this.tShirtsCart = tShirtsCart;
    }

    public List<TwitchGift> getTwitchGiftsCart() {
        return twitchGiftsCart;
    }

    public void setTwitchGiftsCart(List<TwitchGift> twitchGiftsCart) {
        this.twitchGiftsCart = twitchGiftsCart;
    }

    public String getTwitchNickname() {
        return twitchNickname;
    }

    public void setTwitchNickname(String twitchNickname) {
        this.twitchNickname = twitchNickname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentConfirmation() {
        if (paymentConfirmation) {
            return "Потрібне підтвердження оплати";
        } else {
            return "-";
        }
    }

    public void setPaymentConfirmation(boolean paymentConfirmation) {
        this.paymentConfirmation = paymentConfirmation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTwitchGiftsCartString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(TwitchGift twitchGift : twitchGiftsCart) {
            stringBuilder.append(twitchGift.getName())
                    .append(Objects.nonNull(twitchGift.getStickersNames()) && !twitchGift.getStickersNames().isEmpty() ? twitchGift.getStickersNames().toString() : "")
                    .append(", ");
        }
        return stringBuilder.toString();
    }

}

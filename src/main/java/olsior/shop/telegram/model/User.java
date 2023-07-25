package olsior.shop.telegram.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    private Long chatId;

    private String name;
    private String surname;
    private String userName;
    private boolean whiteMarketActive;
    private boolean activeUser;
    private Double maxPrice;
    private Double minPrice;
    private boolean receiveWhiteMarketFloats;
    private boolean receiveWhiteMarketDiscounts;
    private boolean receiveWhiteMarketStickers;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isWhiteMarketActive() {
        return whiteMarketActive;
    }

    public void setWhiteMarketActive(boolean whiteMarketActive) {
        this.whiteMarketActive = whiteMarketActive;
    }

    public boolean isActiveUser() {
        return activeUser;
    }

    public void setActiveUser(boolean activeUser) {
        this.activeUser = activeUser;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isReceiveWhiteMarketFloats() {
        return receiveWhiteMarketFloats;
    }

    public void setReceiveWhiteMarketFloats(boolean receiveWhiteMarketFloats) {
        this.receiveWhiteMarketFloats = receiveWhiteMarketFloats;
    }

    public boolean isReceiveWhiteMarketDiscounts() {
        return receiveWhiteMarketDiscounts;
    }

    public void setReceiveWhiteMarketDiscounts(boolean receiveWhiteMarketDiscounts) {
        this.receiveWhiteMarketDiscounts = receiveWhiteMarketDiscounts;
    }

    public boolean isReceiveWhiteMarketStickers() {
        return receiveWhiteMarketStickers;
    }

    public void setReceiveWhiteMarketStickers(boolean receiveWhiteMarketStickers) {
        this.receiveWhiteMarketStickers = receiveWhiteMarketStickers;
    }
}

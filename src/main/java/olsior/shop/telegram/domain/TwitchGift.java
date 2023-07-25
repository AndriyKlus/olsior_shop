package olsior.shop.telegram.domain;

import java.util.List;

public class TwitchGift {

    private String name;
    private int points;
    private List<String> stickersNames;
    private List<String> urls;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getStickersNames() {
        return stickersNames;
    }

    public void setStickersNames(List<String> stickersNames) {
        this.stickersNames = stickersNames;
    }

    public TwitchGift(String name, int points, List<String> stickerName, List<String> urls) {
        this.name = name;
        this.points = points;
        this.stickersNames = stickerName;
        this.urls = urls;
    }

    public TwitchGift(String name, int points, List<String> urls) {
        this.name = name;
        this.points = points;
        this.urls = urls;
    }

    public TwitchGift(TwitchGift twitchGift) {
        this.name = twitchGift.getName();
        this.points = twitchGift.getPoints();
        this.urls = twitchGift.getUrls();
        this.stickersNames = twitchGift.getStickersNames();
    }
}

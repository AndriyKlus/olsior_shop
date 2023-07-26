package olsior.shop.telegram.db;

import olsior.shop.telegram.domain.TwitchGift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwitchGiftsDB {
    private final static List<TwitchGift> TWITCH_GIFTS = new ArrayList<>();

    static {
        TWITCH_GIFTS.addAll(
                List.of(new TwitchGift(
                                "Листівка від Olsior",
                                150000,
                                Collections.singletonList("https://i.imgur.com/BhmPRPi.jpg")
                        ),
                        new TwitchGift(
                                "Наліпки зі смайлами каналу",
                                125000,
                                Collections.singletonList("https://i.imgur.com/xBxXY7I.jpg")
                        ),
                        new TwitchGift(
                                "Набір з трьох значків зі смайлами каналу",
                                100000,
                                List.of("РЕСПЕКОТ", "ПРАЙД", "НАРУТО", "МЯВ", "БРО", "РЕЙДЖ", "СІК", "ПЛЕД",
                                        "КЛОУН", "БІБЛМІРА", "ВЕСЕЛКА", "БАВОВНА", "АУФ", "ЗРОЗ", "ШЕЙХ"),
                                Collections.singletonList("https://i.imgur.com/HZYHZAL.jpg")
                        )
                )
        );
    }

    public static List<TwitchGift> getTwitchGifts() {
        return TWITCH_GIFTS;
    }

}

package olsior.shop.telegram.db;

import olsior.shop.telegram.domain.TShirt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TShirtDB {
    private final static List<TShirt> tShirts = new ArrayList<>();

    static {
        tShirts.addAll(
                List.of(new TShirt(
                                "Футболка «ЗРОЗ»",
                                600,
                                "Бавовна",
                                "Прикольна футболка",
                                List.of("XS", "S-M", "L-XL"),
                                List.of("images\\Merch.png", "images\\Merch.png", "images\\Merch.png")
                        ),
                        new TShirt(
                                "Футболка «Лагідна Українізація»",
                                600,
                                "Текстиль",
                                "Прикольна футболка",
                                List.of("XS", "S-M", "L-XL"),
                                List.of("images\\Merch.png", "images\\Merch.png", "images\\Merch.png")
                        ),
                        new TShirt(
                                "Футболка «Полапав і спить»",
                                600,
                                "Бавовна",
                                "Прикольна футболка",
                                List.of("XS", "S-M", "L-XL"),
                                List.of("images\\Merch.png", "images\\Merch.png", "images\\Merch.png")
                        )
                )
        );
    }

    public static List<TShirt> getTShirts() {
        return tShirts;
    }

}

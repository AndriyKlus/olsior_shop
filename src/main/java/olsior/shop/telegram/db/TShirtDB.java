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
                                1300,
                                "Склад:\n" +
                                        "95% бавовна\n" +
                                        "5% еластан",
                                "Подивитись більше фото\nhttps://www.instagram.com/olsiorshop/",
                                List.of("XS", "S-M", "L-XL"),
                                List.of("\\root\\olsiorShop\\olsior_shop\\images\\zroz1.jpg", "\\root\\olsiorShop\\olsior_shop\\images\\zroz2.jpg", "\\root\\olsiorShop\\olsior_shop\\images\\zroz3.jpg")
                        ),
                        new TShirt(
                                "Футболка «Лагідна Українізація»",
                                1300,
                                "Склад:\n" +
                                        "95% бавовна\n" +
                                        "5% еластан",
                                "Подивитись більше фото\nhttps://www.instagram.com/olsiorshop/",
                                List.of("XS", "S-M", "L-XL"),
                                List.of("\\root\\olsiorShop\\olsior_shop\\images\\ukrainization1.jpg", "\\root\\olsiorShop\\olsior_shop\\images\\ukrainization2.jpg", "\\root\\olsiorShop\\olsior_shop\\images\\ukrainization3.jpg")
                        ),
                        new TShirt(
                                "Футболка «Полапав і спить»",
                                1300,
                                "Склад:\n" +
                                        "95% бавовна\n" +
                                        "5% еластан",
                                "Подивитись більше фото\nhttps://www.instagram.com/olsiorshop/",
                                List.of("XS", "S-M", "L-XL"),
                                List.of("\\root\\olsiorShop\\olsior_shop\\images\\paws1.jpg", "\\root\\olsiorShop\\olsior_shop\\images\\paws2.jpg", "\\root\\olsiorShop\\olsior_shop\\images\\paws3.jpg")
                        )
                )
        );
    }

    public static List<TShirt> getTShirts() {
        return tShirts;
    }

}

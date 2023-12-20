package olsior.shop.mono.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import olsior.shop.mono.domain.BasketOrderItem;
import olsior.shop.mono.domain.MerchantPaymInfo;
import olsior.shop.mono.domain.MonobankApiResponse;
import olsior.shop.mono.domain.PaymentRequest;
import olsior.shop.telegram.domain.TShirtPurchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonoService {

    private static final String url = "https://api.monobank.ua/api/merchant/invoice/create";

    public static String sendRequestForInvoice(List<TShirtPurchase> items) {
        PaymentRequest paymentRequest = createPaymentRequest(items);
        URL obj;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Token", "maeNoXoeXa2KmGSmLkQ7d2w");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(paymentRequest);
            OutputStream os = con.getOutputStream();
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Parse JSON response into a Java object
            ObjectMapper objectMapper = new ObjectMapper();
            MonobankApiResponse monobankApiResponse = objectMapper.readValue(response.toString(), MonobankApiResponse.class);
            return monobankApiResponse.getPageUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PaymentRequest createPaymentRequest(List<TShirtPurchase> items) {
        return PaymentRequest.builder()
                .amount(getSummPrice(items) * 100)
                .ccy(980)
                .merchantPaymInfo(createMerchantPaymInfo(items))
                .validity(3600)
                .build();
    }

    private static MerchantPaymInfo createMerchantPaymInfo(List<TShirtPurchase> items) {
        return MerchantPaymInfo.builder()
                .destination("Покупка кращого мерча")
                .basketOrder(createBasket(items))
                .build();
    }

    private static List<BasketOrderItem> createBasket(List<TShirtPurchase> items) {
        Map<TShirtPurchase, Integer> countMap = new HashMap<>();
        for (TShirtPurchase item : items) {
            countMap.put(item, countMap.getOrDefault(item.getName(), 0) + 1);
        }

        List<BasketOrderItem> basketOrderItems = new ArrayList<>();

        countMap.forEach((key, value) -> basketOrderItems.add(
                BasketOrderItem.builder()
                        .name(key.getName() + " Розмір: " + key.getSize())
                        .sum(key.getPrice() * value * 100)
                        .qty(value)
                        .unit("шт")
                        .icon(key.getImgUrl())
                        .code(key.getId().toString())
                        .build()
        ));
        return basketOrderItems;
    }

    private static int getSummPrice(List<TShirtPurchase> items) {
        return items.stream()
                .mapToInt(TShirtPurchase::getPrice)
                .sum();
    }

}

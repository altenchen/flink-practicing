package self.practice.topN;

import lombok.Data;

/**
 * @description:
 * @create: 2020/12/13
 * @author: altenchen
 */
@Data
public class OrderDetail {

    private long userId;
    private long itemId;
    private String cityName;
    private double price;
    private long timstamp;

    public OrderDetail(long userId, long itemId, String cityName, double price, long timstamp) {
        this.userId = userId;
        this.itemId = itemId;
        this.cityName = cityName;
        this.price = price;
        this.timstamp = timstamp;
    }
}

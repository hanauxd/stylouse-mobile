package lk.apiit.eea.stylouse.models.responses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdersResponse {
    private String id;
    private String address;
    private String city;
    private String postalCode;
    private String paymentMethod;
    private Date date;
    private List<OrderItemResponse> orderItems = new ArrayList<>();
}

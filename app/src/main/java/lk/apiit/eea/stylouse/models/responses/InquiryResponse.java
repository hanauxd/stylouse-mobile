package lk.apiit.eea.stylouse.models.responses;

import java.util.List;

import lk.apiit.eea.stylouse.models.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponse {
    private List<Inquiry> inquiries;
}

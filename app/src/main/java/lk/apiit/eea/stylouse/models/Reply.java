package lk.apiit.eea.stylouse.models;

import java.util.Date;

import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    private String id;
    private SignUpRequest user;
    private Date date;
    private String message;
    private boolean read;
}

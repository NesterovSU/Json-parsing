import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author Sergey Nesterov
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Company {
    private int id;
    private String  name,
                    address,
                    phoneNumber,
                    inn;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd.MM.yyyy")
    private Date founded;
    private List<Security> securities;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class Security{
        private String  name,
                        code;
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "dd.MM.yyyy")
        private Date date;
        private List<String> currency;
    }
}

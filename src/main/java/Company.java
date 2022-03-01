import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Nesterov
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Company {
    private int id;
    private String  name,
                    address,
                    phoneNumber,
                    inn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate founded;
    private List<Security> securities;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString()
    static class Security{
        private String  name,
                        code;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        private LocalDate date;
        private List<String> currency;
    }
}

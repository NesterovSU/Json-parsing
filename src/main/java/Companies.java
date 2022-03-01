import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Sergey Nesterov
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Companies {
    private List<Company> companies;
}

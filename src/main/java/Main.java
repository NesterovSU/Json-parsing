import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * @author Sergey Nesterov
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("shares.json");
        ObjectMapper mapper = new ObjectMapper();
        Companies companies = mapper.readValue(file, Companies.class);
        companies.getCompanies().forEach(System.out::println);
        System.out.println(companies.getCompanies().get(1).getSecurities().get(1));
    }
}

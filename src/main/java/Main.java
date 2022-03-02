import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sergey Nesterov
 */
public class Main {
    private static Companies companies;
    private static final DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("dd/MM/yy");

    public static void main(String[] args) throws IOException{

        // На входе имеется файл в формате json, содержащий информацию о каком-то количестве
        // организаций, в т.ч. названия, адреса, номера телефонов, ИНН, ОГРН, а также о ценных
        // бумагах, которыми владеет каждая компания.
        File file = new File("shares.json");
        ObjectMapper mapper = new ObjectMapper();
        companies = mapper.readValue(file, Companies.class);


        //Вывести все имеющиеся компании в формате «Краткое название» – «Дата основания 17/01/98»;
        System.out.println("\nВсе имеющиеся компании:");
        companies.getCompanies().forEach(c -> System.out.printf("%s - %s\n",
                        c.getName(),
                        c.getFounded().format(defaultDateFormat)));

        //Вывести все ценные бумаги (их код, дату истечения и полное название организации-владельца),
        //которые просрочены на текущий день, а также посчитать суммарное число всех таких бумаг;
        showExpiredSecurities();

        // На запрос пользователя в виде даты «ДД.ММ.ГГГГ», «ДД.ММ,ГГ», «ДД/ММ/ГГГГ» и «ДД/ММ/ГГ»
        // вывести название и дату создания всех организаций, основанных после введенной даты;
        // На запрос пользователя в виде кода валюты, например EU, USD, RUB и пр. выводить id
        // и коды ценных бумаг, использующих заданную валюту.
        String input = "";
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        while (true){
            sb.setLength(0);
            System.out.println("\nВведите дату или код валюты      'exit' - для выхода:");
            if (scanner.hasNext()) input = scanner.nextLine().toUpperCase();
            if (input.equals("EXIT")) return;
            if (input.matches("\\d\\D\\d{2}\\D\\d{2}(\\d{2})?")){
                showIfFoundedAfter("0" + input);
            }else if(input.matches("\\d\\D\\d\\D\\d{2}(\\d{2})?")){
                sb.append("0").append(input).insert(3, "0");
                showIfFoundedAfter(sb.toString());
            }else if(input.matches("\\d{2}\\D\\d\\D\\d{2}(\\d{2})?")){
                sb.append(input).insert(3, "0");
                showIfFoundedAfter(sb.toString());
            }else if(input.matches("[a-zA-Z]{2,3}")){
                showIfContainsCurrency(input);
            }else {
                System.out.println("Неверый запрос!");
            }
        }
    }

    // На запрос пользователя в виде даты «ДД.ММ.ГГГГ», «ДД.ММ,ГГ», «ДД/ММ/ГГГГ» и «ДД/ММ/ГГ»
    // вывести название и дату создания всех организаций, основанных после введенной даты;
    private static void showIfFoundedAfter(String input){
        String pattern = input
                .replaceFirst("\\d{2}", "dd")
                .replaceFirst("\\d{2}", "MM")
                .replaceFirst("\\d{2}", "yy")
                .replaceFirst("\\d{2}", "yy");
        DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern(pattern);
        LocalDate inputDate = LocalDate.parse(input, inputDateFormat);
        if(inputDate.getYear()>2069) inputDate = inputDate.minusYears(100);
        LocalDate date = inputDate;
        System.out.println("Организации основанные позднее введенной даты:");
        companies.getCompanies().stream().filter(c->c.getFounded().isAfter(date))
                .forEach(c-> System.out.printf("%s - %s\n",
                        c.getName(),
                        c.getFounded().format(inputDateFormat)));
    }

    // На запрос пользователя в виде кода валюты, например EU, USD, RUB и пр. выводить id
    // и коды ценных бумаг, использующих заданную валюту.
    private static void showIfContainsCurrency(String currency){
        companies.getCompanies().forEach(
                company->{
                    if (company.getSecurities().stream()
                            .anyMatch(security -> security.getCurrency().contains(currency))){
                        System.out.println("id компании - " + company.getId());
                        company.getSecurities().stream()
                            .filter(security -> security.getCurrency().contains(currency))
                            .forEach(
                                    security -> System.out.println("   код бумаги " + security.getCode()));}});
    }


    //Вывести все ценные бумаги (их код, дату истечения и полное название организации-владельца),
    //которые просрочены на текущий день, а также посчитать суммарное число всех таких бумаг;
    private static void showExpiredSecurities(){
        LocalDate today = LocalDate.now();
        AtomicInteger count = new AtomicInteger(0);
        System.out.println("\nПросроченные бумаги:");
        companies.getCompanies().forEach(
                company -> {
                    System.out.println("компания " + company.getName());
                    company.getSecurities().stream()
                        .filter(security -> security.getDate().isBefore(today))
                        .forEach(
                                security -> {
                                    System.out.printf("   код бумаги %s, дата истечения %s, эмитент %s\n",
                                            security.getCode(),
                                            security.getDate().format(defaultDateFormat),
                                            security.getName());
                                    count.getAndIncrement();});
                    System.out.printf("   количество бумаг = %d\n", count.get());
                count.set(0);});
    }
}

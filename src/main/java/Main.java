import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sergey Nesterov
 */
public class Main {
    public static void main(String[] args) throws IOException{
        File file = new File("shares.json");
        ObjectMapper mapper = new ObjectMapper();
        Companies companies = mapper.readValue(file, Companies.class);

        //Вывести все имеющиеся компании в формате «Краткое название» – «Дата основания 17/01/98»;
        System.out.println("Все имеющиеся компании:");
        final DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/yy");
        companies.getCompanies().forEach(c -> System.out.printf("%s - %s\n",
                        c.getName(),
                        c.getFounded().format(f1)));

        //Вывести все ценные бумаги (их код, дату истечения и полное название организации-владельца),
        //которые просрочены на текущий день, а также посчитать суммарное число всех таких бумаг;
        LocalDate today = LocalDate.now();
        AtomicInteger count = new AtomicInteger(0);
        System.out.println("\nПросроченные бумаги:");
        companies.getCompanies().stream().map(c->c.getSecurities().stream().filter(s-> s.getDate().isBefore(today)))
                .forEach(l->l.forEach((s)->{
                    System.out.printf("код = %s, дата истечения = %s, %s\n",
                            s.getCode(),
                            s.getDate().format(f1),
                            s.getName());
                    count.getAndSet(count.get() + 1);
                }));
        System.out.printf("Количество бумаг = %d\n", count.get());

        // На запрос пользователя в виде даты «ДД.ММ.ГГГГ», «ДД.ММ,ГГ», «ДД/ММ/ГГГГ» и «ДД/ММ/ГГ»
        // вывести название и дату создания всех организаций, основанных после введенной даты;
        System.out.println("Введите запрос в виде даты:");
        String input = "";
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) input = scanner.nextLine();
        String pattern = input.replaceFirst("\\d{2}", "dd")
                .replaceFirst("\\d{2}", "MM")
                .replaceFirst("\\d{2}", "yy")
                .replaceFirst("\\d{2}", "yy");
        DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern);
        LocalDate inputDate = LocalDate.parse(input, f);
        if(inputDate.getYear()>2069)
            inputDate = inputDate.minusYears(100);
        final LocalDate iDate= inputDate;
        System.out.println("Организации основанные позднее:");
        companies.getCompanies().stream().filter(c->c.getFounded().isAfter(iDate))
                .forEach(c-> System.out.printf("%s - %s\n",
                        c.getName(),
                        c.getFounded().format(f)));
    }
}

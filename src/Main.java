import fileReader.FileReader;
import service.NumberService;

import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        NumberService numberService = new NumberService(fileReader);

        long start = System.nanoTime();
        List<String> data = numberService.getData(Paths.get("10m.txt"));
        long finish = System.nanoTime();

        data.forEach(System.out::println);

        System.out.printf("Execution time in nano = " + (finish-start));
    }
}
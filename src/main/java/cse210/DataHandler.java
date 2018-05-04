package cse210;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * This class provides a set of powerful methods to read and manipulate the
 * excel data, which utilizes Java 8 Stream API. It also contains a simple user
 * interface to interact with the data and exception/error hanlding.
 * 
 * @author Dixing Xu
 * @since 0.1.0
 */
public class DataHandler {

    /**
     * The location of the excel files
     */
    public static final String FILE_PATH = "./Dataset_RG.xlsx";

    /**
     * A simple user interface to interact.
     */
    public static void SimpleUI() {

        System.out.println("Welcome to Researcher Analyser");
        System.out.println("------------------------------------------");

        /**
         * Setting up for the simple UI
         */
        Scanner kb = new Scanner(System.in);
        List<Researcher> rsList = getResearcherList(FILE_PATH);
        String researcherName;
        String targetInterest;
        Researcher targetrs;

        boolean exit = false;

        String select = "";

        while (!exit) {
            System.out.println();
            System.out.println("0: exit");
            System.out.println("1: Calculate the number of distinct researchers");
            System.out.println("2: Calculate the number of distinct interests");
            System.out.println("3: Give a researcher name, show detailed information of a researcher");
            System.out.println("4: Give an interest, calculate the number of researchers who have that interest");
            System.out.println("5: Give two interests, show the number of times they co-occur");
            System.out.println("6: Give a researcher, find similar researchers");
            System.out.print("Please input your option: ");
            select = kb.nextLine().trim();
            switch (select) {
            case "0":
                System.out.println("Thank you for using the Researcher Analyser");
                exit = true;
                break;
            case "1":
                long totalResearcher = countTotalResearchers(rsList);
                System.out.println("No. of distinct researchers: " + totalResearcher);
                break;
            case "2":
                long totalInterest = countTotalInterest(rsList);
                System.out.println("No. of distinct interests: " + totalInterest);
                break;
            case "3":
                System.out.print("Please give researcher name: ");
                researcherName = kb.nextLine().trim();
                System.out.println("You are searching " + researcherName);
                targetrs = searchByName(rsList, researcherName);
                System.out.println(targetrs);
                break;
            case "4":
                System.out.print("Please give an interest: ");
                targetInterest = kb.nextLine().trim();
                countByInterest(rsList, targetInterest);
                break;
            case "5":
                System.out.println("Please input two interests, separate by comma(,)");
                try {
                    String two_interests = kb.nextLine().trim();
                    String[] parsed = two_interests.split(",");
                    String interest1 = parsed[0].trim();
                    String interest2 = parsed[1].trim();
                    System.out.println("Interest1 is " + interest1);
                    System.out.println("Interest2 is " + interest2);
                    cooccuredInterest(rsList, interest1, interest2);
                } catch (Exception e) {
                    System.out.println("Something wrong with your input. Please check again.");
                }
                break;
            case "6":
                System.out.print("Please give researcher name: ");
                researcherName = kb.nextLine().trim();
                System.out.println("You are searching " + researcherName);
                targetrs = searchByName(rsList, researcherName);
                findSimilar(targetrs, rsList);
                break;
            default:
                System.out.println("Invalid option");
            }
        }
        kb.close();
    }

    /**
     * This method is used to find the top 5 similar researchers based on input
     * researcher.
     * 
     * @param target: the input Researcher
     * @param researcherList: the list of researchers to search against
     */
    public static void findSimilar(Researcher target, List<Researcher> researcherList) {

        if (target == null) {
            return;
        }

        Map<String, Double> smMap = new HashMap<String, Double>();

        for (Researcher rs : researcherList) {
            if (target.getName() == rs.getName()) {
                continue;
            }
            double i = SimilarityUtil.cosineSimilarity(target.getInterest(), rs.getInterest());
            smMap.put(rs.getName(), i);
        }

        Map<String, Double> after = sortMap(smMap);
        System.out.println("Similarity Top 5: ");
        after.entrySet().stream().filter(s -> !Double.isNaN(s.getValue())).limit(5).forEach(e -> System.out.println(e));

    }

    /**
     * Sort a map by values
     * 
     * @param unsorted: an unsorted map
     * @return sorted: a map sorted by value
     */
    public static Map<String, Double> sortMap(Map<String, Double> unsorted) {
        Map<String, Double> after_sort = unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return after_sort;
    }

    /**
     * Use captured locals to maintain state
     * 
     * @param <T>super: a general type
     * @param keyExtractor: a generaal function used to identify the key
     * @return Predicate which maintains state about what it's seen previously and
     *         given element was seen for the first time
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Count distinct Researchers by name
     * 
     * @param researcherList: a list of Researchers
     * @return long the number of distinct researchers
     */
    public static long countTotalResearchers(List<Researcher> researcherList) {
        long l = researcherList.stream().filter(distinctByKey(Researcher::getName)).count();
        return l;
    }

    /**
     * Count distinct Interests
     * 
     * @param researcherList: a list of Researchers
     * @return long the number of distinct interests
     */
    public static long countTotalInterest(List<Researcher> researcherList) {
        long i = researcherList.stream().flatMap(s -> Arrays.stream(s.getInterest())).map(String::toLowerCase)
                .distinct().count();
        return i;
    }

    /**
     * Count the number of researchers by an interest
     * 
     * @param researcherList: a list of Researchers
     * @param interest: a given interest (String)
     */
    public static void countByInterest(List<Researcher> researcherList, String interest) {
        try {
            Map<String, Long> map = researcherList.stream().flatMap(s -> Arrays.stream(s.getInterest()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            System.out.println("No. of researchers is: " + map.get(interest));
        } catch (Exception e) {
            System.out.println("There is something wrong with the input. Please check");
        }
    }

    /**
     * Search researcher by the name
     * 
     * @param researcherList: a list of Researchers
     * @param name: a given name of Researcher (String)
     * @return Researcher (object)
     */
    public static Researcher searchByName(List<Researcher> researcherList, String name) {
        try {
            List<Researcher> result = researcherList.stream().filter(rs -> rs.getName().equals(name))
                    .collect(Collectors.toList());
            Researcher rers = result.get(0);
            return rers;
        } catch (Exception e) {
            // throw new RuntimeException("The name does not match anyone in the list.
            // Please check you input");
            System.out.println("The name does not match anyone in the list. Please check your input");
            return null;
        }

    }

    /**
     * Calculate the number of two given interests co-occured
     * 
     * @param researcherList: a list of Researchers
     * @param interest1: a given interest (String)
     * @param interest2: a given interest (String)
     */
    public static void cooccuredInterest(List<Researcher> researcherList, String interest1, String interest2) {
        try {
            long num = researcherList.stream().filter(s -> {
                List<String> interests = Arrays.asList(s.getInterest());
                return interests.contains(interest1) && interests.contains(interest2);
            }).count();
            System.out.println("Number of co-occurence is: " + num);
        } catch (Exception e) {
            System.out.println("There is something wrong with your input. Please check.");
        }
    }

    /**
     * Read Excel File and return a list of Researches
     * 
     * @return list of researchers
     */
    public static List<Researcher> getResearcherList(String path) {

        List<Researcher> researcherList = new ArrayList<Researcher>();

        try {

            // Creating a Workbook from an Excel file (.xls or .xlsx)
            Workbook wb = WorkbookFactory.create(new File(path));

            // Retrieving the number of sheets in the Workbook
            // System.out.println("Workbook has " + wb.getNumberOfSheets() + " Sheets : ");

            /*
             * =====================================================================
             * Iterating over all the rows and columns in a Sheet
             * =====================================================================
             */

            // Getting the Sheet at index zero
            Sheet sheet = wb.getSheetAt(0);

            // Set<String> totalInterest = new HashSet<String>();

            // Create a DataFormatter to format and get each cell's value as String
            // DataFormatter dataFormatter = new DataFormatter();

            // use a for-each loop to iterate over the rows and columns
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Researcher rs = new Researcher();
                for (Cell cell : row) {

                    switch (cell.getColumnIndex()) {
                    case 0:
                        rs.setUniversity(String.valueOf(cell.getStringCellValue()));
                        break;
                    case 1:
                        rs.setDepartment(String.valueOf(cell.getStringCellValue()));
                        break;
                    case 2:
                        rs.setName(String.valueOf(cell.getStringCellValue()));
                        break;
                    case 10:
                        rs.setTopic(String.valueOf(cell.getStringCellValue()).split("\\s*(,)\\s*"));
                        break;
                    case 11:
                        rs.setSkill(String.valueOf(cell.getStringCellValue()).split("\\s*(,)\\s*"));
                        break;

                    }

                }
                // System.out.println();
                String[] concat = Stream
                        .concat(Arrays.stream(rs.getTopic()).filter(s -> s != null && !s.isEmpty()),
                                Arrays.stream(rs.getSkill()).filter(s -> s != null && !s.isEmpty()))
                        .sorted().toArray(String[]::new);
                rs.setInterest(concat);
                // Collections.addAll(totalInterest, concat);
                researcherList.add(rs);
            }

            // Closing the workbook
            wb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return researcherList;
    }

}
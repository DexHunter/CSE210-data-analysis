package cse210;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    static final String filepath = "./src/main/java/cse210/Dataset_RG.xlsx";
    List<Researcher> rsList = DataHandler.getResearcherList(filepath);

    @Test
    /**
     * Simple test for Researcher Type
     */
    public void TestNotNull() {
        Researcher testrs = DataHandler.searchByName(rsList, "Joy Wang");
        assertNotNull(testrs);
    }

    @Test
    /**
     * Simple test for numeric values
     */
    public void TestNumer() {
        double total_interest = 7277.0;
        assertEquals(DataHandler.countTotalInterest(rsList), total_interest, 1);
        double total_researcher = 6527.0;
        assertEquals(DataHandler.countTotalResearchers(rsList), total_researcher, 1);
    }
}

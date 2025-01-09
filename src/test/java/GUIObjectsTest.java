import GUI.BalanceData;
import GUI.HistoryMainData;
import GUI.HistoryTransferData;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class GUIObjectsTest {
    @Test
    @DisplayName("BalanceData Test")
    public void BalanceData() {
        int successCount = 0;
        try {
            BalanceData balanceData = new BalanceData("PB", "SUM", "CURR");
            if (balanceData.getPb().equals("PB")) {
                successCount++;
            }
            if (balanceData.getSum().equals("SUM")) {
                successCount++;
            }
            if (balanceData.getCurrency().equals("CURR")) {
                successCount++;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(3, successCount);
    }

    @Test
    @DisplayName("HistoryMainData Test")
    public void HistoryMainData() {
        int successCount = 0;
        try {
            HistoryMainData hmData = new HistoryMainData("ID", "DATE", "SUM", "CURR", "CAT", "SUBCAT", "COMMENT", "PB");
            if (hmData.getId().equals("ID")) {
                successCount++;
            }
            if (hmData.getDate().equals("DATE")) {
                successCount++;
            }
            if (hmData.getSum().equals("SUM")) {
                successCount++;
            }
            if (hmData.getCurrency().equals("CURR")) {
                successCount++;
            }
            if (hmData.getCategory().equals("CAT")) {
                successCount++;
            }
            if (hmData.getSubcategory().equals("SUBCAT")) {
                successCount++;
            }
            if (hmData.getComment().equals("COMMENT")) {
                successCount++;
            }
            if (hmData.getPb().equals("PB")) {
                successCount++;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(8, successCount);
    }

    @Test
    @DisplayName("HistoryTransferData Test")
    public void HistoryTransferData() {
        int successCount = 0;
        try {
            HistoryTransferData htData = new HistoryTransferData("ID", "DATE", "SUM", "CURR", "PBFROM", "PBTO", "COMMENT");
            if (htData.getId().equals("ID")) {
                successCount++;
            }
            if (htData.getDate().equals("DATE")) {
                successCount++;
            }
            if (htData.getSum().equals("SUM")) {
                successCount++;
            }
            if (htData.getCurrency().equals("CURR")) {
                successCount++;
            }
            if (htData.getSender().equals("PBFROM")) {
                successCount++;
            }
            if (htData.getReceiver().equals("PBTO")) {
                successCount++;
            }
            if (htData.getComment().equals("COMMENT")) {
                successCount++;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(7, successCount);
    }
}

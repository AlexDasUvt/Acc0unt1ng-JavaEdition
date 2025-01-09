import DBObjects.InitPBData;
import DBObjects.ReadCommand;
import DBObjects.RecordData;
import DBObjects.ResultData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DBObjectsTest {
    @Test
    @DisplayName("InitPBData object")
    public void InitPBData() {
        int SuccessCount = 0;
        try {
            InitPBData init = new InitPBData("val1", 99.99, "CURR");
            SuccessCount++;
            InitPBData init2 = new InitPBData("val1,99.99,CURR");
            SuccessCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(SuccessCount, 2);
    }

    @Test
    @DisplayName("ReadCommand Test")
    public void ReadCommand() {
        int SuccessCount = 0;
        try {
            ReadCommand rc = new ReadCommand();
            SuccessCount++;

            rc.setCommand("allm");
            if (rc.isValid()) {
                SuccessCount++;
            }
            rc.setCommand("unk");
            if (!rc.isValid()) {
                SuccessCount++;
            }
            if (rc.getCommand() != null) {
                SuccessCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(SuccessCount, 4);
    }

    @Test
    @DisplayName("RecordData Test")
    public void RecordData() {
        int SuccessCount = 0;
        try {
            RecordData recordData;
            recordData = new RecordData("date", "cat", "subcat", "pb", null, 20.2, "curr", "comm");
            SuccessCount++;
            if (recordData.isValid()) {
                SuccessCount++;
            }
            if (!recordData.isValidTransfer()) {
                SuccessCount++;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(SuccessCount, 3);
    }

    @Test
    @DisplayName("ResultData Test")
    public void ResultData() {
        try {
            ResultData resultData = new ResultData();
            assertNotNull(resultData.getMap());
            assertTrue(resultData.getMap().isEmpty());
            assertTrue(resultData.isValid() == false);

            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("Key1", "Value1");
            mapList.add(map1);

            resultData = new ResultData(mapList);

            assertFalse(resultData.getMap().isEmpty());
            assertTrue(resultData.isValid());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

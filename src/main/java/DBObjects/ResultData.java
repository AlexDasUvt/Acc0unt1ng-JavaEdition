package DBObjects;

import Interfaces.Readable;

import java.util.List;
import java.util.Map;

public class ResultData implements Readable {
    private List<Map<String, Object>> map;
    private Boolean isEmpty = true;

    public ResultData(List<Map<String, Object>> map) {
        this.map = map;
        isEmpty = false;
    }

    @Override
    public Boolean isReadable() {
        return !isEmpty;
    }

    public List<Map<String, Object>> getMap() {
        return map;
    }

    public String formatResultData() {
        StringBuilder resultString = new StringBuilder();

        if (!isReadable()) {
            return "ResultData is empty.";
        }

        List<Map<String, Object>> data = map;
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            resultString.append("Row ").append(i + 1).append(": \n");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                resultString.append("  ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }
            resultString.append("\n");
        }

        return resultString.toString();
    }

}

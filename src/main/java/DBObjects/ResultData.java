package DBObjects;

import Interfaces.Validatable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultData implements Validatable {
    private List<Map<String, Object>> map;
    private Boolean isEmpty = true;

    public ResultData() {
        this.map = new ArrayList<>();
        this.isEmpty = true;
    }

    public ResultData(List<Map<String, Object>> map) {
        this.map = map;
        isEmpty = false;
    }

    public void setMap(List<Map<String, Object>> map) {
        this.map = map;
        this.isEmpty = (map == null || map.isEmpty());
    }

    @Override
    public boolean isValid() {
        if (!isEmpty) {
            return true;
        } else {
            return false;
        }
    }

    public List<Map<String, Object>> getMap() {
        return map;
    }

    public String formatResultData() {
        StringBuilder resultString = new StringBuilder();

        if (!isValid()) {
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

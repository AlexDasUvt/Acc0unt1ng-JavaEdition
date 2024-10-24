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

}

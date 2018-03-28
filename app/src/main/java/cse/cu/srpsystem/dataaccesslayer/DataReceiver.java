package cse.cu.srpsystem.dataaccesslayer;

import java.util.List;

public interface DataReceiver {
    void onReceive(List<?> dataList);
}

package cse.cu.srpsystem.data;

import java.util.List;

public interface DataReceiver {
    void onReceive(List<?> dataList);
}

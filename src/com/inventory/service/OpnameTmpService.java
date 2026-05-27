package com.inventory.service;

import com.inventory.model.StokOpnameTmp;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface OpnameTmpService {

    boolean insertData(StokOpnameTmp model);
    boolean updateData(StokOpnameTmp model);
    boolean deleteData(int opnameTmpID);
    boolean clearTmp();
    
    List<StokOpnameTmp> getData();
    boolean loadDetailToTmp(int opnameID);
}

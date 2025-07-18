package api.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProvidersPet {
	
	@DataProvider(name = "petData")
	public Object[][] getPetData() throws IOException{
		
		XLUtility xl = new XLUtility("./testData/PetData.xlsx");
	    int rowCount = xl.getRowCount("Sheet1");
	    int colCount = xl.getCellCount("Sheet1", 1);

	    List<Object[]> dataList = new ArrayList<>();
	    
	    
	    for (int i = 1; i <= rowCount; i++) {
	        String id = xl.getCellData("Sheet1", i, 0);

	        if (id == null || id.trim().isEmpty()) {
	            System.out.println("Skipping blank row at Excel row index: " + i);
	            continue;
	        }

	        Object[] rowData = new Object[colCount];
	        for (int j = 0; j < colCount; j++) {
	            String cellValue = xl.getCellData("Sheet1", i, j);
	            System.out.println("Reading cell [Row: " + i + ", Col: " + j + "] = " + cellValue);
	            rowData[j] = cellValue;
	        }
	        dataList.add(rowData);
	    }
	    
	    // Convert list to array
	    Object[][] data = new Object[dataList.size()][colCount];
	    for (int i = 0; i < dataList.size(); i++) {
	        data[i] = dataList.get(i);
	    }

	    return data;
	}

}

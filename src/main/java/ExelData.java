import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExelData {

    private final List<Object[]> data = new ArrayList<>();
    private final XSSFWorkbook workbook = new XSSFWorkbook();

    ExelData() {
    }

    public void addDataToExel(State cellsData) {
        this.data.add(new Object[]{MathModel.toGrad(cellsData.alpha), MathModel.toGrad(cellsData.teta),MathModel.toGrad(cellsData.beta_k), MathModel.toGrad(cellsData.teta_c), cellsData.V_k, cellsData.a_k, cellsData.M_k, cellsData.p_k, cellsData.p_k_ch, cellsData.ro_k, cellsData.T_k, cellsData.Cxp, cellsData.Cx, cellsData.Cy, cellsData.mz, cellsData.Cxa, cellsData.Cya, cellsData.K, cellsData.Xcd_ch});
    }

    public void addNewTableTitle() {
        this.data.add(new Object[]{ "alpha", "teta_k, grad", "beta_k, grad", "teta_c, grad", "V_k, м/с", "a_k, м/с", "M_k", "p_k, Па", "coaf p_k", "ro_k, кг/м^3", "T_k, K","Cxp",  "Cx", "Cy", "mz", "Cxa", "Cya", "K", "Xcd_ch"});
    }

    public void writeDataToSheet() {
        Sheet sheet = workbook.createSheet();
        this.data.forEach(objectArr -> {
            Row row = sheet.createRow(this.data.indexOf(objectArr));
            int cellnum = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
                else if (obj instanceof Float)
                    cell.setCellValue((Float) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        });
    }
    public void writeExelDataFile(String exelFilename) {
        try {
            FileOutputStream out = new FileOutputStream(exelFilename + ".xlsx");
            this.workbook.write(out);
            out.close();
            System.out.println(exelFilename + ".xlsx written successfully on disk.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

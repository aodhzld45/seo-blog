package springbootdeveloper.common;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    /*
    *   유틸리티 클래스에서 제네릭 타입을 사용하여 객체를 생성하고, 리플렉션을 통해 필드에 값을 설정하는 방법을 사용
    * */

    public static <T> List<T> processExcelFile(MultipartFile file, Class<T> clazz) throws Exception {
        List<T> result = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            OPCPackage opcPackage = OPCPackage.open(inputStream);
            XSSFWorkbook wb = new XSSFWorkbook(opcPackage);

            int sheetNum = wb.getNumberOfSheets();

            for (int i = 0; i < sheetNum; i++) {
                XSSFSheet sheet = wb.getSheetAt(i);

                int rows = sheet.getPhysicalNumberOfRows();

                for (int j = 1; j < rows; j++) { // Start from 1 to skip the header
                    XSSFRow row = sheet.getRow(j);
                    if (row == null) continue; // Skip empty rows

                    T instance = clazz.getDeclaredConstructor().newInstance();

                    int cells = row.getPhysicalNumberOfCells();
                    for (int k = 0; k < cells; k++) {
                        XSSFCell cell = row.getCell(k);
                        String value = "";

                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case FORMULA:
                                    value = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    value = (int) cell.getNumericCellValue() + "";
                                    break;
                                case STRING:
                                    value = cell.getStringCellValue() + "";
                                    break;
                                case BLANK:
                                    value = cell.getBooleanCellValue() + "";
                                    break;
                                case ERROR:
                                    value = cell.getErrorCellValue() + "";
                                    break;
                                default:
                                    value = "";
                            }
                        }

                        Field field = clazz.getDeclaredFields()[k];
                        field.setAccessible(true);
                        field.set(instance, value.trim());
                    }

                    result.add(instance);
                }
            }
        }

        return result;
    }

}

package wuxingxing.me.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import wuxingxing.me.utils.date.DateUtil;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author xingxing.wu
 * @date 2018/7/4
 */
public class AbstractExcelGenerator<T> {
    protected void buildSheetOfDto(Sheet sheet, List<T> dataList) throws IllegalAccessException {
        if (sheet == null || (null == dataList || dataList.size() < 1)) {
            return;
        }

        // 标题行
        T tTitle = dataList.get(0);
        Row rowTitle = sheet.createRow(0);
        Field[] titleArray = tTitle.getClass().getDeclaredFields();
        for (int i = 0; i < titleArray.length; i++) {
            titleArray[i].setAccessible(true);
            rowTitle.createCell(i).setCellValue(titleArray[i].getName());
        }

        // 数据行
        for (int i = 0; i < dataList.size(); i++) {
            T t = dataList.get(i);
            Row row = sheet.createRow(i+1);
            Field[] fieldArray = dataList.get(i).getClass().getDeclaredFields();
            for (int j = 0; j < fieldArray.length; j++) {
                Cell cell = row.createCell(j);
                fieldArray[j].setAccessible(true);
                Object fieldValue = fieldArray[j].get(t);
                if (fieldValue instanceof Number) {
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(Double.valueOf(String.valueOf(fieldValue)));
                } else if (fieldValue instanceof String) {
                    cell.setCellValue(String.valueOf(fieldValue));
                } else if (fieldValue instanceof LocalDateTime) {
                    cell.setCellValue(DateUtil.getDateStrWithFormate2((LocalDateTime) fieldValue));
                } else if (fieldValue instanceof Boolean) {
                    cell.setCellValue((Boolean) fieldValue ? "YES" : "");
                } else if (fieldValue instanceof Date) {
                    cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fieldValue));
                }  else {
                    cell.setCellValue(String.valueOf(fieldValue));
                    //TODO 目前查询的数据只有这两种格式
//                    throw new RuntimeException("not match type");
                }
            }
        }
    }

}

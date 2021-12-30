package com.s2i.lms.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s2i.lms.service.dto.ReportResponseDTO;

import java.io.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;

public abstract class AbstractReport2<T> {

    private final Logger log = LoggerFactory.getLogger(AbstractReport2.class);

    protected final Workbook workbook;
    protected final Sheet sh;
    protected Row theader;
    protected Row tdetail;
    protected Font boldFont;

    protected List<T> list = null;

    private ReportResponseDTO reportResponseDTO = null;

    public AbstractReport2() throws Exception {
        try (InputStream is = new FileInputStream(new File(this.getClass().getClassLoader().getResource("sheet_model.xls").getPath()))) {
            this.workbook = new HSSFWorkbook(is);
            Sheet ts = workbook.getSheet(getTemplateName());
            ts.setSelected(true);

            this.sh = ts;

            this.boldFont = workbook.createFont();
            this.boldFont.setFontName("Arial");
            this.boldFont.setBold(true);

            this.theader = ts.getRow(0);
            this.tdetail = ts.getRow(1);
        } catch (IOException e) {
            throw new Exception("Invalid template sheet_model.xls");
        }
    }

    public abstract List<T> getList();

    public abstract String getFileName();

    public abstract String getTemplateName();

    public abstract void createRow(T item, Row row);

    public abstract void createFooter();

    public ReportResponseDTO getReportResponseDTO() {
        return reportResponseDTO;
    }

    public void createReport() {
        this.list = getList();
        log.debug("list.size={}", list.size());
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            createDetails();
            createFooter();
            setPostConfig();
            this.reportResponseDTO = createResponseDto(bos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void setPostConfig() {
        this.sh.createFreezePane(0, 1);
        this.sh.showInPane(0, 0);

        this.sh.setAutoFilter(new CellRangeAddress(0, list.size() > 0 ? list.size() - 1 : 0, 0, this.sh.getRow(0).getLastCellNum() - 1));
        // for (int col = 0; col < this.sh.getRow(0).getLastCellNum(); col++) {
        //  this.sh.autoSizeColumn(col);
        // }

        // hide other template
        for (int i = 0; i < this.workbook.getNumberOfSheets(); i++) {
            Sheet sheet = this.workbook.getSheetAt(i);
            if (sheet.equals(this.sh)) {
                sheet.setSelected(true);
                this.workbook.setActiveSheet(i);
            } else {
                workbook.setSheetHidden(i, true);
            }
        }
        this.sh.setSelected(true);
    }

    private ReportResponseDTO createResponseDto(ByteArrayOutputStream bos) throws IOException {
        workbook.write(bos);
        byte[] bytes = bos.toByteArray();
        return new ReportResponseDTO(new String(Base64.getEncoder().encode(bytes)), bytes, this.getFileName());
    }

    public void createDetails() {
        for (int i = 0; i < list.size(); i++) {
            Row row = this.sh.createRow(i + 1);
            createRow(list.get(i), row);
        }
    }

    public Cell setCell(String value, int col, Row row) {
        Cell cell = createCell(col, row);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCell(long value, int col, Row row) {
        Cell cell = createCell(col, row);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCell(double value, int col, Row row) {
        Cell cell = createCell(col, row);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCell(LocalDate value, int col, Row row) {
        Cell cell = createCell(col, row);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCell(ZonedDateTime value, int col, Row row) {
        Cell cell = createCell(col, row);
        if(value != null) {
            cell.setCellValue(value.toLocalDateTime());
        }

        return cell;
    }

    public Cell setCellf(String formula, int col, Row row) {
        Cell cell = createCell(col, row);
        cell.setCellFormula(formula);
        cell.setCellType(CellType.FORMULA);
        return cell;
    }

    private Cell createCell(int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(tdetail.getCell(col).getCellStyle());
        return cell;
    }

    protected Cell setFooterStyle(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();

        CellStyle newCellStyle = workbook.createCellStyle();
        newCellStyle.cloneStyleFrom(cellStyle);
        newCellStyle.setFont(this.boldFont);
        // newCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        // newCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(newCellStyle);

        return cell;
    }
}

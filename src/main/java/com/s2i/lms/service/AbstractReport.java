package com.s2i.lms.service;

import com.s2i.lms.service.dto.ReportResponseDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public abstract class AbstractReport<T> {

    private final Logger log = LoggerFactory.getLogger(AbstractReport.class);

    protected final Workbook workbook;
    protected final Sheet sh;
    protected Row theader;
    protected Row tdetail;

    protected List<T> list = null;

    private CellStyle cellStyleDate;

    private ReportResponseDTO reportResponseDTO = null;

    public AbstractReport() throws Exception {
        try (InputStream is = new FileInputStream(new File(this.getClass().getClassLoader().getResource("sheet_model.xls").getPath()))) {
            this.workbook = new HSSFWorkbook(is);
            Sheet ts = workbook.getSheet(getTemplateName());
            this.sh = ts;

            this.theader = ts.getRow(0);
            this.tdetail = ts.getRow(1);
        } catch (IOException e) {
            throw new Exception("Invalid template sheet_model.xls");
        }
    }

    public abstract List<T> getList();

    public abstract int getHeaderLength();

    public abstract String getFileName();

    public abstract String getTemplateName();

    public abstract void createRow(T item, Row row);

    public abstract void createFooter();

    public ReportResponseDTO getReportResponseDTO() {
        return reportResponseDTO;
    }

    public void createReport() {
        this.list = getList();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            setCellStyle();
            createDetails();
            createFooter();

            this.reportResponseDTO = createResponseDto(bos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void setCellStyle() {
        CreationHelper createHelper = this.workbook.getCreationHelper();
        this.cellStyleDate = this.workbook.createCellStyle();
        this.cellStyleDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
        this.cellStyleDate.setAlignment(HorizontalAlignment.CENTER);
    }

    private ReportResponseDTO createResponseDto(ByteArrayOutputStream bos) throws IOException {
        this.sh.setAutoFilter(new CellRangeAddress(0, list.size() - 1, 0, this.getHeaderLength() - 1));
        for (int col = 0; col < this.getHeaderLength(); col++) {
            this.sh.autoSizeColumn(col);
        }
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
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellType(CellType.STRING);
        return cell;
    }

    public Cell setCellt(String value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(tdetail.getCell(col).getCellStyle());
        return cell;
    }

    public Cell setCell(double value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCellt(double value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(tdetail.getCell(col).getCellStyle());
        return cell;
    }

    public Cell setCell(LocalDate value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyleDate);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCellt(LocalDate value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(tdetail.getCell(col).getCellStyle());
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCellt(Instant value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(tdetail.getCell(col).getCellStyle());
        cell.setCellValue(Date.from(value));
        return cell;
    }
}

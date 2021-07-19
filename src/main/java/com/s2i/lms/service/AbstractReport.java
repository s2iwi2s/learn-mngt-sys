package com.s2i.lms.service;

import com.s2i.lms.service.dto.ReportResponseDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public abstract class AbstractReport<T> {

    private final Logger log = LoggerFactory.getLogger(AbstractReport.class);

    protected final Workbook workbook;
    protected final Sheet sh;

    protected List<T> list = null;
    protected String[] header = null;

    private CellStyle cellStyleDate;

    private ReportResponseDTO reportResponseDTO = null;

    public AbstractReport() {
        this.workbook = new HSSFWorkbook();
        this.sh = workbook.createSheet();
        this.header = getHeader();
    }

    public abstract List<T> getList();

    public abstract String[] getHeader();

    public abstract String getFileName();

    public abstract void createRow(T item, Row row);

    public abstract void createFooter();

    public ReportResponseDTO getReportResponseDTO() {
        return reportResponseDTO;
    }

    public void createReport() {
        this.list = getList();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            setCellStyle();
            createHeader();
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
        for (int col = 0; col < header.length; col++) {
            this.sh.autoSizeColumn(col);
        }

        workbook.write(bos);
        byte[] bytes = bos.toByteArray();
        return new ReportResponseDTO(new String(Base64.getEncoder().encode(bytes)), bytes, this.getFileName());
    }

    public void createHeader() {
        Row row = this.sh.createRow(0);
        for (int col = 0; col < header.length; col++) {
            Cell cell = row.createCell(col);
            cell.setCellValue(header[col]);
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
            this.sh.autoSizeColumn(col);
        }
        this.sh.setAutoFilter(new CellRangeAddress(0, list.size() - 1, 0, header.length - 1));
    }

    public void createDetails() {
        for (int i = 0; i < list.size(); i++) {
            Row row = this.sh.createRow(i + 1);
            createRow(list.get(i), row);
            // setCell(i + 1, header.length, row);
        }
    }

    public Cell setCell(String value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellType(CellType.STRING);
        return cell;
    }

    public Cell setCell(double value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCell(LocalDate value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyleDate);
        cell.setCellValue(value);
        return cell;
    }

    public Cell setCell(Instant value, int col, Row row) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyleDate);
        cell.setCellValue(Date.from(value));
        return cell;
    }
}

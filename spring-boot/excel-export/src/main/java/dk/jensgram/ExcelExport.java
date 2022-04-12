package dk.jensgram;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.BiConsumer;

public class ExcelExport {
    private static final int CHAR_WIDTH = 256;

    private static final DummyDto[] DATA = new DummyDto[] {
        new DummyDto("foo", "First item", Instant.parse("2007-12-03T10:15:30.123Z")),
        new DummyDto("bar", "Second item", Instant.parse("2017-01-01T00:00:00Z")),
        new DummyDto("baz", "Third item", Instant.now())
    };

    public static void main(String[] args) throws IOException {
        var path = Path.of("./dummy-file.xlsx");
        var bytes = new ExcelExport().getExcelExport();

        Files.write(path, bytes);
        System.out.printf("Wrote %d bytes to %s%n", bytes.length, path.toAbsolutePath().normalize());
    }

    public byte[] getExcelExport() throws IOException {
        try (
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet("Dummy Items");

            // Cell styles.
            CellStyle cellStyleDate = workbook.createCellStyle();
            cellStyleDate.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-dd-MM hh:mm:ss.000"));
            CellStyle cellStyleHeader = workbook.createCellStyle();
            cellStyleHeader.setBorderBottom(BorderStyle.THIN);

            var columnDefinitions = new ColumnDefinition[] {
                new ColumnDefinition("ID", 5, (cell, dto) -> cell.setCellValue(dto.id())),
                new ColumnDefinition("Value", 10, (cell, dto) -> cell.setCellValue(dto.value())),
                new ColumnDefinition("Date & time (UTC)", 20, (cell, dto) -> {
                    cell.setCellValue(LocalDateTime.ofInstant(dto.timestamp(), ZoneOffset.UTC));
                    cell.setCellStyle(cellStyleDate);
                })
            };

            // Add header row and set column widths.
            sheet.createFreezePane( 0, 1, 0, 1);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnDefinitions.length; i++) {
                sheet.setColumnWidth(i, columnDefinitions[i].widthInChars() * CHAR_WIDTH);

                var cell = headerRow.createCell(i);
                cell.setCellValue(columnDefinitions[i].heading());
                cell.setCellStyle(cellStyleHeader);
            }

            // Add messages.
            int currentRow = 1; // Row 0 contains headings.
            for (var item : DATA) {
                Row messageRow = sheet.createRow(currentRow++);
                for (int i = 0; i < columnDefinitions.length; i++) {
                    columnDefinitions[i].cellRenderer.accept(messageRow.createCell(i), item);
                }
            }

            workbook.write(baos);

            return baos.toByteArray();
        }
    }

    private record ColumnDefinition(
        String heading,
        int widthInChars,
        BiConsumer<Cell, DummyDto> cellRenderer
    ) {}

    private record DummyDto(
        String id,
        String value,
        Instant timestamp
    ) {}
}

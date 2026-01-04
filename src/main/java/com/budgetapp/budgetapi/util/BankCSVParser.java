package com.budgetapp.budgetapi.util;

import org.apache.commons.csv.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BankCSVParser {

    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MMM-yyyy")
    );

    public List<ImportedTransactionRow> parse(MultipartFile file) {
        try (
                Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setTrim(true)
                        .setIgnoreSurroundingSpaces(true)
                        .build()
                        .parse(reader)
        ) {
            List<ImportedTransactionRow> rows = new ArrayList<>();

            for (CSVRecord record : parser) {
                if (record.size() < 5) continue;

                String dateRaw = record.get(0);
                String descriptionRaw = record.get(1);

                if (dateRaw.isBlank() || descriptionRaw.isBlank()) continue;

                LocalDate date = parseDate(dateRaw);
                String description = normalizeDescription(descriptionRaw);

                BigDecimal debit = parseMoney(record.get(2));
                BigDecimal credit = parseMoney(record.get(3));

                BigDecimal amount = credit.subtract(debit);

                rows.add(new ImportedTransactionRow(
                        date,
                        description,
                        amount
                ));
            }

            return rows;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse bank CSV", e);
        }
    }

    private LocalDate parseDate(String value) {
        for (DateTimeFormatter fmt : DATE_FORMATS) {
            try {
                return LocalDate.parse(value, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Unparseable date: " + value);
    }

    private BigDecimal parseMoney(String value) {
        if (value == null || value.isBlank()) return BigDecimal.ZERO;
        return new BigDecimal(value.replace(",", ""));
    }

    private String normalizeDescription(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    public record ImportedTransactionRow(
            LocalDate date,
            String description,
            BigDecimal amount
    ) {}
}

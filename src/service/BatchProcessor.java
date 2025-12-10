package service;

import model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class BatchProcessor {
    private final TaxService svc;

    public BatchProcessor(TaxService svc){
        this.svc = svc;




    }

    public void processFile(String filePath) {
        util.AppLogger.LOGGER.info("Starting batch processing: " + filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                try {
                    processLine(line);
                } catch (Exception e) {
                    // Якщо один рядок поганий, програма не падає, але ми пишемо в лог
                    util.AppLogger.LOGGER.warning("Error in batch file at line " + lineNum + ": " + line + ". Cause: " + e.getMessage());
                }
            }
            util.AppLogger.LOGGER.info("Batch processing finished.");
        } catch (IOException e) {
            util.AppLogger.LOGGER.log(java.util.logging.Level.SEVERE, "Failed to read batch file: " + filePath, e);
        }
    }

    private void processLine(String line) {

    }

    private LocalDate parseDateSafe(String s){



        try { return s == null || s.isBlank() ? LocalDate.now() : LocalDate.parse(s); }


        catch (Exception e) { return LocalDate.now(); }
    }
}

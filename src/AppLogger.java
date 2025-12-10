package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.*;

public class AppLogger {

    public static final Logger LOGGER = Logger.getLogger("TaxSystem");

    static {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(Level.ALL);

            FileHandler fileHandler = new FileHandler("tax_system.log", true);
            fileHandler.setLevel(Level.ALL);

            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tF %1$tT] [%2$s] %3$s %n",
                            new Date(record.getMillis()),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            LOGGER.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO); // В консоль пишемо тільки INFO і вище
            LOGGER.addHandler(consoleHandler);

            LOGGER.addHandler(new MockEmailHandler());

        } catch (IOException e) {
            System.err.println("CRITICAL: Failed to setup logger: " + e.getMessage());
        }
    }



    static class MockEmailHandler extends Handler {
        @Override
        public void publish(LogRecord record) {
            if (record.getLevel() != Level.SEVERE) {
                return;
            }

            StringBuilder emailBody = new StringBuilder();
            emailBody.append(record.getMessage());

            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                emailBody.append("\nStack Trace:\n").append(sw.toString());
            }

            System.err.println("\n============== EMAIL ALERT SYSTEM ==============");
            System.err.println("SENDING TO: admin@taxsystem.com");
            System.err.println("SUBJECT: CRITICAL ERROR DETECTED");
            System.err.println("BODY: \n" + emailBody.toString());
            System.err.println("================================================\n");
        }

        @Override
        public void flush() {}

        @Override
        public void close() throws SecurityException {}
    }
}
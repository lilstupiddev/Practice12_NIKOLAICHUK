package ua.university;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


enum PaymentStatus {
    NEW, PAID, FAILED
}

record Payment(int id, String email, PaymentStatus status, int amountCents) {}

class PaymentLoadResult {
    List<Payment> payments;
    int invalidLines;

    PaymentLoadResult(List<Payment> payments, int invalidLines) {
        this.payments = payments;
        this.invalidLines = invalidLines;
    }
}

class PaymentLoader {

    public static PaymentLoadResult loadWithStats(Path csvPath) {
        List<Payment> payments = new ArrayList<>();
        int invalidLines = 0;

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) {
                    invalidLines++;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 4) {
                    invalidLines++;
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String email = parts[1].trim();
                    PaymentStatus status = PaymentStatus.valueOf(parts[2].trim().toUpperCase());
                    int amountCents = Integer.parseInt(parts[3].trim());

                    payments.add(new Payment(id, email, status, amountCents));
                } catch (Exception e) {
                    invalidLines++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new PaymentLoadResult(payments, invalidLines);
    }

    public static void main(String[] args) {
        PaymentLoadResult result = loadWithStats(Path.of("payment.csv"));
        System.out.println("Валідні: " + result.payments.size());
        System.out.println("Невалідні: " + result.invalidLines);
    }
}
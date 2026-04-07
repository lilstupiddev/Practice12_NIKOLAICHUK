package ua.university;

import java.nio.file.Path;

public class PathSafety {

    public static Path safeResolve(Path base, String userInput) {
        Path resolved = base.resolve(userInput).normalize();
        if (!resolved.startsWith(base.normalize())) {
            throw new IllegalArgumentException("вихід за межі бази");
        }
        return resolved;
    }

    public static void main(String[] args) {
        Path base = Path.of("C:/data"); // приклад базової папки

        try {
            Path safePath = safeResolve(base, "reports/2025.txt");
            System.out.println("Шлях був безпечний");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
        try {
            Path unsafePath = safeResolve(base, "../secret.txt");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}

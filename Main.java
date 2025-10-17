import java.io.*;
import java.util.*;

/**
 * Головний клас програми для управління базою даних абітурієнтів.
 * <p>
 * Програма надає можливості для:
 * <ul>
 *   <li>Введення даних абітурієнтів вручну</li>
 *   <li>Перегляду всіх записів у табличному форматі</li>
 *   <li>Пошуку за різними критеріями</li>
 *   <li>Фільтрації за середнім балом</li>
 *   <li>Вибірки топ N абітурієнтів</li>
 *   <li>Видалення окремих записів або всієї бази</li>
 *   <li>Збереження та завантаження даних з файлу</li>
 * </ul>
 * 
 * @author Your Name
 * @version 1.0
 */
public class Main {
    /** Об'єкт Scanner для зчитування введення користувача з консолі */
    private static final Scanner scanner = new Scanner(System.in);
    
    /** Список для зберігання всіх абітурієнтів у пам'яті */
    private static final ArrayList<Abiturient> abiturients = new ArrayList<>();
    
    /** Поточний файл, з яким працює програма */
    private static File currentFile;

    /**
     * Головний метод програми. Підключає файл бази даних та запускає головне меню.
     * 
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        // === 1. Підключення файлу перед меню ===
        pidklyuchytyFile();

        // === 2. Головне меню ===
        while (true) {
            System.out.println("\n===== МЕНЮ =====");
            System.out.println("1. Ввести данi вручну");
            System.out.println("2. Показати всю таблицю");
            System.out.println("3. Пошук");
            System.out.println("4. Показати абiтурiєнтiв з балом вище заданого");
            System.out.println("5. Показати топ N абiтурiєнтiв з найвищим балом");
            System.out.println("6. Видалити конкретного абiтурiєнта");
            System.out.println("7. Видалити всю базу");
            System.out.println("8. Завершити i зберегти у файл");
            System.out.print("Виберiть опцiю: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> vvestyVruchnu();
                case "2" -> pokazatyTablytsyu();
                case "3" -> menuPoshuku();
                case "4" -> pokazatyZBalomVysche();
                case "5" -> pokazatyTopN();
                case "6" -> vydalytyZaID();
                case "7" -> vydalytyVsyuBazu();
                case "8" -> {
                    zberehtyUFile();
                    System.out.println("Програму завершено.");
                    return;
                }
                default -> System.out.println("Некоректний вибiр!");
            }
        }
    }

    /**
     * Підключає файл бази даних. Якщо файл не існує, створює новий.
     * Після підключення автоматично завантажує дані з файлу.
     * <p>
     * Метод працює в циклі до успішного підключення файлу.
     */
    private static void pidklyuchytyFile() {
        while (true) {
            System.out.print("Введiть повний шлях до вже iснуючого файлу бази даних(наприклад, data.txt) або програма створить файл текстовий файл самостiйно: ");
            String path = scanner.nextLine().trim();
            if (path.isEmpty()) {
                System.out.println("Шлях не може бути порожнiм!");
                continue;
            }

            currentFile = new File(path);

            try {
                if (!currentFile.exists()) {
                    // створюємо новий порожній файл
                    boolean created = currentFile.createNewFile();
                    if (created) {
                        System.out.println("Створено новий файл: " + currentFile.getAbsolutePath());
                    }
                } else {
                    System.out.println("Пiдключено файл: " + currentFile.getAbsolutePath());
                }

                // одразу спробуємо зчитати дані (навіть якщо файл порожній)
                zavantazhityZPotoky();
                break;
            } catch (IOException e) {
                System.out.println("Помилка при створеннi або вiдкриттi файлу: " + e.getMessage());
            }
        }
    }

    /**
     * Завантажує дані абітурієнтів з поточного файлу.
     * Очищує попередній список та заповнює його даними з файлу.
     * Використовує кодування UTF-8 для читання файлу.
     * Автоматично сортує список після завантаження.
     */
    private static void zavantazhityZPotoky() {
        abiturients.clear();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(currentFile), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Abiturient a = Abiturient.fromFileLine(line);
                if (a != null) abiturients.add(a);
            }
            // Автоматичне сортування після завантаження
            sortuvaty();
            System.out.println("Данi успiшно завантажено з файлу.");
        } catch (IOException e) {
            System.out.println("Помилка при зчитуваннi файлу: " + e.getMessage());
        }
    }

    /**
     * Виконує введення даних нового абітурієнта вручну через консоль.
     * Запитує у користувача всі необхідні поля та додає новий запис до бази.
     * Автоматично сортує список після додавання.
     */
    private static void vvestyVruchnu() {
        System.out.println("\nВведення даних абiтурiєнта:");
        int id = generateUniqueID(); // автоматично генеруємо ID
        System.out.println("Згенеровано ID: " + id);
        String prizvysche = vvestyNepuste("Прiзвище");
        String imya = vvestyNepuste("Iм`я");
        String pobatkovi = vvestyNepuste("По-батьковi");
        String adressa = vvestyNepuste("Адреса");
        String telefon = vvestyNepuste("Телефон");
        double seredniyBal = vvestyBal();

        abiturients.add(new Abiturient(id, prizvysche, imya, pobatkovi, adressa, telefon, seredniyBal));
        // Автоматичне сортування після додавання
        sortuvaty();
        System.out.println("Абiтурiєнта додано!");
    }

    /**
     * Генерує унікальний 6-значний ID для нового абітурієнта.
     * 
     * @return унікальний ID абітурієнта
     */
    private static int generateUniqueID() {
        Random random = new Random();
        while (true) {
            int id = 100000 + random.nextInt(900000); // генерує число від 100000 до 999999
            boolean exists = abiturients.stream().anyMatch(a -> a.getId() == id);
            if (!exists) return id; // унікальний — можна використовувати
        }
    }

    /**
     * Зчитує та валідує середній бал абітурієнта від користувача.
     * Бал повинен бути в діапазоні від 0 до 100.
     * 
     * @return валідний середній бал (0-100)
     */
    private static double vvestyBal() {
        while (true) {
            try {
                System.out.print("Середнiй бал (0–100): ");
                double bal = Double.parseDouble(scanner.nextLine());
                if (bal >= 0 && bal <= 100) return bal;
            } catch (NumberFormatException ignored) {}
            System.out.println("Помилка! Введiть число вiд 0 до 100.");
        }
    }

    /**
     * Зчитує непорожнє текстове поле від користувача.
     * Продовжує запитувати введення, поки користувач не введе непорожній текст.
     * 
     * @param field назва поля для відображення у запиті
     * @return непорожній текст після обрізання пробілів
     */
    private static String vvestyNepuste(String field) {
        while (true) {
            System.out.print(field + ": ");
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) return text;
            System.out.println("Поле не може бути порожнiм!");
        }
    }

    /**
     * Відображає всю таблицю абітурієнтів у форматованому вигляді.
     * Якщо база порожня, виводить відповідне повідомлення.
     * Таблиця містить всі поля кожного абітурієнта з обмеженням довжини.
     */
    private static void pokazatyTablytsyu() {
        if (abiturients.isEmpty()) {
            System.out.println("База порожня.");
            return;
        }

        System.out.println("\nСПИСОК АБIТУРIЄНТIВ");
        System.out.println("=".repeat(105));
        System.out.printf("%-5s | %-15s | %-12s | %-15s | %-20s | %-12s | %-6s%n",
                "ID", "Прiзвище", "Iм'я", "По-батьковi", "Адреса", "Телефон", "Бал");
        System.out.println("-".repeat(105));

        for (Abiturient a : abiturients) {
            System.out.printf("%-5d | %-15s | %-12s | %-15s | %-20s | %-12s | %-6.2f%n",
                    a.getId(),
                    obmezhyty(a.getPrizvysche(), 15),
                    obmezhyty(a.getImya(), 12),
                    obmezhyty(a.getPobatkovi(), 15),
                    obmezhyty(a.getAdressa(), 20),
                    obmezhyty(a.getTelefon(), 12),
                    a.getSeredniyBal());
        }

        System.out.println("=".repeat(105));
        System.out.println("Усього записiв: " + abiturients.size());
    }

    /**
     * Обмежує довжину тексту до вказаної максимальної довжини.
     * Якщо текст довший, обрізає його та додає "..." в кінці.
     * 
     * @param text текст для обмеження
     * @param maxLen максимальна довжина
     * @return обмежений текст або оригінальний, якщо він не перевищує maxLen
     */
    private static String obmezhyty(String text, int maxLen) {
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }

    /**
     * Відображає меню пошуку та делегує виконання відповідному методу пошуку.
     * Надає можливість пошуку за різними полями: ID, прізвище, ім'я, 
     * по-батькові, адреса, телефон, середній бал.
     */
    private static void menuPoshuku() {
        System.out.println("\nПошук за:");
        System.out.println("1. ID");
        System.out.println("2. Прiзвищем");
        System.out.println("3. Iм'енем");
        System.out.println("4. По-батьковi");
        System.out.println("5. Адресою");
        System.out.println("6. Телефоном");
        System.out.println("7. Середнiм балом");
        System.out.print("Вибiр: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> poshukZaID();
            case "2" -> poshukZaPrizvysche();
            case "3" -> poshukZaImya();
            case "4" -> poshukZaPobatkovi();
            case "5" -> poshukZaAdressa();
            case "6" -> poshukZaTelefon();
            case "7" -> poshukZaBal();
            default -> System.out.println("Некоректний вибiр!");
        }
    }

    /**
     * Виконує пошук абітурієнта за ID.
     * Виводить всі знайдені записи у консоль та кількість результатів.
     */
    private static void poshukZaID() {
        System.out.print("Введiть ID для пошуку (6 цифр): ");
        String input = scanner.nextLine().trim();

        // Перевірка правильності формату
        if (!input.matches("\\d{6}")) {
            System.out.println("Помилка! ID має складатися з 6 цифр.");
            return;
        }

        int id = Integer.parseInt(input);

        // Пошук у базі
        int count = 0;
        for (Abiturient a : abiturients) {
            if (a.getId() == id) {
                System.out.println(a);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("Абiтурiєнта з таким ID не знайдено.");
        } else {
            System.out.println("Знайдено записiв: " + count);
        }
    }

    /**
     * Виконує пошук абітурієнтів за прізвищем.
     * Пошук є регістронезалежним та підтримує часткове співпадіння.
     * Виводить кількість знайдених записів.
     */
    private static void poshukZaPrizvysche() {
        System.out.print("Введiть прiзвище: ");
        String value = scanner.nextLine().trim().toLowerCase();
        
        List<Abiturient> results = abiturients.stream()
                .filter(a -> a.getPrizvysche().toLowerCase().contains(value))
                .toList();
        
        if (results.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв за цим прiзвищем.");
        } else {
            results.forEach(System.out::println);
            System.out.println("Знайдено записiв: " + results.size());
        }
    }

    /**
     * Виконує пошук абітурієнтів за ім'ям.
     * Пошук є регістронезалежним та підтримує часткове співпадіння.
     * Виводить кількість знайдених записів.
     */
    private static void poshukZaImya() {
        System.out.print("Введiть iм'я: ");
        String value = scanner.nextLine().trim().toLowerCase();
        
        List<Abiturient> results = abiturients.stream()
                .filter(a -> a.getImya().toLowerCase().contains(value))
                .toList();
        
        if (results.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв за цим iм'ям.");
        } else {
            results.forEach(System.out::println);
            System.out.println("Знайдено записiв: " + results.size());
        }
    }

    /**
     * Виконує пошук абітурієнтів за по-батькові.
     * Пошук є регістронезалежним та підтримує часткове співпадіння.
     * Виводить кількість знайдених записів.
     */
    private static void poshukZaPobatkovi() {
        System.out.print("Введiть по-батьковi: ");
        String value = scanner.nextLine().trim().toLowerCase();
        
        List<Abiturient> results = abiturients.stream()
                .filter(a -> a.getPobatkovi().toLowerCase().contains(value))
                .toList();
        
        if (results.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв за цим по-батьковi.");
        } else {
            results.forEach(System.out::println);
            System.out.println("Знайдено записiв: " + results.size());
        }
    }

    /**
     * Виконує пошук абітурієнтів за адресою.
     * Пошук є регістронезалежним та підтримує часткове співпадіння.
     * Виводить кількість знайдених записів.
     */
    private static void poshukZaAdressa() {
        System.out.print("Введiть адресу: ");
        String value = scanner.nextLine().trim().toLowerCase();
        
        List<Abiturient> results = abiturients.stream()
                .filter(a -> a.getAdressa().toLowerCase().contains(value))
                .toList();
        
        if (results.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв за цією адресою.");
        } else {
            results.forEach(System.out::println);
            System.out.println("Знайдено записiв: " + results.size());
        }
    }

    /**
     * Виконує пошук абітурієнтів за телефоном.
     * Пошук є регістронезалежним та підтримує часткове співпадіння.
     * Виводить кількість знайдених записів.
     */
    private static void poshukZaTelefon() {
        System.out.print("Введiть телефон: ");
        String value = scanner.nextLine().trim().toLowerCase();
        
        List<Abiturient> results = abiturients.stream()
                .filter(a -> a.getTelefon().toLowerCase().contains(value))
                .toList();
        
        if (results.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв за цим телефоном.");
        } else {
            results.forEach(System.out::println);
            System.out.println("Знайдено записiв: " + results.size());
        }
    }

    /**
     * Виконує пошук абітурієнтів за середнім балом.
     * Шукає точне співпадіння балу.
     * Виводить кількість знайдених записів.
     */
    private static void poshukZaBal() {
        double bal = vvestyBal();
        
        List<Abiturient> results = abiturients.stream()
                .filter(a -> a.getSeredniyBal() == bal)
                .toList();
        
        if (results.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв з балом " + bal);
        } else {
            results.forEach(System.out::println);
            System.out.println("Знайдено записiв: " + results.size());
        }
    }

    /**
     * Показує список абітурієнтів, середній бал яких вище заданого.
     */
    private static void pokazatyZBalomVysche() {
        System.out.print("Введiть мiнiмальний бал: ");
        double minBal = vvestyBal();

        List<Abiturient> filtered = abiturients.stream()
                .filter(a -> a.getSeredniyBal() > minBal)
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("Не знайдено абiтурiєнтiв з балом вище " + minBal);
            return;
        }

        System.out.println("\nАБIТУРIЄНТИ З БАЛОМ ВИЩЕ " + minBal);
        System.out.println("=".repeat(105));
        System.out.printf("%-5s | %-15s | %-12s | %-15s | %-20s | %-12s | %-6s%n",
                "ID", "Прiзвище", "Iм'я", "По-батьковi", "Адреса", "Телефон", "Бал");
        System.out.println("-".repeat(105));

        for (Abiturient a : filtered) {
            System.out.printf("%-5d | %-15s | %-12s | %-15s | %-20s | %-12s | %-6.2f%n",
                    a.getId(),
                    obmezhyty(a.getPrizvysche(), 15),
                    obmezhyty(a.getImya(), 12),
                    obmezhyty(a.getPobatkovi(), 15),
                    obmezhyty(a.getAdressa(), 20),
                    obmezhyty(a.getTelefon(), 12),
                    a.getSeredniyBal());
        }

        System.out.println("=".repeat(105));
        System.out.println("Знайдено записiв: " + filtered.size());
    }

    /**
     * Показує топ N абітурієнтів з найвищим середнім балом.
     */
    private static void pokazatyTopN() {
        if (abiturients.isEmpty()) {
            System.out.println("База порожня.");
            return;
        }

        System.out.print("Введiть кiлькiсть абiтурiєнтiв для виводу: ");
        int n;
        try {
            n = Integer.parseInt(scanner.nextLine());
            if (n <= 0) {
                System.out.println("Помилка! Число має бути бiльше 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка! Введiть цiле число.");
            return;
        }

        List<Abiturient> topN = abiturients.stream()
                .sorted(Comparator.comparingDouble(Abiturient::getSeredniyBal).reversed())
                .limit(n)
                .toList();

        System.out.println("\nТОП " + n + " АБIТУРIЄНТIВ З НАЙВИЩИМ БАЛОМ");
        System.out.println("=".repeat(105));
        System.out.printf("%-5s | %-15s | %-12s | %-15s | %-20s | %-12s | %-6s%n",
                "ID", "Прiзвище", "Iм'я", "По-батьковi", "Адреса", "Телефон", "Бал");
        System.out.println("-".repeat(105));

        for (Abiturient a : topN) {
            System.out.printf("%-5d | %-15s | %-12s | %-15s | %-20s | %-12s | %-6.2f%n",
                    a.getId(),
                    obmezhyty(a.getPrizvysche(), 15),
                    obmezhyty(a.getImya(), 12),
                    obmezhyty(a.getPobatkovi(), 15),
                    obmezhyty(a.getAdressa(), 20),
                    obmezhyty(a.getTelefon(), 12),
                    a.getSeredniyBal());
        }

        System.out.println("=".repeat(105));
        System.out.println("Виведено записiв: " + topN.size());
    }

    /**
     * Сортує список абітурієнтів за прізвищем, а потім за ім'ям.
     * Використовує лексикографічне сортування.
     */
    private static void sortuvaty() {
        abiturients.sort(Comparator.comparing(Abiturient::getPrizvysche)
                .thenComparing(Abiturient::getImya));
    }

    /**
     * Видаляє абітурієнта з бази за вказаним ID.
     * Виводить повідомлення про успіх або невдачу операції.
     */
    private static void vydalytyZaID() {
        System.out.print("Введiть ID абiтурiєнта для видалення (6 цифр): ");
        String input = scanner.nextLine().trim();

        // Перевірка формату ID
        if (!input.matches("\\d{6}")) {
            System.out.println("Помилка! ID має складатися з 6 цифр.");
            return;
        }

        int id = Integer.parseInt(input);

        // Спроба видалити
        boolean removed = abiturients.removeIf(a -> a.getId() == id);
        System.out.println(removed ? "Абiтурiєнта видалено." : "Не знайдено абiтурiєнта з таким ID.");
    }

    /**
     * Видаляє всю базу даних абітурієнтів після підтвердження користувача.
     * Запитує підтвердження перед виконанням операції.
     */
    private static void vydalytyVsyuBazu() {
        System.out.print("Ви впевненi, що хочете видалити всi данi? (т/н): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("т")) {
            abiturients.clear();
            System.out.println("Усi данi успiшно видалено.");
        } else {
            System.out.println("Видалення скасовано.");
        }
    }

    /**
     * Зберігає поточну базу даних абітурієнтів у файл.
     * Використовує формат, визначений у методі {@link Abiturient#toFileFormat()}.
     * У разі помилки виводить відповідне повідомлення.
     */
    private static void zberehtyUFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(currentFile))) {
            for (Abiturient a : abiturients) pw.println(a.toFileFormat());
            System.out.println("Базу збережено у файл: " + currentFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Помилка збереження: " + e.getMessage());
        }
    }
}
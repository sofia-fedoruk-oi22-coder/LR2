/**
 * Клас для представлення абітурієнта навчального закладу.
 * <p>
 * Зберігає інформацію про абітурієнта, включаючи особисті дані,
 * контактну інформацію та середній бал атестату.
 * Надає методи для серіалізації та десеріалізації даних у файловий формат.
 * 
 * @author Your Name
 * @version 1.0
 */
public class Abiturient {
    /** Унікальний ідентифікатор абітурієнта */
    private int id;
    
    /** Прізвище абітурієнта */
    private String prizvysche;
    
    /** Ім'я абітурієнта */
    private String imya;
    
    /** По-батькові абітурієнта */
    private String pobatkovi;
    
    /** Адреса проживання абітурієнта */
    private String adressa;
    
    /** Контактний телефон абітурієнта */
    private String telefon;
    
    /** Середній бал атестату абітурієнта (0-100) */
    private double seredniyBal;

    /**
     * Конструктор за замовчуванням.
     * Створює порожній об'єкт абітурієнта без ініціалізації полів.
     */
    public Abiturient() {
    }

    /**
     * Конструктор з параметрами для створення повністю ініціалізованого об'єкта абітурієнта.
     * 
     * @param id унікальний ідентифікатор абітурієнта
     * @param prizvysche прізвище абітурієнта
     * @param imya ім'я абітурієнта
     * @param pobatkovi по-батькові абітурієнта
     * @param adressa адреса проживання абітурієнта
     * @param telefon контактний телефон абітурієнта
     * @param seredniyBal середній бал атестату (0-100)
     */
    public Abiturient(int id, String prizvysche, String imya, String pobatkovi, String adressa, String telefon, double seredniyBal) {
        this.id = id;
        this.prizvysche = prizvysche;
        this.imya = imya;
        this.pobatkovi = pobatkovi;
        this.adressa = adressa;
        this.telefon = telefon;
        this.seredniyBal = seredniyBal;
    }

    /**
     * Встановлює ідентифікатор абітурієнта.
     * 
     * @param id новий ідентифікатор абітурієнта
     * @return встановлений ідентифікатор
     */
    public int setId(int id) {
        return this.id = id;
    }
    
    /**
     * Повертає ідентифікатор абітурієнта.
     * 
     * @return ідентифікатор абітурієнта
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює прізвище абітурієнта.
     * 
     * @param prizvysche нове прізвище абітурієнта
     * @return встановлене прізвище
     */
    public String setPrizvysche(String prizvysche) {
        return this.prizvysche = prizvysche;
    }
    
    /**
     * Повертає прізвище абітурієнта.
     * 
     * @return прізвище абітурієнта
     */
    public String getPrizvysche() {
        return prizvysche;
    }

    /**
     * Встановлює ім'я абітурієнта.
     * 
     * @param imya нове ім'я абітурієнта
     * @return встановлене ім'я
     */
    public String setImya(String imya) {
        return this.imya = imya;
    }
    
    /**
     * Повертає ім'я абітурієнта.
     * 
     * @return ім'я абітурієнта
     */
    public String getImya() {
        return imya;
    }

    /**
     * Встановлює по-батькові абітурієнта.
     * 
     * @param pobatkovi нове по-батькові абітурієнта
     * @return встановлене по-батькові
     */
    public String setPobatkovi(String pobatkovi) {
        return this.pobatkovi = pobatkovi;
    }
    
    /**
     * Повертає по-батькові абітурієнта.
     * 
     * @return по-батькові абітурієнта
     */
    public String getPobatkovi() {
        return pobatkovi;
    }

    /**
     * Встановлює адресу проживання абітурієнта.
     * 
     * @param adressa нова адреса абітурієнта
     * @return встановлена адреса
     */
    public String setAdressa(String adressa) {
        return this.adressa = adressa;
    }
    
    /**
     * Повертає адресу проживання абітурієнта.
     * 
     * @return адреса абітурієнта
     */
    public String getAdressa() {
        return adressa;
    }

    /**
     * Встановлює контактний телефон абітурієнта.
     * 
     * @param telefon новий номер телефону абітурієнта
     * @return встановлений номер телефону
     */
    public String setTelefon(String telefon) {
        return this.telefon = telefon;
    }
    
    /**
     * Повертає контактний телефон абітурієнта.
     * 
     * @return номер телефону абітурієнта
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * Встановлює середній бал атестату абітурієнта.
     * 
     * @param seredniyBal новий середній бал (має бути в діапазоні 0-100)
     * @return встановлений середній бал
     */
    public double setSeredniyBal(double seredniyBal) {
        return this.seredniyBal = seredniyBal;
    }
    
    /**
     * Повертає середній бал атестату абітурієнта.
     * 
     * @return середній бал абітурієнта
     */
    public double getSeredniyBal() {
        return seredniyBal;
    }
   
    /**
     * Повертає рядкове представлення об'єкта абітурієнта для виведення на екран.
     * Включає всі основні поля об'єкта у читабельному форматі.
     * 
     * @return рядкове представлення абітурієнта з усіма полями
     */
    @Override
    public String toString() {
        return "Прізвище = " + prizvysche + ' ' +
                ", Ім'я = " + imya + ' ' +
                ", По батькові = " + pobatkovi + ' ' +
                ", Адреса = " + adressa + ' ' +
                ", Телефон = " + telefon + ' ' +
                ", Середній бал = " + seredniyBal;
    }

    /**
     * Перетворює об'єкт абітурієнта у рядок для збереження у файл.
     * Використовує символ ";" як розділювач між полями.
     * <p>
     * Формат: {@code id;прізвище;ім'я;по-батькові;адреса;телефон;середній_бал}
     * 
     * @return рядок у форматі для збереження у файл
     * @see #fromFileLine(String)
     */
    public String toFileFormat() {
        return id + ";" + prizvysche + ";" + imya + ";" + pobatkovi + ";" + adressa + ";" + telefon + ";" + seredniyBal;
    }

    /**
     * Створює об'єкт абітурієнта з рядка, зчитаного з файлу.
     * Очікує, що рядок містить 7 полів, розділених символом ";".
     * <p>
     * Формат рядка: {@code id;прізвище;ім'я;по-батькові;адреса;телефон;середній_бал}
     * 
     * @param line рядок з файлу для парсингу
     * @return новий об'єкт {@code Abiturient} або {@code null}, якщо формат рядка некоректний
     * @throws NumberFormatException якщо ID або середній бал не можуть бути перетворені у числа
     * @see #toFileFormat()
     */
    public static Abiturient fromFileLine(String line) {
        String[] parts = line.split(";");
        if (parts.length != 7) return null;
        return new Abiturient(
                Integer.parseInt(parts[0]),
                parts[1], parts[2], parts[3], parts[4], parts[5],
                Double.parseDouble(parts[6])
        );
    }
}
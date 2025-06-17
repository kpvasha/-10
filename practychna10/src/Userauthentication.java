import java.util.Scanner;
import javax.naming.AuthenticationException;

public class Userauthentication {
    private static final int MAX_USERS = 15;
    private static final int MIN_USERNAME_LENGTH = 5;
    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final int MIN_DIGITS_IN_PASSWORD = 3;
    private static final int MIN_SPECIAL_CHARS_IN_PASSWORD = 1;

    private String[] usernames;
    private String[] passwords;
    private String[] bannedPasswords;
    private int userCount;
    private int bannedPasswordsCount;
    private static Scanner scanner = new Scanner(System.in);

    public Userauthentication() {
        usernames = new String[MAX_USERS];
        passwords = new String[MAX_USERS];
        bannedPasswords = new String[50];

        bannedPasswords[0] = "password";
        bannedPasswords[1] = "admin";
        bannedPasswords[2] = "pass";
        bannedPasswords[3] = "qwerty";
        bannedPasswords[4] = "ytrewq";
        bannedPasswords[5] = "123456";
        bannedPasswordsCount = 6;
        userCount = 0;
    }

    public void register(String username, String password) throws AuthenticationException {
        if (userCount >= MAX_USERS) {
            throw new AuthenticationException("Максимальна кількість користувачів - 15");
        }
        trueUsername(username);

        for(int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                throw new AuthenticationException("Логін вже зайнятий");
            }
        }
        truePassword(password);
        usernames[userCount] = username;
        passwords[userCount] = password;
        userCount++;

        System.out.println("Користувача зареєстровано");
    }

    public void delete(String username) throws AuthenticationException {
        int userIndex = -1;

        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                userIndex = i;
                break;
            }
        }

        if (userIndex == -1) {
            throw new AuthenticationException("Логін '" + username + "' не знайдено.");
        }

        for (int i = userIndex; i < userCount - 1; i++) {
            usernames[i] = usernames[i + 1];
            passwords[i] = passwords[i + 1];
        }

        usernames[userCount - 1] = null;
        passwords[userCount - 1] = null;
        userCount--;

        System.out.println("Користувач видален.");
    }

    public void authenticateUser(String username, String password) throws AuthenticationException {
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                if (passwords[i].equals(password)) {
                    System.out.println("Користувача аутентифіковано.");
                    return;
                } else {
                    throw new AuthenticationException("Невірний пароль");
                }
            }
        }

        throw new AuthenticationException("Логін '" + username + "' не знайдено.");
    }

    private void trueUsername(String username) throws AuthenticationException {
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new AuthenticationException("Логін має мати довжину " + MIN_USERNAME_LENGTH + " символів.");
        }

        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ') {
                throw new AuthenticationException("Логін не може містити пробілів.");
            }
        }
    }

    private void truePassword(String password) throws AuthenticationException {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new AuthenticationException("Пароль має містити хоча б " + MIN_PASSWORD_LENGTH + " символів.");
        }

        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) == ' ') {
                throw new AuthenticationException("Пароль не може містити пробілів.");
            }
        }

        int digitCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        if (digitCount < MIN_DIGITS_IN_PASSWORD) {
            throw new AuthenticationException("Пароль має містити хоча б " + MIN_DIGITS_IN_PASSWORD + " цифри.");
        }

        int specialCharCount = 0;
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                specialCharCount++;
            }
        }

        if (specialCharCount < MIN_SPECIAL_CHARS_IN_PASSWORD) {
            throw new AuthenticationException("Пароль має містити хоча б " + MIN_SPECIAL_CHARS_IN_PASSWORD + " спеціальних символів.");
        }

        for (int i = 0; i < bannedPasswordsCount; i++) {
            if (bannedPasswords[i].equals(password)) {
                throw new AuthenticationException("Пароль заблоковано.");
            }
        }
    }

    public void addBannedPassword(String bannedPassword) throws AuthenticationException {
        for (int i = 0; i < bannedPasswordsCount; i++) {
            if (bannedPasswords[i].equals(bannedPassword)) {
                throw new AuthenticationException("Цей пароль заборонено.");
            }
        }

        if (bannedPasswordsCount < bannedPasswords.length) {
            bannedPasswords[bannedPasswordsCount] = bannedPassword;
            bannedPasswordsCount++;
            System.out.println("Пароль '" + bannedPassword + "' заблокований.");
        } else {
            throw new AuthenticationException("Максимальна кількість заборонених паролів.");
        }
    }

    public void printAllUsers() {
        if (userCount == 0) {
            System.out.println("Немає зареєстрованих користувачів.");
            return;
        }

        System.out.println("Список користувачів:");
        for (int i = 0; i < userCount; i++) {
            System.out.println((i + 1) + ". " + usernames[i]);
        }
    }

    public static void main(String[] args) {
        Userauthentication auth = new Userauthentication();
        boolean running = true;

        while (running) {
            try {
                System.out.println("""
                        Меню реєстрації
                        1. Реєстрація нового користувача
                        2. Видалення користувача
                        3. Аутентифікація користувача
                        4. Додати заборонений пароль
                        5. Всі користувачи
                        0. Вихід""");
                System.out.print("Оберіть: ");

                int choice = readInt();

                switch (choice) {
                    case 1:
                        registerUserMenu(auth);
                        break;
                    case 2:
                        deleteUserMenu(auth);
                        break;
                    case 3:
                        authenticateUserMenu(auth);
                        break;
                    case 4:
                        addBannedPasswordMenu(auth);
                        break;
                    case 5:
                        auth.printAllUsers();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Завершення");
                        break;
                    default:
                        System.out.println("Невірно");
                }
            } catch (Exception e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    private static void registerUserMenu(Userauthentication auth) throws AuthenticationException {
        System.out.print("Введіть логін (мін " + MIN_USERNAME_LENGTH + " символів): ");
        String username = scanner.nextLine();

        System.out.print("Введіть пароль (мін " + MIN_PASSWORD_LENGTH + " символів, " +
                MIN_DIGITS_IN_PASSWORD + " цифри, 1 символ): ");
        String password = scanner.nextLine();

        auth.register(username, password);
    }

    private static void deleteUserMenu(Userauthentication auth) throws AuthenticationException {
        System.out.print("Введіть логін користувача для видалення: ");
        String username = scanner.nextLine();

        auth.delete(username);
    }

    private static void authenticateUserMenu(Userauthentication auth) throws AuthenticationException {
        System.out.print("Введіть логін: ");
        String username = scanner.nextLine();

        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();

        auth.authenticateUser(username, password);
    }

    private static void addBannedPasswordMenu(Userauthentication auth) throws AuthenticationException {
        System.out.print("Введіть пароль для заблокування: ");
        String password = scanner.nextLine();

        auth.addBannedPassword(password);
    }

    private static int readInt() throws AuthenticationException {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new AuthenticationException("Введіть цифри.");
        }
    }
}
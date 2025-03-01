import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase PasswordGenerator.
 * Proporciona métodos para generar contraseñas aleatorias seguras y configurables.
 */
public class PasswordGenerator {

    // Definición de los grupos de caracteres
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*";

    private static final Random RANDOM = new Random();

    /**
     * Versión 1 (default).
     * Genera una contraseña aleatoria de longitud 'length' que cumple:
     * - Al menos 8 caracteres.
     * - Al menos una letra minúscula, una letra mayúscula, un dígito y un carácter especial.
     *
     * @param length La longitud deseada para la contraseña.
     * @return La contraseña generada.
     * @throws IllegalArgumentException si la longitud es menor a 8.
     */
    public static String generatePassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("La longitud mínima de la contraseña debe ser 8 caracteres.");
        }

        List<Character> passwordChars = new ArrayList<>();

        // Se aseguran los caracteres obligatorios para cumplir los requisitos
        passwordChars.add(randomCharFrom(LOWERCASE));
        passwordChars.add(randomCharFrom(UPPERCASE));
        passwordChars.add(randomCharFrom(DIGITS));
        passwordChars.add(randomCharFrom(SPECIAL_CHARACTERS));

        // Pool de caracteres combinados para completar la contraseña
        String allChars = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;

        // Completar la contraseña hasta alcanzar la longitud requerida
        for (int i = passwordChars.size(); i < length; i++) {
            passwordChars.add(randomCharFrom(allChars));
        }

        // Se mezclan los caracteres para evitar posiciones fijas
        Collections.shuffle(passwordChars, RANDOM);

        // Convertir la lista de caracteres a cadena
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }
        return password.toString();
    }

    /**
     * Versión 2 (configurable).
     * Genera una contraseña basada en la configuración definida en PasswordConfigDTO.
     *
     * @param config Objeto que contiene las políticas y restricciones para la contraseña.
     * @return La contraseña generada que cumple con los requisitos establecidos.
     * @throws IllegalArgumentException si la longitud mínima no es suficiente para cumplir todas las restricciones.
     */
    public static String generatePassword(PasswordConfigDTO config) {
        // Calcular la cantidad total de caracteres obligatorios a partir de las restricciones
        int requiredCharacters = 0;
        if (config.isRestrictMinDigits()) {
            requiredCharacters += config.getMinDigits();
        }
        if (config.isRestrictMinUpperCaseLetters()) {
            requiredCharacters += config.getMinUpperCaseLetters();
        }
        if (config.isRestrictMinLowerCaseLetters()) {
            requiredCharacters += config.getMinLowerCaseLetters();
        }
        if (config.isRestrictMinNonAlphanumericCharacters()) {
            requiredCharacters += config.getMinNonAlphanumericCharacters();
        }

        if (config.getMinLength() < requiredCharacters) {
            throw new IllegalArgumentException("La longitud mínima no es suficiente para cumplir con las restricciones configuradas.");
        }

        List<Character> passwordChars = new ArrayList<>();

        // Agregar dígitos si se requiere
        if (config.isRestrictMinDigits()) {
            for (int i = 0; i < config.getMinDigits(); i++) {
                passwordChars.add(randomCharFrom(DIGITS));
            }
        }
        // Agregar letras mayúsculas si se requiere
        if (config.isRestrictMinUpperCaseLetters()) {
            for (int i = 0; i < config.getMinUpperCaseLetters(); i++) {
                passwordChars.add(randomCharFrom(UPPERCASE));
            }
        }
        // Agregar letras minúsculas si se requiere
        if (config.isRestrictMinLowerCaseLetters()) {
            for (int i = 0; i < config.getMinLowerCaseLetters(); i++) {
                passwordChars.add(randomCharFrom(LOWERCASE));
            }
        }
        // Agregar caracteres especiales si se requiere
        if (config.isRestrictMinNonAlphanumericCharacters()) {
            for (int i = 0; i < config.getMinNonAlphanumericCharacters(); i++) {
                passwordChars.add(randomCharFrom(SPECIAL_CHARACTERS));
            }
        }

        // Rellenar el resto de la contraseña con caracteres aleatorios de un pool combinado
        int remainingLength = config.getMinLength() - passwordChars.size();
        String allChars = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
        for (int i = 0; i < remainingLength; i++) {
            passwordChars.add(randomCharFrom(allChars));
        }

        // Mezclar para evitar patrones predecibles
        Collections.shuffle(passwordChars, RANDOM);

        // Construir y retornar la contraseña
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }
        return password.toString();
    }

    /**
     * Método auxiliar para obtener un carácter aleatorio de una cadena dada.
     *
     * @param chars Cadena de caracteres de donde se extrae el carácter.
     * @return Un carácter aleatorio de la cadena.
     */
    private static char randomCharFrom(String chars) {
        int index = RANDOM.nextInt(chars.length());
        return chars.charAt(index);
    }

    /**
     * Método main para demostrar ejemplos de uso de ambas versiones del generador.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        // Ejemplo de uso de la versión 1 (default)
        System.out.println("Contraseña default: " + generatePassword(10));

        // Configuración para la versión 2 (configurable)
        PasswordConfigDTO config = new PasswordConfigDTO();
        config.setRestrictMinDigits(true);
        config.setMinDigits(2);
        config.setRestrictMinUpperCaseLetters(true);
        config.setMinUpperCaseLetters(2);
        config.setRestrictMinLowerCaseLetters(true);
        config.setMinLowerCaseLetters(2);
        config.setRestrictMinNonAlphanumericCharacters(true);
        config.setMinNonAlphanumericCharacters(2);
        config.setMinLength(10);

        System.out.println("Contraseña configurable: " + generatePassword(config));
    }
}

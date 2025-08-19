import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConversorApp {

    private static final String API_KEY = "25aaffab0febe831d614dc77";

    private static final String[] NOMBRES_MONEDAS = {
            "Dólar estadounidense", "Dólar canadiense", "Peso mexicano",
            "Peso colombiano", "Peso argentino", "Real brasileño",
            "Euros", "Yen japonés", "Won surcoreano", "Yuan chino"
    };

    private static final String[] CODIGOS_MONEDAS = {
            "USD", "CAD", "MXN",
            "COP", "ARS", "BRL",
            "EUR", "JPY", "KRW", "CNY"
    };

    public static double getExchangeRate(String fromCurrency, String toCurrency) {
        double rate = 0.0;
        try {
            String urlStr = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/"
                    + fromCurrency + "/" + toCurrency;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            Pattern pattern = Pattern.compile("\"conversion_rate\":([0-9.]+)");
            Matcher matcher = pattern.matcher(response.toString());
            if (matcher.find()) {
                rate = Double.parseDouble(matcher.group(1));
            } else {
                System.out.println("No se pudo obtener la tasa de conversión. Verifica las monedas.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rate;
    }


    private static void mostrarMenu() {
        System.out.println("\nOpciones de monedas:");
        for (int i = 0; i < NOMBRES_MONEDAS.length; i++) {
            System.out.println((i + 1) + ". " + NOMBRES_MONEDAS[i]);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        // Mensaje de bienvenida
        System.out.println("======================================");
        System.out.println("   ¡Bienvenido al Conversor de Monedas!");
        System.out.println("======================================\n");

        while (continuar) {
            // Mensaje para seleccionar moneda
            System.out.println("Por favor, selecciona la moneda de origen:");
            mostrarMenu();
            System.out.print("Ingresa el número correspondiente a la moneda de origen: ");
            int fromIndex = scanner.nextInt() - 1;

            System.out.println("\nAhora, selecciona la moneda de destino:");
            mostrarMenu();
            System.out.print("Ingresa el número correspondiente a la moneda de destino: ");
            int toIndex = scanner.nextInt() - 1;

            System.out.print("\nIngresa la cantidad a convertir: ");
            double amount = scanner.nextDouble();

            String fromCurrency = CODIGOS_MONEDAS[fromIndex];
            String toCurrency = CODIGOS_MONEDAS[toIndex];

            double rate = getExchangeRate(fromCurrency, toCurrency);
            if (rate != 0) {
                double convertedAmount = amount * rate;
                System.out.println("\n" + amount + " " + NOMBRES_MONEDAS[fromIndex] + " = "
                        + convertedAmount + " " + NOMBRES_MONEDAS[toIndex]);
            } else {
                System.out.println("No se pudo realizar la conversión.");
            }

            // Preguntar si continuar
            System.out.print("\n¿Deseas hacer otra conversión? (si/no): ");
            String respuesta = scanner.next().toLowerCase();
            if (!respuesta.equals("si")) {
                continuar = false;
            }
        }

        System.out.println("\nGracias por usar el Conversor de Monedas. ¡Hasta luego!");
        scanner.close();
    }
}

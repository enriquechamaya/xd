package trismegistoplanilla.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamsValidation {

  private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{1,})$";
  private static final String PATTERN_NUMBERS_ONLY = "[0-9]+";
  private static final String PATTERN_LETTERS_ONLY = "^[a-zA-Z\\u00C0-\\u00ff\\s]+$";
  private static final String PATTERN_ALPHANUMERIC_ONLY = "^[a-zA-Z0-9]+$";
  private static final String PATTERN_ALPHANUMERIC_ONLY_WITH_SPACES = "^\\s*[\\da-zA-Z][\\da-zA-Z\\\\u00C0-\\u00ff\\s]*$";
  private static final String PATTERN_DATE_ONLY = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
  private static final String PATTERN_LATITUDE = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,20}";
  private static final String PATTERN_LONGITUDE = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,20}";
  private static final String PATTERN_DECIMAL = "^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";

  // validar correo electrónico
  public static boolean validaCorreo(String correo) {
    Pattern pattern = Pattern.compile(PATTERN_EMAIL);
    Matcher matcher = pattern.matcher(correo);
    return matcher.matches();
  }

  // validar longitud de documento
  public static boolean validaLongitudNumeroDocumento(int longitud, String numeroDocumento) {
    if (numeroDocumento.length() == longitud) {
      return true;
    }
    return false;
  }

  // valida el  numero de documento ingresado por el usuario
  public static boolean validaNumeroDocumento(String TipoEntrada, String numeroDocumento) {
    Pattern pattern = null;
    Matcher matcher = null;
    // alfanumerico
    if (TipoEntrada.equals("A")) {
      pattern = Pattern.compile(PATTERN_ALPHANUMERIC_ONLY);
      matcher = pattern.matcher(numeroDocumento);
    } // numerico
    else if (TipoEntrada.equals("N")) {
      pattern = Pattern.compile(PATTERN_NUMBERS_ONLY);
      matcher = pattern.matcher(numeroDocumento);
    }
    return matcher.matches();
  }

  // valida que solo sea numeros
  public static boolean validaSoloNumeros(int param) {
    Pattern pattern = Pattern.compile(PATTERN_NUMBERS_ONLY);
    Matcher matcher = pattern.matcher(String.valueOf(param));
    return matcher.matches();
  }

  // valida que solo sea letras
  public static boolean validaSoloLetras(String param) {
    Pattern pattern = Pattern.compile(PATTERN_LETTERS_ONLY);
    Matcher matcher = pattern.matcher(param);
    return matcher.matches();
  }

  // valida que solo sea alfanumerico
  public static boolean validaSoloAlfanumerico(String param) {
    Pattern pattern = Pattern.compile(PATTERN_ALPHANUMERIC_ONLY);
    Matcher matcher = pattern.matcher(param);
    return matcher.matches();
  }

  // valida que solo sea fecha
  public static boolean validaSoloFecha(String param) {
    Pattern pattern = Pattern.compile(PATTERN_DATE_ONLY);
    Matcher matcher = pattern.matcher(param);
    return matcher.matches();
  }

  // valida el tipo de entrada
  public static boolean validaTipoEntrada(String TipoEntrada, String cadena) {
    Pattern pattern = null;
    Matcher matcher = null;
    // alfanumerico
    if (TipoEntrada.equals("A")) { // alfanumerico
      pattern = Pattern.compile(PATTERN_ALPHANUMERIC_ONLY);
      matcher = pattern.matcher(cadena);
    } // numerico
    else if (TipoEntrada.equals("N")) { // numerico
      pattern = Pattern.compile(PATTERN_NUMBERS_ONLY);
      matcher = pattern.matcher(cadena);
    }
    return matcher.matches();
  }

  public static boolean validaLongitudCadena(int longitud, String numeroDocumento) {
    if (numeroDocumento.length() == longitud) {
      return true;
    }
    return false;
  }

  public static boolean validaLatitud(String cadena) {
    Pattern pattern = Pattern.compile(PATTERN_LATITUDE);
    Matcher matcher = pattern.matcher(String.valueOf(cadena));
    return matcher.matches();
  }

  public static boolean validaLongitud(String cadena) {
    Pattern pattern = Pattern.compile(PATTERN_LONGITUDE);
    Matcher matcher = pattern.matcher(String.valueOf(cadena));
    return matcher.matches();
  }

  // valida que solo sea alfanumerico
  public static boolean validaSoloAlfanumericoConEspacios(String param) {
    Pattern pattern = Pattern.compile(PATTERN_ALPHANUMERIC_ONLY_WITH_SPACES);
    Matcher matcher = pattern.matcher(param);
    return matcher.matches();
  }

  // valida que solo decimal
  public static boolean validaSoloDecimal(String param) {
    Pattern pattern = Pattern.compile(PATTERN_DECIMAL);
    Matcher matcher = pattern.matcher(param);
    return matcher.matches();
  }

//  public static void main(String[] args) {
//    System.out.println(ParamsValidation.validaSoloLetras(""));
//  }
}

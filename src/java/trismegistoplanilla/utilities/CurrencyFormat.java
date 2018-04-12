package trismegistoplanilla.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyFormat {

  public static String getCustomCurrency(double currency) {
    BigDecimal cc = new BigDecimal(currency);
    return cc.setScale(2, RoundingMode.HALF_UP).toString();
  }

  /**
   * Método que retorna la fecha actual enviando como parámetro el formato
   * deseado.
   *
   * @param format Formato deseado para la fecha actual
   * @return Fecha actual
   */
  public static String getActualDate(String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    Date date = new Date();
    return dateFormat.format(date);
  }

//  public static void main(String[] args) {
//    String fechaActual = getActualDate("dd/MM/yyyy");
//    System.out.println(fechaActual);
//
//    String x_fechaActual = fechaActual.split("/")[0];
//    System.out.println(x_fechaActual);
//
//  }
}

package sabalan.paydar.mohtasham.ezibazi.controller.system.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class HelperText {



  private static String[] persianNumbers = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

  public static String toPersianNumber(String text) {
    if (text.length() == 0) {
      return "";
    }
    String out = "";
    int length = text.length();
    for (int i = 0; i < length; i++) {
      char c = text.charAt(i);
      if ('0' <= c && c <= '9') {
        int number = Integer.parseInt(String.valueOf(c));
        out += persianNumbers[number];
      } else if (c == '٫'){
        out += '،';
      }else if(c == ','){
        out += ',';
      } else {
        out += c;
      }
    }
    return out;
  }




  public static String split_price(int price) {
    try {
      DecimalFormat decimalFormat = new DecimalFormat();
      DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
      decimalFormatSymbol.setGroupingSeparator(',');
      decimalFormat.setDecimalFormatSymbols(decimalFormatSymbol);
      return toPersianNumber(decimalFormat.format(price));
    } catch (Exception ex) {
      return toPersianNumber(String.valueOf(price));
    }
  }

  public static String split_price(String priceText) {
    int price = Integer.valueOf(priceText);
    try {
      DecimalFormat decimalFormat = new DecimalFormat();
      DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
      decimalFormatSymbol.setGroupingSeparator(',');
      decimalFormat.setDecimalFormatSymbols(decimalFormatSymbol);
      return toPersianNumber(decimalFormat.format(price));
    } catch (Exception ex) {
      return toPersianNumber(String.valueOf(price));
    }
  }
}

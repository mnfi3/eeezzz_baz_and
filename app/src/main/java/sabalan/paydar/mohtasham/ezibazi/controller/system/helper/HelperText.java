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


  public static String splitedPersianToLatin(String price){
    if (price.length() == 0) {
      return "";
    }

    price = price.replace(persianNumbers[0], "0");
    price = price.replace(persianNumbers[1], "1");
    price = price.replace(persianNumbers[2], "2");
    price = price.replace(persianNumbers[3], "3");
    price = price.replace(persianNumbers[4], "4");
    price = price.replace(persianNumbers[5], "5");
    price = price.replace(persianNumbers[6], "6");
    price = price.replace(persianNumbers[7], "7");
    price = price.replace(persianNumbers[8], "8");
    price = price.replace(persianNumbers[9], "9");
    price = price.replace(",", "");
    price = price.replace("،", "");
//
    return price;

//    StringBuilder out = new StringBuilder();
//    int length = price.length();
//    for (int i = 0; i < length; i++) {
//      String c = Character.toString(price.charAt(i));
//      if (c.equals(persianNumbers[0])) {
//        out.append("0");
//      } else if (c.equals(persianNumbers[1])) {
//        out.append("1");
//      } else if (c.equals(persianNumbers[2])) {
//        out.append("2");
//      } else if (c.equals(persianNumbers[3])) {
//        out.append("3");
//      } else if (c.equals(persianNumbers[4])) {
//        out.append("4");
//      } else if (c.equals(persianNumbers[5])) {
//        out.append("5");
//      } else if (c.equals(persianNumbers[6])) {
//        out.append("6");
//      } else if (c.equals(persianNumbers[7])) {
//        out.append("7");
//      } else if (c.equals(persianNumbers[8])) {
//        out.append("8");
//      } else if (c.equals(persianNumbers[9])) {
//        out.append("9");
//      } else if (c.equals(Character.toString(','))) {
//        out.append("");
//      } else if (c.equals(Character.toString('،'))) {
//        out.append("");
//      }
//    }
//    return out.toString();
  }




}

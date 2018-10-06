package mohtasham.paydar.sabalan.ezbazi.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Finance {
  private int user_id;
  private String bank_card_number;
  private String bank_account_number;
  private String bank_shba_number;
  private String bank_account_owner_name;
  private int user_balance;




  public static class Parser{
    public static Finance parse(JSONObject object){
      Finance finance = new Finance();
      try {
        finance.setUser_id(object.getInt("user_id"));
        finance.setBank_card_number(object.getString("bank_card_number"));
        finance.setBank_account_number(object.getString("bank_account_number"));
        finance.setBank_shba_number(object.getString("bank_shba_number"));
        finance.setBank_account_owner_name(object.getString("bank_account_owner_name"));
        finance.setUser_balance(object.getInt("user_balance"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

      return finance;
    }
  }




  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getBank_card_number() {
    return bank_card_number;
  }

  public void setBank_card_number(String bank_card_number) {
    this.bank_card_number = bank_card_number;
  }

  public String getBank_account_number() {
    return bank_account_number;
  }

  public void setBank_account_number(String bank_account_number) {
    this.bank_account_number = bank_account_number;
  }

  public String getBank_shba_number() {
    return bank_shba_number;
  }

  public void setBank_shba_number(String bank_shba_number) {
    this.bank_shba_number = bank_shba_number;
  }

  public String getBank_account_owner_name() {
    return bank_account_owner_name;
  }

  public void setBank_account_owner_name(String bank_account_owner_name) {
    this.bank_account_owner_name = bank_account_owner_name;
  }

  public int getUser_balance() {
    return user_balance;
  }

  public void setUser_balance(int user_balance) {
    this.user_balance = user_balance;
  }





}

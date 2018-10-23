package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.UserDetailService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.RentService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.model.Finance;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class ActivityPurchase extends AppCompatActivity {

  private static final String TAG = "ActivityPurchase";

  private static final String TYPE_RENT = "RENT";
  private static final String TYPE_SHOP = "SHOP";



  //first block
  TextView txt_show_product;
  RoundedImageView img_game;
  TextView txt_region;
  TextView txt_name;
  TextView txt_game_status;

  //second block
  TextView txt_show_address;
  AppCompatSpinner spinner_state;
  AppCompatSpinner spinner_city;
  TextView txt_show_state;
  TextView txt_show_city;
  EditText edt_address_content;
  EditText edt_phone_number;
  EditText edt_postcode;

  //third block
  TextView txt_show_payment;
  TextView txt_game_price;
  TextView txt_show_game_price;
  TextView txt_rent_price;
  TextView txt_show_rent_price;
  TextView txt_sum_price;
  TextView txt_show_sum_price;

  //buttons
  AppCompatButton btn_wallet_pay;
  AppCompatButton btn_bank_pay;

  String type;
  int id;
  int rent_type_id = 0;

  Game game;
  private int user_balance = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_purchase);

    setupViews();
    setTypeFace();

    Bundle extras = getIntent().getExtras();
    if(extras != null) {
      type = extras.getString("TYPE");
      id = extras.getInt("ID");
      if(type.equals(TYPE_RENT)) {
        rent_type_id = extras.getInt("RENT_TYPE_ID");
      }
    }else {
      type = (String) savedInstanceState.getSerializable("TYPE");
      id = (int) savedInstanceState.getSerializable("ID");
      if(type.equals(TYPE_RENT)) {
        rent_type_id = (int) savedInstanceState.getSerializable("RENT_TYPE_ID");
      }
    }

    switch (type) {
      case TYPE_RENT:
        setRent();
        break;
      case TYPE_SHOP:
        setShop();
        break;
      default:
        Log.i(TAG, "onCreate: " + "data error");
        break;
    }

  }



  private void setupViews(){
    txt_show_product = findViewById(R.id.txt_show_product);
    img_game = findViewById(R.id.img_game);
    txt_region = findViewById(R.id.txt_region);
    txt_name = findViewById(R.id.txt_name);
    txt_game_status = findViewById(R.id.txt_game_status);
    txt_show_address = findViewById(R.id.txt_show_address);
    spinner_state = findViewById(R.id.spinner_state);
    spinner_city = findViewById(R.id.spinner_city);
    txt_show_state = findViewById(R.id.txt_show_state);
    txt_show_city = findViewById(R.id.txt_show_city);
    edt_address_content = findViewById(R.id.edt_address_content);
    edt_phone_number = findViewById(R.id.edt_phone_number);
    edt_postcode = findViewById(R.id.edt_postcode);
    txt_show_payment = findViewById(R.id.txt_show_payment);
    txt_game_price = findViewById(R.id.txt_game_price);
    txt_show_game_price = findViewById(R.id.txt_show_game_price);
    txt_rent_price = findViewById(R.id.txt_rent_price);
    txt_show_rent_price = findViewById(R.id.txt_show_rent_price);
    txt_sum_price = findViewById(R.id.txt_sum_price);
    txt_show_sum_price = findViewById(R.id.txt_show_sum_price);
    btn_wallet_pay = findViewById(R.id.btn_wallet_pay);
    btn_bank_pay = findViewById(R.id.btn_bank_pay);

  }


  private void setTypeFace(){
    txt_show_product.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
    txt_region.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_name.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_game_status.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_address.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
    txt_show_state.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_city.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    edt_address_content.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    edt_phone_number.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    edt_postcode.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_payment.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
    txt_game_price.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_game_price.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_rent_price.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_rent_price.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_sum_price.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_sum_price.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    btn_wallet_pay.setTypeface(MyViews.getIranSansUltraLightFont(ActivityPurchase.this));
    btn_bank_pay.setTypeface(MyViews.getIranSansUltraLightFont(ActivityPurchase.this));
  }



  private void setRent(){
    RentService service = new RentService(ActivityPurchase.this);
    service.getSpecialRent(id, new RentService.onSpecialRentReceived() {
      @Override
      public void onReceived(int status, String message, Game game) {
        if (status == 1) {
          ActivityPurchase.this.game = game;
          int rent_price = getRentPrice();
          //fill rent views
        }
      }
    });

  }



  private void setShop(){
    ShopService shopService = new ShopService(ActivityPurchase.this);
    shopService.getSpecialShop(id, new ShopService.onSpecialShopReceived() {
      @Override
      public void onReceived(int status, String message, Game game) {
        if (status == 1) {
          ActivityPurchase.this.game = game;
          //fill shop views
        }
      }
    });

  }


  private int getRentPrice(){
    for (int i=0 ; i< G.rentTypes.size() ; i++){
      if (G.rentTypes.get(i).getId() == rent_type_id){
        return (game.getPrice() * G.rentTypes.get(i).getPrice_percent()) / 100;
      }
    }
    return 0;
  }


  private void getUserBalance(){
    UserDetailService service = new UserDetailService(ActivityPurchase.this);
    service.getUserFinance(new UserDetailService.onFinanceReceivedComplete() {
      @Override
      public void onComplete(int status, String message, Finance finance) {
        if (status == 1) {
          user_balance = finance.getUser_balance();
          //calculate pay with wallet
        }
      }
    });
  }
}

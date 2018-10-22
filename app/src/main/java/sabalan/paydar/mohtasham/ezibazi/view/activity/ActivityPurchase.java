package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.AccountService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.UserDetailService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.RentService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment.FinanceService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.model.Finance;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;

public class ActivityPurchase extends AppCompatActivity {

  private static final String TAG = "ActivityPurchase";

  private static final String TYPE_RENT = "RENT";
  private static final String TYPE_SHOP = "SHOP";

  String type;
  int id;
  int rent_type_id = 0;

  Game game;
  private int user_balance = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_purchase);


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
        Toast.makeText(ActivityPurchase.this, "rent", Toast.LENGTH_SHORT).show();
        setRent();
        break;
      case TYPE_SHOP:
        Toast.makeText(ActivityPurchase.this, "shop", Toast.LENGTH_SHORT).show();
        setShop();
        break;
      default:
        Log.i(TAG, "onCreate: " + "data error");
        break;
    }

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

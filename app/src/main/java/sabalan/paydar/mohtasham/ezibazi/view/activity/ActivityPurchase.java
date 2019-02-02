package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.UserDetailService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.address.AddressService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.RentService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.model.Address;
import sabalan.paydar.mohtasham.ezibazi.model.City;
import sabalan.paydar.mohtasham.ezibazi.model.Finance;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;
import sabalan.paydar.mohtasham.ezibazi.model.State;
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
  TextView txt_rent_day;
  TextView txt_game_status;

  //second block
  LinearLayout lyt_last_address;
  TextView txt_show_address;
  TextView txt_last_address_show;
  TextView txt_address_text;
  TextView txt_use_last_address;
  SwitchCompat swch_use_last_address;

  LinearLayout lyt_new_address;
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

  private Address address = null;

  private boolean is_use_last_address = false;

  private ArrayList<State> states = new ArrayList<>();
  private ArrayList<City> cities = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_purchase);

    setupViews();
    setTypeFace();

    getUserLastAddress();
    getUserBalance();
    getStates();

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
        ActivityPurchase.this.finish();
        break;
    }



    swch_use_last_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
          txt_address_text.setVisibility(View.VISIBLE);
          lyt_new_address.setVisibility(View.GONE);
          is_use_last_address = true;
        }else {
          txt_address_text.setVisibility(View.GONE);
          lyt_new_address.setVisibility(View.VISIBLE);
          is_use_last_address = false;
        }
      }
    });

  }



  private void setupViews(){
    txt_show_product = findViewById(R.id.txt_show_product);
    img_game = findViewById(R.id.img_game);
    txt_region = findViewById(R.id.txt_region);
    txt_name = findViewById(R.id.txt_name);
    txt_rent_day = findViewById(R.id.txt_rent_day);
    txt_game_status = findViewById(R.id.txt_game_status);
    lyt_last_address = findViewById(R.id.lyt_last_address);
    txt_show_address = findViewById(R.id.txt_show_address);
    txt_last_address_show = findViewById(R.id.txt_last_address_show);
    txt_address_text = findViewById(R.id.txt_address_text);
    txt_use_last_address = findViewById(R.id.txt_use_last_address);
    swch_use_last_address = findViewById(R.id.swch_use_last_address);
    lyt_new_address = findViewById(R.id.lyt_new_address);
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
    txt_region.setTypeface(MyViews.getRobotoLightFont(ActivityPurchase.this));
    txt_name.setTypeface(MyViews.getRobotoLightFont(ActivityPurchase.this));
    txt_rent_day.setTypeface(MyViews.getRobotoLightFont(ActivityPurchase.this));
    txt_game_status.setTypeface(MyViews.getIranSansLightFont(ActivityPurchase.this));
    txt_show_address.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
    txt_last_address_show.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
    txt_address_text.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
    txt_use_last_address.setTypeface(MyViews.getIranSansMediumFont(ActivityPurchase.this));//medium
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
          fillViews();
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
          fillViews();
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



  private void fillViews(){
    Picasso.with(ActivityPurchase.this).
      load(game.getApp_cover_photo_url())
//      .placeholder()
      .into(img_game);
    txt_name.setText(game.getName());
    txt_region.setText("Region : " + game.getRegion());

    if(type.equals(TYPE_RENT)){
      txt_rent_day.setText("اجاره " + RentType.findById(rent_type_id).getDay_count() + " روزه ");
      txt_game_status.setText("اجاره ای");
    }else if(type.equals(TYPE_SHOP)){
      txt_rent_day.setText("");
      txt_game_status.setText("فروشی");
    }

  }


  private void getUserLastAddress(){
    UserDetailService service = new UserDetailService(ActivityPurchase.this);
    service.getUserLastAddress(new UserDetailService.onLastAddressReceived() {
      @Override
      public void onComplete(int status, String message, Address address) {
        ActivityPurchase.this.address = address;
        if (address != null){
          fillWithAddress();
        }else {
          fillWithoutAddress();
        }
      }
    });
  }

  private void fillWithAddress(){
    txt_address_text.setText(address.getAddressText());
    txt_use_last_address.setVisibility(View.VISIBLE);
    swch_use_last_address.setChecked(true);
    is_use_last_address = true;
    swch_use_last_address.setVisibility(View.VISIBLE);
    lyt_new_address.setVisibility(View.GONE);
  }

  private void fillWithoutAddress(){
    txt_address_text.setVisibility(View.INVISIBLE);
    swch_use_last_address.setChecked(false);
    is_use_last_address = false;
    swch_use_last_address.setVisibility(View.INVISIBLE);
    txt_use_last_address.setVisibility(View.INVISIBLE);
    lyt_last_address.setVisibility(View.GONE);

  }


  private void getStates(){
    AddressService service = new AddressService(ActivityPurchase.this);
    service.getStates(new AddressService.onStatesReceived() {
      @Override
      public void onComplete(int status, String message, ArrayList<State> states) {
        ActivityPurchase.this.states = states;
        List<String> states_array = new ArrayList<>();
        for(int i=0 ; i<states.size() ; i++) {
          states_array.add(states.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPurchase.this, android.R.layout.simple_spinner_item, states_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter);
      }
    });
  }


  private void getStateCities(int state_id){
    AddressService service = new AddressService(ActivityPurchase.this);
    service.getStateCities(state_id, new AddressService.onStateCitiesReceived() {
      @Override
      public void onComplete(int status, String message, ArrayList<City> cities) {
        ActivityPurchase.this.cities = cities;
      }
    });
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

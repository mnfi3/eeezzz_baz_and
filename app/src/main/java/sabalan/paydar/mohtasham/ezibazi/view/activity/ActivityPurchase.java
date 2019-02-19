package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.account.UserDetailService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.address.AddressService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.RentService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.main.ShopService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment.RentPayService;
import sabalan.paydar.mohtasham.ezibazi.controller.api_service.payment.ShopPayService;
import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.controller.system.helper.HelperText;
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

  //center
  AVLoadingIndicatorView avl_wait;

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
  LinearLayout lyt_rent_price;
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
  int rent_price = 0;

  Game game;
  private int user_balance = 0;

  private Address last_address = null;

  private boolean is_use_last_address = false;

  private ArrayList<State> states = new ArrayList<>();
  private ArrayList<City> cities = new ArrayList<>();

  private int state_id = 0, city_id = 0;
  private int new_address_id = 0;
  private int sum_price = 0;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_purchase);

    setupViews();
    setTypeFace();
    avl_wait.setVisibility(View.VISIBLE);

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

    spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        state_id = states.get(i).getId();
        avl_wait.setVisibility(View.VISIBLE);
        getStateCities(state_id);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });


    spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        city_id = cities.get(i).getId();
        Toast.makeText(ActivityPurchase.this, ""+cities.get(i).getName(), Toast.LENGTH_LONG).show();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });



    btn_bank_pay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        avl_wait.setVisibility(View.VISIBLE);
        if(type.equals(TYPE_RENT)){
          rentGameWithBank();
        }else {
          shopGameWithBank();
        }
      }
    });



    btn_wallet_pay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        avl_wait.setVisibility(View.VISIBLE);
        if(type.equals(TYPE_RENT)){
          rentGameWithWallet();
        }else {
          shopGameWithWallet();
        }
      }
    });
  }







  private void setRent(){
    RentService service = new RentService(ActivityPurchase.this);
    service.getSpecialRent(id, new RentService.onSpecialRentReceived() {
      @Override
      public void onReceived(int status, String message, Game game) {
        if (status == 1) {
          ActivityPurchase.this.game = game;
          rent_price = getRentPrice();
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



  @SuppressLint("SetTextI18n")
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


    fillPrices();

  }


  private void getUserLastAddress(){
    UserDetailService service = new UserDetailService(ActivityPurchase.this);
    service.getUserLastAddress(new UserDetailService.onLastAddressReceived() {
      @Override
      public void onComplete(int status, String message, Address address) {
        avl_wait.setVisibility(View.INVISIBLE);
        ActivityPurchase.this.last_address = address;
        if (address != null){
          fillWithAddress();
        }else {
          fillWithoutAddress();
        }
      }
    });
  }

  private void fillWithAddress(){
    txt_address_text.setText(last_address.getAddressText());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPurchase.this, R.layout.item_style_spinner, states_array);
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
        avl_wait.setVisibility(View.INVISIBLE);
        ActivityPurchase.this.cities = cities;
        List<String> city_array = new ArrayList<>();
        for(int i=0 ; i<cities.size() ; i++) {
          city_array.add(cities.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPurchase.this, R.layout.item_style_spinner, city_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapter);
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


  @SuppressLint("SetTextI18n")
  private void fillPrices(){
    sum_price = game.getPrice() + rent_price;
    txt_game_price.setText(HelperText.split_price(game.getPrice()) + "\t تومان \t");
    if(type.equals(TYPE_RENT)){
      txt_rent_price.setText(HelperText.split_price(rent_price) + "\t تومان \t");
      txt_sum_price.setText(HelperText.split_price(game.getPrice() + rent_price) + "\t تومان \t");
    }else {
      lyt_rent_price.setVisibility(View.GONE);
      txt_game_price.setText(HelperText.split_price(game.getPrice()) + "\t تومان \t");
      txt_sum_price.setText(HelperText.split_price(game.getPrice()) + "\t تومان \t");
    }
  }








  //wallet
  private void rentGameWithWallet(){
    if(sum_price > user_balance){
      MyViews.makeText(ActivityPurchase.this, "موجودی کیف پول شما کمتر میباشد. لطفا اقدام به شارژ کیف پول خود نمایید", Toast.LENGTH_LONG);
      return;
    }


    final RentPayService service = new RentPayService(ActivityPurchase.this);
    if(is_use_last_address){
      JSONObject object = new JSONObject();
      try {
        object.put("game_id", game.getId());
        object.put("address_id", last_address.getId());
        object.put("rent_type_id", rent_type_id);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      service.rentWithWallet(object, new RentPayService.onRentWithWalletComplete() {
        @Override
        public void onComplete(int status, String message) {
          avl_wait.setVisibility(View.INVISIBLE);
          if(status == 0){
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
            restartApp();
          }
        }
      });

    }

    else {
      if(getNewAddressObject() == null) return;
      AddressService addressService = new AddressService(ActivityPurchase.this);
      addressService.saveAddress(getNewAddressObject(), new AddressService.onAddressSaved() {
        @Override
        public void onComplete(int status, String message, Address address) {
          if (status == 0) {
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            new_address_id = address.getId();
            JSONObject object = new JSONObject();
            try {
              object.put("game_id", game.getId());
              object.put("address_id", new_address_id);
              object.put("rent_type_id", rent_type_id);
            } catch (JSONException e) {
              e.printStackTrace();
            }

            service.rentWithWallet(object, new RentPayService.onRentWithWalletComplete() {
              @Override
              public void onComplete(int status, String message) {
                avl_wait.setVisibility(View.INVISIBLE);
                if(status == 0){
                  MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
                }else {
                  MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
                  restartApp();
                }
              }
            });

          }

        }
      });
    }
  }

  public void shopGameWithWallet() {
    if(sum_price > user_balance){
      MyViews.makeText(ActivityPurchase.this, "موجودی کیف پول شما کمتر میباشد. لطفا اقدام به شارژ کیف پول خود نمایید", Toast.LENGTH_LONG);
      return;
    }


    final ShopPayService service = new ShopPayService(ActivityPurchase.this);
    if(is_use_last_address){
      JSONObject object = new JSONObject();
      try {
        object.put("game_id", game.getId());
        object.put("address_id", last_address.getId());
      } catch (JSONException e) {
        e.printStackTrace();
      }
      service.shopWithWallet(object, new ShopPayService.onShopWithWalletComplete() {
        @Override
        public void onComplete(int status, String message) {
          if(status == 0){
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
            restartApp();
          }
        }
      });

    }

    else {
      if(getNewAddressObject() == null) return;
      AddressService addressService = new AddressService(ActivityPurchase.this);
      addressService.saveAddress(getNewAddressObject(), new AddressService.onAddressSaved() {
        @Override
        public void onComplete(int status, String message, Address address) {
          if (status == 0) {
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            new_address_id = address.getId();
            JSONObject object = new JSONObject();
            try {
              object.put("game_id", game.getId());
              object.put("address_id", new_address_id);
              object.put("rent_type_id", rent_type_id);
            } catch (JSONException e) {
              e.printStackTrace();
            }

            service.shopWithWallet(object, new ShopPayService.onShopWithWalletComplete() {
              @Override
              public void onComplete(int status, String message) {
                if(status == 0){
                  MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
                }else {
                  MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
                  restartApp();
                }
              }
            });

          }

        }
      });
    }
  }




  //bank

  public void rentGameWithBank(){
    final RentPayService service = new RentPayService(ActivityPurchase.this);
    if (is_use_last_address){
      JSONObject object = new JSONObject();
      try {
        object.put("game_id", game.getId());
        object.put("address_id", last_address.getId());
        object.put("rent_type_id", rent_type_id);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      service.rentWithBank(object, new RentPayService.onRentWithBankComplete() {
        @Override
        public void onComplete(int status, String message, String pay_url) {
          if(status == 0){
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            openBrowser(pay_url);
          }
        }
      });

    }
    else {
      if(getNewAddressObject() == null) return;
      AddressService addressService = new AddressService(ActivityPurchase.this);
      addressService.saveAddress(getNewAddressObject(), new AddressService.onAddressSaved() {
        @Override
        public void onComplete(int status, String message, Address address) {
          JSONObject object = new JSONObject();
          try {
            object.put("game_id", game.getId());
            object.put("address_id", last_address.getId());
            object.put("rent_type_id", rent_type_id);
          } catch (JSONException e) {
            e.printStackTrace();
          }
          service.rentWithBank(object, new RentPayService.onRentWithBankComplete() {
            @Override
            public void onComplete(int status, String message, String pay_url) {
              if(status == 0){
                MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
              }else {
                openBrowser(pay_url);
              }
            }
          });
        }
      });
    }
  }



  public void shopGameWithBank(){
    final ShopPayService service = new ShopPayService(ActivityPurchase.this);
    if(is_use_last_address){
      JSONObject object = new JSONObject();
      try {
        object.put("game_id", game.getId());
        object.put("address_id", last_address.getId());
      } catch (JSONException e) {
        e.printStackTrace();
      }
      service.shopWithBank(object, new ShopPayService.onShopWithBankComplete() {
        @Override
        public void onComplete(int status, String message, String pay_url) {
          avl_wait.setVisibility(View.INVISIBLE);
          if(status == 0){
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            openBrowser(pay_url);
          }
        }
      });

    }else {
      if(getNewAddressObject() == null) return;
      AddressService addressService = new AddressService(ActivityPurchase.this);
      addressService.saveAddress(getNewAddressObject(), new AddressService.onAddressSaved() {
        @Override
        public void onComplete(int status, String message, Address address) {
          if (status == 0) {
            MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
          }else {
            new_address_id = address.getId();
            JSONObject object = new JSONObject();
            try {
              object.put("game_id", game.getId());
              object.put("address_id", new_address_id);
            } catch (JSONException e) {
              e.printStackTrace();
            }

            service.shopWithBank(object, new ShopPayService.onShopWithBankComplete() {
              @Override
              public void onComplete(int status, String message, String pay_url) {
                avl_wait.setVisibility(View.INVISIBLE);
                if(status == 0){
                  MyViews.makeText(ActivityPurchase.this, message, Toast.LENGTH_SHORT);
                }else {
                  openBrowser(pay_url);
                }
              }
            });

          }

        }
      });
    }
  }







private JSONObject getNewAddressObject(){
  if(!checkEntry()) return null;
  String content = edt_address_content.getText().toString();
  String phone = edt_phone_number.getText().toString();
  String post_code = edt_postcode.getText().toString();
  if(post_code .length() < 1) post_code = "";

  JSONObject object = new JSONObject();
  try {
    object.put("state_id", state_id);
    object.put("city_id", city_id);
    object.put("post_code", post_code);
    object.put("home_phone_number", phone);
    object.put("content", content);
    object.put("latitude", 0);
    object.put("longitude", 0);
  } catch (JSONException e) {
    e.printStackTrace();
  }
  return object;
}







  private boolean checkEntry(){
    String address = edt_address_content.getText().toString();
    String phone = edt_phone_number.getText().toString();
    String post_code = edt_postcode.getText().toString();
    if(address.length() < 2){
      MyViews.makeText(ActivityPurchase.this, "آدرس وارد شده بسیار کوتاه میباشد", Toast.LENGTH_SHORT);
      return false;
    }
    else if(address.length() > 1990){
      MyViews.makeText(ActivityPurchase.this, "آدرس وارد شده بسیار طولانی میباشد", Toast.LENGTH_SHORT);
      return false;
    }
    else if(phone.length() < 9 || phone.length() > 14){
      MyViews.makeText(ActivityPurchase.this, "شماره تلفن وارد شده صحیح نمیباشد", Toast.LENGTH_SHORT);
      return false;
    }

    return true;
  }

  private void setupViews(){
    avl_wait = findViewById(R.id.avl_wait);
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
    lyt_rent_price = findViewById(R.id.lyt_rent_price);
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



  private void restartApp(){
    if(ActivityMenu.getInstance() != null) {
      ActivityMenu.getInstance().finish();
      Intent intent = new Intent(ActivityPurchase.this, ActivityMenu.class);
      startActivity(intent);
      ActivityPurchase.this.finish();
    }
  }

  private void openBrowser(String url){
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(browserIntent);
  }
}

package sabalan.paydar.mohtasham.ezibazi.controller.api_service;

import sabalan.paydar.mohtasham.ezibazi.controller.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.controller.system.config.AppConfig;

public class Urls {
  //-------Main Menu-----//
  //slider
  public static final String MAIN_SLIDER = AppConfig.MAIN_URL + "/slider";
  //posts
  public static final String POST_INDEX = AppConfig.MAIN_URL + "/post";
  public static final String POST_SEARCH = AppConfig.MAIN_URL + "/post-search";

  //games
  public static final String SHOP_INDEX = AppConfig.MAIN_URL + "/game-for-shop";
  public static final String SHOP_INDEX0 = AppConfig.MAIN_URL + "/game-for-shop-index";
  public static final String SHOP_SEARCH = AppConfig.MAIN_URL + "/game-for-shop-search";

  public static final String RENT_INDEX = AppConfig.MAIN_URL + "/game-for-rent";
  public static final String RENT_INDEX0 = AppConfig.MAIN_URL + "/game-for-rent-index";
  public static final String RENT_SEARCH = AppConfig.MAIN_URL + "/game-for-rent-search";
  public static final String RENT_TYPE_INDEX = AppConfig.MAIN_URL + "/rent-type";

  public static final String GAME_INFO_COMMENTS = AppConfig.MAIN_URL + "/game-info-comments";

  public static final String GAME_RELATED_GAME_FOR_RENT = AppConfig.MAIN_URL + "/game-for-rent-related";
  public static final String GAME_RELATED_GAME_FOR_SHOP = AppConfig.MAIN_URL + "/game-for-shop-related";
  public static final String GAME_RELATED_POSTS = AppConfig.MAIN_URL + "/game-info-related-posts";



  //user requests
  public static final String USER_RENT_REQUESTS = AppConfig.MAIN_URL + "/user-game-for-rent-requests";
  public static final String USER_BUY_REQUESTS = AppConfig.MAIN_URL + "/user-game-for-shop-requests";

  //======Main Menu=====//


  //---passport service---//

  public static final String LOGIN = AppConfig.MAIN_URL + "/login";
  public static final String REGISTER = AppConfig.MAIN_URL + "/register";
  public static final String USER = AppConfig.MAIN_URL + "/user";
  public static final String LOGOUT = AppConfig.MAIN_URL + "/logout";
  public static final String LOGIN_CHECK = AppConfig.MAIN_URL + "/login-check";

  //===passport service===//


  //tickets
  public static final String TICKET_INDEX = AppConfig.MAIN_URL + "/ticket";
  public static final String TICKET_SEEN = AppConfig.MAIN_URL + "/ticket-user-seen";
  public static final String NEW_TICKETS_COUNT = AppConfig.MAIN_URL + "/new-tickets-count";

  //user finance
  public static final String USER_FINANCE = AppConfig.MAIN_URL + "/user-finance";

  //user last address
  public static final String USER_LAST_ADDRESS = AppConfig.MAIN_URL + "/user-last-address";

  //comment
  public static final String COMMENT_INDEX = AppConfig.MAIN_URL + "/comment";


  //fcm refresh token
  public static final String REFRESH_FCM_TOKEN = AppConfig.MAIN_URL + "/refresh-fcm-token";


  //finance
  public static final String INCREASE_CREDIT = AppConfig.MAIN_URL + "/increase-credit";


  //address
  public static final String ADDRESS_STATES = AppConfig.MAIN_URL + "/states";
  public static final String ADDRESS_STATE_CITIES = AppConfig.MAIN_URL + "/state/{id}/cities";
}

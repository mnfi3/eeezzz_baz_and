package mohtasham.paydar.sabalan.ezbazi.controller.api_service;

import mohtasham.paydar.sabalan.ezbazi.controller.system.G;

public class Urls {
  //-------Main Menu-----//
  //slider
  public static final String MAIN_SLIDER = G.MAIN_URL + "/slider";
  //posts
  public static final String POST_INDEX = G.MAIN_URL + "/post";
  public static final String POST_SEARCH = G.MAIN_URL + "/post-search";

  //games
  public static final String SHOP_INDEX = G.MAIN_URL + "/game-for-shop";
  public static final String SHOP_INDEX0 = G.MAIN_URL + "/game-for-shop-index/329";
  public static final String SHOP_SEARCH = G.MAIN_URL + "/game-for-shop-search";

  public static final String RENT_INDEX = G.MAIN_URL + "/game-for-rent";
  public static final String RENT_INDEX0 = G.MAIN_URL + "/game-for-rent-index/329";
  public static final String RENT_SEARCH = G.MAIN_URL + "/game-for-rent-search";

  public static final String GAME_INFO_COMMENTS = G.MAIN_URL + "/game-info-comments";

  public static final String GAME_RELATED_GAME_FOR_RENT = G.MAIN_URL + "/game-for-rent-related";
  public static final String GAME_RELATED_GAME_FOR_SHOP = G.MAIN_URL + "/game-for-shop-related";
  public static final String GAME_RELATED_POSTS = G.MAIN_URL + "/game-info-related-posts";



  //user requests
  public static final String USER_RENT_REQUESTS = G.MAIN_URL + "/user-game-for-rent-requests";
  public static final String USER_BUY_REQUESTS = G.MAIN_URL + "/user-game-for-shop-requests";

  //======Main Menu=====//


  //---passport service---//

  public static final String LOGIN = G.MAIN_URL + "/login";
  public static final String REGISTER = G.MAIN_URL + "/register";
  public static final String USER = G.MAIN_URL + "/user";
  public static final String LOGOUT = G.MAIN_URL + "/logout";
  public static final String LOGIN_CHECK = G.MAIN_URL + "/login-check";

  //===passport service===//




  //tickets
  public static final String TICKET_INDEX = G.MAIN_URL + "/ticket";
  public static final String TICKET_SEEN = G.MAIN_URL + "/ticket-user-seen";


}

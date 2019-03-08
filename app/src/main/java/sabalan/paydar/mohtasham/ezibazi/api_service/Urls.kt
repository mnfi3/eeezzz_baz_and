package sabalan.paydar.mohtasham.ezibazi.api_service

import sabalan.paydar.mohtasham.ezibazi.system.config.AppConfig

object Urls {
    //-------Main Menu-----//
    //slider
    val MAIN_SLIDER = AppConfig.MAIN_URL + "/slider"
    //posts
    val POST_INDEX = AppConfig.MAIN_URL + "/post"
    val POST_SEARCH = AppConfig.MAIN_URL + "/post-search"

    //games
    val SHOP_INDEX = AppConfig.MAIN_URL + "/game-for-shop"
    val SHOP_INDEX0 = AppConfig.MAIN_URL + "/game-for-shop-index"
    val SHOP_SEARCH = AppConfig.MAIN_URL + "/game-for-shop-search"

    val RENT_INDEX = AppConfig.MAIN_URL + "/game-for-rent"
    val RENT_INDEX0 = AppConfig.MAIN_URL + "/game-for-rent-index"
    val RENT_SEARCH = AppConfig.MAIN_URL + "/game-for-rent-search"
    val RENT_TYPE_INDEX = AppConfig.MAIN_URL + "/rent-type"

    val GAME_INFO_COMMENTS = AppConfig.MAIN_URL + "/game-info-comments"

    val GAME_RELATED_GAME_FOR_RENT = AppConfig.MAIN_URL + "/game-for-rent-related"
    val GAME_RELATED_GAME_FOR_SHOP = AppConfig.MAIN_URL + "/game-for-shop-related"
    val GAME_RELATED_POSTS = AppConfig.MAIN_URL + "/game-info-related-posts"


    //user requests
    val USER_RENT_REQUESTS = AppConfig.MAIN_URL + "/user-game-for-rent-requests"
    val USER_BUY_REQUESTS = AppConfig.MAIN_URL + "/user-game-for-shop-requests"

    //======Main Menu=====//


    //---passport service---//

    val LOGIN = AppConfig.MAIN_URL + "/login"
    val REGISTER = AppConfig.MAIN_URL + "/register"
    val USER = AppConfig.MAIN_URL + "/user"
    val LOGOUT = AppConfig.MAIN_URL + "/logout"
    val LOGIN_CHECK = AppConfig.MAIN_URL + "/login-check"

    //password reset
    val PASSWORD_RESET = "/password/reset"


    //===passport service===//


    //tickets
    val TICKET_INDEX = AppConfig.MAIN_URL + "/ticket"
    val TICKET_SEEN = AppConfig.MAIN_URL + "/ticket-user-seen"
    val NEW_TICKETS_COUNT = AppConfig.MAIN_URL + "/new-tickets-count"

    //user finance
    val USER_FINANCE = AppConfig.MAIN_URL + "/user-finance"

    //user last address
    val USER_LAST_ADDRESS = AppConfig.MAIN_URL + "/user-last-address"

    //comment
    val COMMENT_INDEX = AppConfig.MAIN_URL + "/comment"


    //fcm refresh token
    val REFRESH_FCM_TOKEN = AppConfig.MAIN_URL + "/refresh-fcm-token"


    //finance
    val INCREASE_CREDIT = AppConfig.MAIN_URL + "/increase-credit"


    //address
    val ADDRESS_INDEX = AppConfig.MAIN_URL + "/address"
    val ADDRESS_STATES = AppConfig.MAIN_URL + "/states"
    val ADDRESS_STATE_CITIES = AppConfig.MAIN_URL + "/state/{id}/cities"

    //game payments
    val RENT_WITH_WALLET = AppConfig.MAIN_URL + "/rent-game-with-wallet"
    val RENT_WITH_BANK = AppConfig.MAIN_URL + "/rent-game"

    val SHOP_WITH_WALLET = AppConfig.MAIN_URL + "/shop-game-with-wallet"
    val SHOP_WITH_BANK = AppConfig.MAIN_URL + "/shop-game"








    //osm
    val OSM_SEARCH = "https://nominatim.openstreetmap.org/search" +
            "?format=json" +
            "&accept-language={language}" +
            "&country={country}" +
            "&sate={state}" +
            "&city={city}" +
            "&street={street}" +
            "&limit={limit}" +
            "&addressdetails={addressdetails}" +
            "&q={query}"


    val OSM_REVERSE = "https://nominatim.openstreetmap.org/reverse" +
            "?format=json" +
            "&accept-language=persian" +
            "&lat={lat}" +
            "&lon={lon}"
}

package sabalan.paydar.mohtasham.ezibazi.system.config

object AppConfig {

    //  public static final String MAIN_URL="http://192.168.30.55/izi-bazi.ud/api";
    //  public static final String MAIN_URL="http://5.196.102.75/~ittiktak/public/api";
    //  public static final String MAIN_URL="http://localhost/izi-bazi.ud/api";
    //  public static final String MAIN_URL="http://ezibazi.com/webservice/public/api";
    //  public static final String MAIN_URL="http://ittiktak.com/ezibazi/public/api";
    val MAIN_URL = "http://192.168.30.55/izi-bazi.ud/api"


    val DEFAULT_CITY_ID = 14//TABRIZ
    val NEW_TICKETS_CHECK_TIME_MS = 10000
    val TIME_GET_USER_FINANCE_REFRESH_MS = 10000

    val DEFAULT_VOLLEY_TIME_OUT_MILLIS = 12000
    val DEFAULT_VOLLEY_RETRY_COUNT = 3
}

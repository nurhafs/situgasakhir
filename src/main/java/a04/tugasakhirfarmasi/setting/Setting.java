package a04.tugasakhirfarmasi.setting;

public class Setting {
    final public static String CLIENT_BASE_URL = "http://tugasakhir-farmasi.herokuapp.com";
    final public static String CLIENT_LOGIN = CLIENT_BASE_URL + "/validate-ticket";
    final public static String CLIENT_LOGOUT = CLIENT_BASE_URL + "/log-out";

    final public static String SERVER_BASE_URL = "https://sso.ui.ac.id";
    final public static String SERVER_LOGIN = SERVER_BASE_URL + "/cas2/login?service=";
    final public static String SERVER_LOGOUT = SERVER_BASE_URL + "/cas2/logout?url=";
    final public static String SERVER_VALIDATE_TICKET = SERVER_BASE_URL + "/cas2/serviceValidate?ticket=%s&service=%s";

    final public static String CURRENT_PERIODE = "Genap 2021/2022";
}
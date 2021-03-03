package preproject.client;

public class Action {
    public static final String LOGIN_USER = "LOGIN_USER";
    public static final String LOGOUT_USER = "LOGOUT_USER";
    public static final String REGISTER_USER = "REGISTER_USER";
    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String ADD_FAVOURITE = "ADD_FAVOURITE";
    public static final String ADD_GROUP = "ADD_GROUP";
    public static final String ADD_GROUP_NEW_MEMBER = "ADD_GROUP_NEW_MEMBER";
    public static final String SEND_BROADCAST_MESSAGE = "SEND_BROADCAST_MESSAGE";

    // GET REQUESTS
    public static final String GET_GROUP_LIST = "GET_GROUP_LIST";
    public static final String GET_ONLINEUSERS_LIST = "GET_ONLINEUSERS_LIST";
    public static final String GET_VERIFIED_USERS = "GET_VERIFIED_USERS";
    public static final String GET_UNVERIFIED_USERS = "GET_UNVERIFIED_USERS";
    public static final String GET_USER_INFORMATION = "GET_USER_INFORMATION";
    public static final String GET_GROUP_MEMBERS = "GET_GROUP_MEMBERS";
    public static final String GET_GROUP_MESSAGES = "GET_GROUP_MESSAGES";
    public static final String GET_UNREAD_MESSAGES = "GET_UNREAD_MESSAGES";

    // POST REQUESTS
    public static final String POST_VERIFIED_USERS = "POST_VERIFIED_USERS";
    public static final String ACCEPT_ALL_USERS = "ACCEPT_ALL_USERS";
    public static final String DECLINE_ALL_USERS = "DECLINE_ALL_USERS";

    // ON SERVER SEND
    public static final String ON_GROUP_LIST_SEND = "ON_GROUP_LIST_SEND";
    public static final String ON_USER_INFO_SEND = "ON_USER_INFO_SEND";
    public static final String ON_MESSAGE_RECEIVE = "ON_MESSAGE_RECEIVE";
    public static final String ON_GROUP_CREATION = "ON_GROUP_CREATION";
    public static final String ON_INITIAL_MESSAGES_RECEIVED = "ON_INITIAL_MESSAGES_RECEIVED";
    public static final String ON_FAVORITE_TOGGLED = "ON_FAVORITE_TOGGLED";
    public static final String ON_NEW_GROUP_CREATION = "ON_NEW_GROUP_CREATION";
    public static final String ON_BROADCAST_MESSAGE_RECEIVED ="ON_BROADCAST_MESSAGE_RECEIVED";


}

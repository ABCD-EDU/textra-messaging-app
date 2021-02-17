package preproject.frontend;

import java.io.Serializable;

public class Action implements Serializable {
    public static final String LOGIN_USER = "LOGIN_USER";
    public static final String LOGOUT_USER = "LOGOUT_USER";
    public static final String REGISTER_USER = "REGISTER_USER";
    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String ADD_FAVOURITE = "ADD_FAVOURITE";
    public static final String ADD_GROUP = "ADD_GROUP";
    public static final String ADD_GROUP_NEW_MEMBER = "ADD_GROUP_NEW_MEMBER";

    // GET REQUESTS
    public static final String GET_GROUP_LIST = "GET_GROUP_LIST";
    public static final String GET_MESSAGE_LIST = "GET_MESSAGE_LIST";
    public static final String GET_NONVERIFIED_STUDENT_LIST = "GET_NONVERIFIED_STUDENT_LIST";
    public static final String GET_ONLINEUSERS_LIST = "GET_ONLINEUSERS_LIST";
}

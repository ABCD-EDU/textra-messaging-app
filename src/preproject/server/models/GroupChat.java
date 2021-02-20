package preproject.server.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class creates an instance of a {@link GroupChat} that consists of 2 or more users. Each chat group will have
 * their own unique id. In order to use the data effectively, collect all the users and their messages, then print out
 * all of the messages from each users based on their timestamp.
 *
 * An attempt for this is to put all of the messages from each user then sort it based on their timestamp. Oldest
 * message will be printed out first, then the most recent will be printed last.
 *
 * In oder to save space for the window, find a way to print out the first N amount of messages first, then load N more
 * messages if needed (through scrolling).
 *
 * NOTE: No group chat shall have the same ID
 */
public class GroupChat {
    private Map<String, List<User>> groupMap; // where String = id

    public GroupChat() {
        groupMap = new HashMap<>();
    }

    public GroupChat(String id, List<User> members) {
        groupMap = new HashMap<>();
        groupMap.put(id, members);
    }

    public Map<String, List<User>> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, List<User>> groupMap) {
        this.groupMap = groupMap;
    }
}

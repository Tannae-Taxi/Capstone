package Database;

import java.util.HashMap;

// << User Database >>
public class User {
    // < Attiributes >
    HashMap<String, String> userDataMap = new HashMap<>();
    // usn, vsn                         = Key data
    // id, pw                           = Account data
    // uname, birth, sex, email, phone  = Personal data
    // type, point, score               = Service data

    // < Constructor >
    public User() {}
    
    // < Method >
    public String getData(String key) { return userDataMap.get(key); }
    public void setData(String key, String value) { userDataMap.put(key, value); }
}
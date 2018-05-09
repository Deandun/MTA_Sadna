package Logic;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

public class DataBase {
    private DatabaseReference mDatabase;

    public DataBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void updatingUserDetails(Map<String, Object> userDetails)
    {
        String key = mDatabase.child("Users").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, userDetails);
        childUpdates.put("/user-posts/" + userDetails.containsKey("id") + "/" + key, userDetails);

        mDatabase.updateChildren(childUpdates);
    }


}

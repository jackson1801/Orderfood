package fpt.prm.orderfood.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private FirebaseDatabase database;
    DatabaseReference myRef;

    public FirebaseUtils() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
}

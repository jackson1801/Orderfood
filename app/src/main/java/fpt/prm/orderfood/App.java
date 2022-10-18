package fpt.prm.orderfood;

public class App extends android.app.Application {
    private static App mInstance;
    private static com.google.gson.Gson mGson;

    public static App self() {
        return mInstance;
    }

    public static com.google.gson.Gson getGSon() {
        return mGson;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mGson = new com.google.gson.Gson();

    }

}

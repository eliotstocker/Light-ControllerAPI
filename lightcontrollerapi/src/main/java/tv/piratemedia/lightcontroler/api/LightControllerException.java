package tv.piratemedia.lightcontroler.api;

/**
 * Created by eliotstocker on 31/01/15.
 */
public class LightControllerException extends Exception {
    public static final int TYPE_APPLICATION_MISSING = 404;
    public static final int TYPE_APPLICATION_OLD = 417;
    public static final int TYPE_ZONE_TYPE_INCORRECT = 412;
    public static final int TYPE_VALUE_OUT_OF_RANGE = 211;

    private int _type = 0;

    public LightControllerException(int type) {
        _type = type;
    }

    public int getCode() {
        return _type;
    }
}

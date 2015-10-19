package objects;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by sajid on 2/15/2015.
 */
public class MainActivityList {

    Bitmap _bitMap;
    String _name;
    Activity context;

    public Bitmap get_bitMap() {
        return _bitMap;
    }

    public void set_bitMap(Bitmap _bitMap) {
        this._bitMap = _bitMap;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }




    public MainActivityList()

    {

    }

    public MainActivityList(Activity context, Bitmap _bitMap,String _name)
    {
        this._bitMap = _bitMap;
        this.context = context;
        this._name = _name;
    }


}

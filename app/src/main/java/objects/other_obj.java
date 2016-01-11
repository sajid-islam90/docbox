package objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajid on 8/23/2015.
 */
public class other_obj implements Parcelable{

    int _pid;

    public other_obj(Parcel in) {
        _pid = in.readInt();
        _date = in.readString();
        _version = in.readInt();
        _field_name = in.readString();
        _field_value = in.readString();
    }

    public static final Creator<other_obj> CREATOR = new Creator<other_obj>() {
        @Override
        public other_obj createFromParcel(Parcel in) {
            return new other_obj(in);
        }

        @Override
        public other_obj[] newArray(int size) {
            return new other_obj[size];
        }
    };

    public other_obj() {

    }

    public int get_pid() {
        return _pid;
    }

    public void set_pid(int _pid) {
        this._pid = _pid;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public int get_version() {
        return _version;
    }

    public void set_version(int _version) {
        this._version = _version;
    }

    public String get_field_name() {
        return _field_name;
    }

    public void set_field_name(String _field_name) {
        this._field_name = _field_name;
    }

    public String get_field_value() {
        return _field_value;
    }

    public void set_field_value(String _field_value) {
        this._field_value = _field_value;
    }

    String _date;
    int _version;
    String _field_name;
    String _field_value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_pid);
        dest.writeString(_date);
        dest.writeInt(_version);
        dest.writeString(_field_name);
        dest.writeString(_field_value);
    }
}

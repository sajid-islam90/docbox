package objects;
//OBJECT TO HOLD HISTORY NOTES DATA OF A PATIENT

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajid on 6/24/2015.
 */
public class history_obj implements Parcelable{

    String _present_illness;
    String _past_illness;

    public history_obj(Parcel in) {
        _present_illness = in.readString();
        _past_illness = in.readString();
        _version = in.readInt();
        _pid = in.readInt();
        _date = in.readString();
        _personal_hist = in.readString();
        _family_hist = in.readString();
    }

    public static final Creator<history_obj> CREATOR = new Creator<history_obj>() {
        @Override
        public history_obj createFromParcel(Parcel in) {
            return new history_obj(in);
        }

        @Override
        public history_obj[] newArray(int size) {
            return new history_obj[size];
        }
    };

    public history_obj() {

    }

    public int get_version() {
        return _version;
    }

    public void set_version(int _version) {
        this._version = _version;
    }

    int _version;

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

    int _pid;
   public String _date;

    public String get_present_illness() {
        return _present_illness;
    }

    public void set_present_illness(String _present_illness) {
        this._present_illness = _present_illness;
    }

    public String get_past_illness() {
        return _past_illness;
    }

    public void set_past_illness(String _past_illness) {
        this._past_illness = _past_illness;
    }

    public String get_personal_hist() {
        return _personal_hist;
    }

    public void set_personal_hist(String _personal_hist) {
        this._personal_hist = _personal_hist;
    }

    public String get_family_hist() {
        return _family_hist;
    }

    public void set_family_hist(String _family_hist) {
        this._family_hist = _family_hist;
    }

    String _personal_hist;
    String _family_hist;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_present_illness);
        dest.writeString(_past_illness);
        dest.writeInt(_version);
        dest.writeInt(_pid);
        dest.writeString(_date);
        dest.writeString(_personal_hist);
        dest.writeString(_family_hist);
    }
}

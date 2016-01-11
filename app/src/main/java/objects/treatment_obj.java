package objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajid on 8/22/2015.
 */
public class treatment_obj implements Parcelable{


    String _diagnosis;

    public treatment_obj(Parcel in) {
        _diagnosis = in.readString();
        _treatment = in.readString();
        _procedure = in.readString();
        _implants = in.readString();
        _pid = in.readInt();
        _date = in.readString();
        _version = in.readInt();
    }

    public static final Creator<treatment_obj> CREATOR = new Creator<treatment_obj>() {
        @Override
        public treatment_obj createFromParcel(Parcel in) {
            return new treatment_obj(in);
        }

        @Override
        public treatment_obj[] newArray(int size) {
            return new treatment_obj[size];
        }
    };

    public treatment_obj() {

    }

    public String get_diagnosis() {
        return _diagnosis;
    }

    public void set_diagnosis(String _diagnosis) {
        this._diagnosis = _diagnosis;
    }

    public String get_treatment() {
        return _treatment;
    }

    public void set_treatment(String _treatment) {
        this._treatment = _treatment;
    }

    public String get_procedure() {
        return _procedure;
    }

    public void set_procedure(String _procedure) {
        this._procedure = _procedure;
    }

    public String get_implants() {
        return _implants;
    }

    public void set_implants(String _implants) {
        this._implants = _implants;
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

    String _treatment;
    String _procedure;
    String _implants;
    int _pid;
    public String _date;
    int _version;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_diagnosis);
        dest.writeString(_treatment);
        dest.writeString(_procedure);
        dest.writeString(_implants);
        dest.writeInt(_pid);
        dest.writeString(_date);
        dest.writeInt(_version);
    }
}

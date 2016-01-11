package objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajid on 6/27/2015.
 */
public class exam_obj implements Parcelable {


    int _pid;

    public exam_obj(Parcel in) {
        _pid = in.readInt();
        _date = in.readString();
        _version = in.readInt();
        _gen_exam = in.readString();
        _local_exam = in.readString();
    }

    public static final Creator<exam_obj> CREATOR = new Creator<exam_obj>() {
        @Override
        public exam_obj createFromParcel(Parcel in) {
            return new exam_obj(in);
        }

        @Override
        public exam_obj[] newArray(int size) {
            return new exam_obj[size];
        }
    };

    public exam_obj() {

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

    public String get_gen_exam() {
        return _gen_exam;
    }

    public void set_gen_exam(String _gen_exam) {
        this._gen_exam = _gen_exam;
    }

    public String get_local_exam() {
        return _local_exam;
    }

    public void set_local_exam(String _local_exam) {
        this._local_exam = _local_exam;
    }

    public String _date;
    int _version;
    String _gen_exam;
    String _local_exam;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_pid);
        dest.writeString(_date);
        dest.writeInt(_version);
        dest.writeString(_gen_exam);
        dest.writeString(_local_exam);
    }
}

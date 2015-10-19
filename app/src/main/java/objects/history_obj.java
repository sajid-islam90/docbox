package objects;
//OBJECT TO HOLD HISTORY NOTES DATA OF A PATIENT
/**
 * Created by sajid on 6/24/2015.
 */
public class history_obj {

    String _present_illness;
    String _past_illness;

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
    String _date;

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
}

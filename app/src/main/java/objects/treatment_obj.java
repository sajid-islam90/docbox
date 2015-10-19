package objects;

/**
 * Created by sajid on 8/22/2015.
 */
public class treatment_obj {


    String _diagnosis;

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
    String _date;
    int _version;
}

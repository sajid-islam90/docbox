package objects;

/**
 * Created by sajid on 8/23/2015.
 */
public class other_obj {

    int _pid;

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

}

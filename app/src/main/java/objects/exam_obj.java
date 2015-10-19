package objects;

/**
 * Created by sajid on 6/27/2015.
 */
public class exam_obj {


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

    String _date;
    int _version;
    String _gen_exam;
    String _local_exam;
}

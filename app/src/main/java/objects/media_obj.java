package objects;

/**
 * Created by sajid on 8/28/2015.
 */
public class media_obj {
    int _pid;

    public int get_pid() {
        return _pid;
    }

    public void set_pid(int _pid) {
        this._pid = _pid;
    }

    public String get_media_name() {
        return _media_name;
    }

    public void set_media_name(String _media_name) {
        this._media_name = _media_name;
    }

    public String get_media_path() {
        return _media_path;
    }

    public void set_media_path(String _media_path) {
        this._media_path = _media_path;
    }

    public byte[] get_bmp() {
        return _bmp;
    }

    public void set_bmp(byte[] _bmp) {
        this._bmp = _bmp;
    }

    public int get_section() {
        return _section;
    }

    public void set_section(int _section) {
        this._section = _section;
    }

    public int get_version() {
        return _version;
    }

    public void set_version(int _version) {
        this._version = _version;
    }

    String _media_name;
    String _media_path;
    byte[] _bmp;
    int _section;
    int _version;

}

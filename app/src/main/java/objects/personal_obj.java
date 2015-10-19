package objects;

/**
 * Created by sajid on 9/11/2015.
 */
public class personal_obj {

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public int get_customerId() {
        return _customerId;
    }

    public void set_customerId(int _customerId) {
        this._customerId = _customerId;
    }

    public String get_photoPath() {
        return _photoPath;
    }

    public void set_photoPath(String _photoPath) {
        this._photoPath = _photoPath;
    }

    String _name,
           _email,
            _password,
            _phone;

    int _customerId;

    String _photoPath;

}

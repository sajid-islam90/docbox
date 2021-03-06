package objects;
// OBJECT TO HOLD PATIENT PERSONAL DATA
import android.graphics.Bitmap;

/**
 * Created by sajid on 2/12/2015.
 */
public class Patient {
    int _id;
    String _name;

    public String get_weight() {
        return _weight;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }

    String _weight;

    public String get_opd_ipd() {
        return _opd_ipd;
    }

    public void set_opd_ipd(String _opd_ipd) {
        this._opd_ipd = _opd_ipd;
    }

    String _opd_ipd;

    public int get_first_aid_id() {
        return _first_aid_id;
    }

    public void set_first_aid_id(int _first_aid_id) {
        this._first_aid_id = _first_aid_id;
    }

    int _first_aid_id;
    public String get_last_seen_date() {
        return _last_seen_date;
    }

    public void set_last_seen_date(String _last_seen_date) {
        this._last_seen_date = _last_seen_date;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_contact_number() {
        return _contact_number;
    }

    public void set_contact_number(String _contact_number) {
        this._contact_number = _contact_number;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_ocupation() {
        return _ocupation;
    }

    public void set_ocupation(String _ocupation) {
        this._ocupation = _ocupation;
    }

    String _email;
    String _contact_number;
    String _address;
    String _ocupation;
    String _last_seen_date;

    public String get_next_follow_up_date() {
        return _next_follow_up_date;
    }

    public void set_next_follow_up_date(String _next_follow_up_date) {
        this._next_follow_up_date = _next_follow_up_date;
    }

    String _next_follow_up_date;
    String _age;
    String _gender;
    String _height;

    public String get_photoPath() {
        return _photoPath;
    }

    public void set_photoPath(String _photoPath) {
        this._photoPath = _photoPath;
    }

    String _photoPath;
    public String get_diagnosis() {
        return this._diagnosis;
    }

    public void set_diagnosis(String _diagnosis) {
        this._diagnosis = _diagnosis;
    }

    String _diagnosis;
    byte[] _bmp;


    public byte[] get_bmp() {
        return _bmp;
    }

    public void set_bmp(byte[] _bmp) {
        this._bmp = _bmp;
    }




    public Patient()
    {
        this._id = 0;
        this._age = "0";
        this._gender = "m";
        this._height = "0";
        this._name = "";
        this._diagnosis = "";
        this._bmp = new byte[0];
        this._email = "";
        this._contact_number= "";
        this. _address = "";
        this._ocupation = "";
        this._last_seen_date = "";
        this._first_aid_id = 0;
        this._weight ="0";
        this._opd_ipd ="";
    }

    public  Patient(int _id,String _name,String _age,String _gender ,String _height,byte[] _bmp,String _diagnosis )
    {
        this._age = _age;
        this._gender = _gender;
        this._first_aid_id = 0;
        this._height = _height;
        this._name = _name;
        this._id = _id;
        this._diagnosis = _diagnosis;
        if(_bmp!=null)
        this._bmp = _bmp;
        else
            this._bmp = new byte[0];

    }

    public  Patient(String _name,String _age,String _gender ,String _height,byte[] _bmp,String _diagnosis )
    {
        this._age = _age;
        this._gender = _gender;
        this._height = _height;
        this._name = _name;
        this._first_aid_id = 0;
        this._diagnosis = _diagnosis;
        if(_bmp!=null)
            this._bmp = _bmp;
        else
            this._bmp = new byte[0];


    }

    public String get_height() {
        return this._height;
    }

    public void set_height(String _height) {
        this._height = _height;
    }

    public String get_gender() {
        return this._gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public String get_age() {
        return this._age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_name() {
        return this._name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }




}

package objects;
//OBJECT TO HOLD DOCUMENTS (OPD , IPD)
/**
 * Created by sajid on 3/26/2015.
 */
public class document_obj {

    int _id;

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    String _date;
    String _doc_name;
    String _doc_path;
    byte[] _bmp;

    public String get_doc_name() {
        return _doc_name;
    }

    public void set_doc_name(String _doc_name) {
        this._doc_name = _doc_name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_doc_path() {
        return _doc_path;
    }

    public void set_doc_path(String _doc_path) {
        this._doc_path = _doc_path;
    }

    public byte[] get_bmp() {
        return _bmp;
    }

    public void set_bmp(byte[] _bmp) {
        this._bmp = _bmp;
    }

    public  document_obj(int id,String docName,String docPath,byte[] bmp)
    {
        this._id = id;
        this._doc_name = docName;
        this._doc_path = docPath;
        this._bmp = bmp;
    }
    public document_obj()
    {

    }

}

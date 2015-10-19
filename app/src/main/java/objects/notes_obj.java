package objects;

/**
 * Created by sajid on 4/16/2015.
 */
public class notes_obj {
    String _date;
    int _id;
    int _version;
    String _complaint;
    String _hist_present_illness;
    String _past_hist;
    String _personal_hist;
    String _family_hist;
    String _gen_exam;
    String _loc_exam;

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_version() {
        return _version;
    }

    public void set_version(int _version) {
        this._version = _version;
    }

    public String get_complaint() {
        return _complaint;
    }

    public void set_complaint(String _complaint) {
        this._complaint = _complaint;
    }

    public String get_hist_present_illness() {
        return _hist_present_illness;
    }

    public void set_hist_present_illness(String _hist_present_illness) {
        this._hist_present_illness = _hist_present_illness;
    }

    public String get_past_hist() {
        return _past_hist;
    }

    public void set_past_hist(String _past_hist) {
        this._past_hist = _past_hist;
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

    public String get_gen_exam() {
        return _gen_exam;
    }

    public void set_gen_exam(String _gen_exam) {
        this._gen_exam = _gen_exam;
    }

    public String get_loc_exam() {
        return _loc_exam;
    }

    public void set_loc_exam(String _loc_exam) {
        this._loc_exam = _loc_exam;
    }

    public byte[] get_clinical() {
        return _clinical;
    }

    public void set_clinical(byte[] _clinical) {
        this._clinical = _clinical;
    }

    public byte[] get_invest() {
        return _invest;
    }

    public void set_invest(byte[] _invest) {
        this._invest = _invest;
    }

    public String get_classification() {
        return _classification;
    }

    public void set_classification(String _classification) {
        this._classification = _classification;
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

    public String get_implant() {
        return _implant;
    }

    public void set_implant(String _implant) {
        this._implant = _implant;
    }

    public String get_score() {
        return _score;
    }

    public void set_score(String _score) {
        this._score = _score;
    }

    public String get_remark() {
        return _remark;
    }

    public void set_remark(String _remark) {
        this._remark = _remark;
    }

    byte[] _clinical;
    byte[] _invest;
    String _classification;
    String _diagnosis;
    String _treatment;
    String _procedure;
    String _implant;
    String _score;
    String _remark;

    public notes_obj()
    {

    }

    public notes_obj(String date,int id,int version,String complaint,String histPIllness,String pastHist,
                     String persHist,String familyHist,String genExam,String locExam,byte[] clinical,byte[] investigation,
                     String classification,String diagnosis,String treatment,String procedure,String implant,
                     String score,String remark)
    {
        this._classification = classification;
        this._clinical = clinical;
        this._complaint = complaint;
        this._date = date;
        this._diagnosis = diagnosis;
        this. _family_hist = familyHist;
        this._gen_exam = genExam;
        this._hist_present_illness = histPIllness;
        this._id = id;
        this. _implant = implant;
        this._invest = investigation;
        this._loc_exam = locExam;
        this._past_hist = pastHist;
        this._version = version;
        this._treatment = treatment;
        this._remark = remark;
        this._score = score;
        this._personal_hist = persHist;
        this._procedure = procedure;

    }
}

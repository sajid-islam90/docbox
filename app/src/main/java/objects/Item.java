package objects;

//OBJECT TO HOLD ALL PATIENT HOME PAGE DATA
import android.graphics.Bitmap;

public class Item {
    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    private int section;
    private int patient_id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    private String extra;

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    private String extra1;
    private String title;
    private String diagnosis;
    private Bitmap bmp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }



    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }



    public Item(String title, String diagnosis,Bitmap bmp) {
        super();
        this.title = title;
        this.diagnosis = diagnosis;
        this.bmp = bmp;
    }
    public Item()
    {

    }
    // getters and setters...
}
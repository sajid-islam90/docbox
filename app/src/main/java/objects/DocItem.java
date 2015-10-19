package objects;

import android.graphics.Bitmap;

/**
 * Created by sajid on 3/30/2015.
 */
public class DocItem {
    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    private Bitmap bmp;
    private String docName;

    public DocItem()
    {

    }
}

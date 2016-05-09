package objects;

/**
 * Created by sajid on 4/16/2016.
 */

import java.io.Serializable;



import java.io.Serializable;

/**
 * Created by sajid on 4/16/2016.
 */
public class bundle implements Serializable {



    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String filePath;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    int number;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public bundle(String filePath, int number, byte[] bytes) {
        this.filePath = filePath;
        this.number = number;
        this.bytes = bytes;
    }

    byte[] bytes;

    public bundle() {
        number = 0;
        filePath="";
        bytes = null;
    }

    public bundle(String filePath) {
        this.filePath = filePath;

    }
}

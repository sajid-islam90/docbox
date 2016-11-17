package activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import adapters.EDirectoryAdapter;
import objects.modelEDirectory;

/**
 * Created by romichandra on 13/11/16.
 */
public class AssociationEDirectoryActivity  extends AppCompatActivity {
    ArrayList<modelEDirectory> list = new ArrayList();
    ListView listViewEDirectory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_e_directory_association);

        getSupportActionBar().setTitle("E - Directory");

        listViewEDirectory = (ListView)findViewById(R.id.listEDirectory);

//        readExcelFile(this, "ophthadataold.xls");
        parseData p = new parseData(this);
        p.execute();
//        for (int i = 0; i < 1600; i++){
//            modelEDirectory model = new modelEDirectory();
//            model.setName("Dr. S. P. Singh");
//            model.setDesignation("Director Regional Inst. Of Ophthalmology, MLN Medical College");
//            model.setAddress("Allahabad");
//            model.setNumber1("9876543210");
//            model.setNumber2("9012345678");
//            model.setEmail("sample.email@email.com");
//
//            list.add(model);
//        }
//        listViewEDirectory.setAdapter(new EDirectoryAdapter(this, list));
    }

    private ArrayList<modelEDirectory> readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
//            Log.e("abcd", "Storage not available or read only");
            return null;
        }

        try{
            // Creating Input Stream
//            AssetManager assetManager = getAssets();
//            File file = new File(context.getExternalFilesDir(null), filename);
//            AssetFileDescriptor fileDescriptor = assetManager.openFd(filename);
//            FileInputStream stream = fileDescriptor.createInputStream();
//            FileInputStream myInput = new FileInputStream(file);

            InputStream is = context.getAssets().open(filename);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(is);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();
            int k = mySheet.getLastRowNum();
//            Log.d("abcd", "sheet rows: " +  k);

            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();

                String name = "" , namep1 = "", namep2 = "", namep3 = "", city = "", num1 = "", num2 = "", email = "";

                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
//                    Log.d("abcd", " row cells physical: " +  i);
//                    Log.d("abcd", " row cells total: " +  i);
//                    Log.d("abcd", "Cell Index: " +  myCell.getColumnIndex() + " Cell Value: " + myCell.toString());
                    switch (myCell.getColumnIndex()){
                        case 2:{
                            namep1 = myCell.toString() + " ";
                            break;
                        }
                        case 3:{
                            namep3 = myCell.toString() + " ";
                            break;
                        }
                        case 4:{
                            namep2 = myCell.toString();
                            break;
                        }
                        case 7:{
                            city = myCell.toString();
                            break;
                        }
                        case 10: {
                            num1 = myCell.toString();
                            if (num1.contains(".") && num1.contains("E9")){
                                num1 = num1.replace(".", "").replace("E9","");
                            }
                            break;
                        }
                        case 11: {
                            num2 = myCell.toString();
                            break;
                        }
                        case 12: {
                            email = myCell.toString();
                            if (email.contains("HYPERLINK")){
                                String[] parts = email.split(",");
                                email = parts[1];
                                email = email.substring(1, email.length()-3);
                            }
                            break;
                        }
                    }
                    if (!namep1.equals("") && !namep2.equals("") && !namep3.equals(""))
                    name = namep1 + namep2 + namep3;
                }

                if  (!name.equals("")){
                    modelEDirectory model = new modelEDirectory();
                    model.setName(name);
                    model.setDesignation(" ");
                    model.setAddress(city);
                    model.setNumber1(num1);
                    model.setNumber2(num2);
                    model.setEmail(email);

                    list.add(model);
                }
            }

        }catch (Exception e){e.printStackTrace(); }

        return list;
    }

    public boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private class parseData extends AsyncTask<String, String, String>{
        Context context;
        public parseData(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "parsing started", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context, "parsing stopped", Toast.LENGTH_SHORT).show();
            listViewEDirectory.setAdapter(new EDirectoryAdapter(context, list));
        }

        @Override
        protected String doInBackground(String... params) {
            readExcelFile(context, "ophthadataold.xls");

            return null;
        }
    }

}

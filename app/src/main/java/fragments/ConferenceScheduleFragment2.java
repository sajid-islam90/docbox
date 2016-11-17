package fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import activity.AssociationConferenceScheduleDetailsActivity;
import adapters.AssociationEventsAdapter;
import objects.modelAssociationEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConferenceScheduleFragment2 extends Fragment {
    ArrayList<modelAssociationEvent> listEvents = new ArrayList();

    String [] dates = new String [] {
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
            "19th Nov\n 2016",
    };

    String [] times = new String[] {
            "08 : 00 am\n to\n 10 : 00 am",
            "08 : 30 am\n to\n 09 : 30 am",
            "09 : 40 am\n to\n 11 : 10 am",
            "10 : 10 am\n to\n 11 : 40 am",
            "11 : 15 am\n to\n 12 : 00 pm",
            "11 : 50 am\n to\n 01 : 20 pm",
            "01 : 00 pm\n to\n 02 : 00 pm",
            "02 : 00 pm\n to\n 03 : 00 pm",
            "03 : 30 pm\n to\n 05 : 00 pm",
            "05 : 00 pm\n to\n 06 : 00 pm",
            "11 : 15 am\n to\n 12 : 00 pm",

    };

    String [] titles = new String [] {
            "(Session 1) - Retina Update",
            "(Session 11) - Free Paper 2",
            "(Session 12) - Cataract & Refractive",
            "(Session 15) - Glucoma Cornea Update (90 mins)",
            "(Session 13) - Oculoplasty",
            "(Session 10) - OPL",
            "LUNCH",
            "(Session 16) - M P Mehrey Oration Session",
            "Inaugration",
            "GBM",
            "GALA Dinner with sparkling musical night"

    };

    String [] locations = new String[]{
            "HALL A, Raj Eye Hospital, Gorakhpur",
            "HALL B, Raj Eye Hospital, Gorakhpur",
            "HALL B, Raj Eye Hospital, Gorakhpur",
            "HALL C, Raj Eye Hospital, Gorakhpur",
            "HALL B, Raj Eye Hospital, Gorakhpur",
            "HALL A, Raj Eye Hospital, Gorakhpur",
            "Raj Eye Hospital, Gorakhpur",
            "HALL C, Raj Eye Hospital, Gorakhpur",
            "Raj Eye Hospital, Gorakhpur",
            "",
            ""
    };

    String [] details = new String [] {
            "Chairperson: Dr Malay Chaturvedi, Dr.Ashish Mitra, Dr Durgesh Srivastava, Dr V K Tiwari, Dr Kirti Mittal, Dr.Mohit Khatri \n\n" +
                    "Panelists: Dr. Kumar guddu, Dr.Malay Jain, Dr. Pankaj Soni, Dr.Parag Agarwal, Dr.Rajat Kumar, Dr. Sanjay Thakur, Dr.Arvind Rai (1) Retinal detchment with PVR,Dr.Jayant Guha \n\n" +
                    "(2) Globe perforating injurie- a case report, Dr B.P.Kashyap\n" +
                    "(3) Individualising treatment of DME Key Note, Dr.Dinesh Talwar\n" +
                    "(4) Decreased vision after pediatric cataract srgery : It may be CVI, Dr.Amit Maitreya\n" +
                    "(5) Guidelines for prevention of Endophthalmitis, Dr.Satanshu Mathur\n" +
                    "(6) ROP,Dr.Neeraj Pandey\n" +
                    "(7) Electrophysiological testings,newer dimensions, Dr.Abdul Waris\n" +
                    "(8) Management of massive submacular haemorrhage, Dr.Shobhit Chawla\n" +
                    "(9) Anterior Vitrectomy: Devine help for all cataract surgeons, Dr.Navendu Rai\n" +
                    "(10) Managing Posterior segment Disorders on the basisof OCT, Dr.Charu Mithal\n" +
                    "(11) Acute post-operative endophthalmitis:what and when:management pearls, Dr.Abhishek Sharan\n" +
                    "(12) Management protocol in pachy choroid diseases, Dr.Avnindra Gupta",
            "Chairperson: Dr.D.P.Singh , Dr.Neelima Mehrotra , Dr.Kapil Agarwal , Dr.NK Singh (J), Dr.Mohit Khatri\n\n" +
                    "Time: 6 min each\n" +
                    "(1) Comparison of posterior capsular opacification in different types of IOL, Dr.Hemendra Singh \n" +
                    "(2) Treatment Modalities in management of CSR, Dr.Priyanka Jain\n" +
                    "(3) Pediatric VKC comparison between topical loteprednol etabonate0.5% and fluorometholone acetate o.1% in terms of efficacy and safety, Dr.Mayank k Shukla\n" +
                    "(4) Comparitive study of topical 1%voriconazole and 5% natamycin in fungal corneal ulcer, Dr.Jeevan Singh\n" +
                    "(5) To study visual outcome, endothelial cell loss and complications after Iris claw lens implantation., Dr.Arun Kumar Singh\n" +
                    "(6) Intraocular Cysticercosis, Dr.Manjusha Agrawal\n" +
                    "(7) Role of video game exercises in management of amblyopia, Dr.Mohtasham Tauheed\n" +
                    "(8) Microbiological Isolates in Patients of Chronic Dacrocystitis, Dr.Sukriti Ahuja\n" +
                    "(9) Evaluation of ocular surface,clinical signs and symptoms in chronically medicated glaucoma patients and post trabeculectomy patients, Dr.Shweta dwivedi\n",
            "Chairperson: Dr.M.K.Singh , Dr.Shashank Srivastava , Dr.Prakash Gupta , Dr.Ramesh Chandra, Dr.Vinod Biala , Dr. Anand Sharma\n\n" +
                    "Panellist: Dr.Abdul Hafiz, Dr.Deepak Mishra Dr.Alka Gupta, Dr.TejBali Singh, Dr.Ramesh Singh, Dr.Dipti verma, Dr.Anoop Srivastava\n\n" +
                    "(1) Refractive Surprise management after cataract surgery , DR.Vinod k.Arora \n" +
                    "(2) Will FLACS really stand the test of time, Dr.Harbans Lal\n" +
                    "(3) Phaco in difficult situations, Dr.Mohan Rajan\n" +
                    "(4) Femtosecondcataract Surgery-A step ahead, Dr.A.K.Grover\n" +
                    "(5) Preoperative IOL workup for premium outcome, Dr.Nagendra Prasad\n" +
                    "(6) Phaco in White Cataract, Dr.S.P.Singh\n" +
                    "(7) Phaco in hard cataract, Dr.V.K.Tewari\n" +
                    "(8) Pros and cones of premium IOL, Dr.B.N.Chaudhary\n" +
                    "(8) IPCL Intraoperative challallenges, Dr.Anjum Mazhari",
            "Chairperson: Dr.R.K.Ojha, Dr.Devendra Sood, Dr.Pankhuri Johri, Dr.Abhishek Chandra, Dr.Virag Srivastava\n\n" +
                    "Panellist: Dr.Mukesh Khare.Dr.Kshama Dwivedi, Dr.Umesh Singh, Dr.B P Tripathi, Dr.Arvind Rai, Dr.Santosh Tiwari\n\n" +
                    "(1) Evaluation and management of failing bleb....VIDEO BASED TALKG, Dr. Amit Solanki \n" +
                    "(2) Developmental Glaucoma, Dr. Devendra Sood\n" +
                    "(3) Dysfunctional bleb, Dr. Tirupati Nath\n" +
                    "(4) Riddle of viral keratitis, Dr. Ashi Khurana\n" +
                    "(5) Current therapy of Viral Keratitis, Dr. Jayita Sharan\n" +
                    "(6) Mebomian gland dysfunction-an overview, Dr. Abhishek Chandra\n" +
                    "(7) Lamellar corneal Transplants, Dr. Neeti Gupta\n" +
                    "(8) Intraoperative hard eye in cataract surgery-role of narrow angle of anterior chamber, Dr. R.K.Ojha\n" +
                    "(9) Keratometery in Lasik Evaluation, Dr. Richa Srivastava",
            "Chairperson: Dr.Vinita Singh, Dr.Shatish K. Sharma , Dr.Arti Singh , Dr.Apjit Kaur, Dr.Anurag Tandon, Dr.B.N.Chaudhary\n\n" +
                    "Panellist: Dr.Shravan Yadav, Dr.Sandesh, Dr.Devanshu Ojha, Dr.Amit Patel, Dr. K Singh, Dr Omkar Gangwar\n\n" +
                    "(1) Minimum invasive squint surgery(MISS),dont give it a miss., Dr.Naheed Akhtar \n" +
                    "(2) Aesthetic Oculoplastic Surgery-A New Dimension, Dr.A.K.Grover\n" +
                    "(3) Management of oblique muscles dysfunction, Prof. Kamlesh\n" +
                    "(4) Tips and techniques of lid reconstruction, Dr. Gyan Bhaskar\n" +
                    "(5) Post traumatic dacryocystitis, Dr. Apjit Kaur\n" +
                    "(6) Strabismus for general ophthalmologist, Dr. Vinita Singh",
            "Cordinator Dr.Mahipal Singh Sachdev\n\n" +
                    "Co-Cordinator Dr.Vipin Sahani\n\n" +
                    "Harbans Lal, Satanshu Mathur, Kapil Agrawal, Dr Jayant Guha Dinesh Talwar, Anjum Majahari, Navendu Rai, Sudhir Srivastava, Malay Chaturvedi, Abhishek Chandra ,Arup Chakrabarti, Mohan Rajan Shalini Mohan ,Joyita Sharan, Dr Vipin Sahani,",
            "Lunch",
            "Chairperson: Prof.R N Mishra, Dr.RPS Bhatia, Dr.Awadh Dube, Dr.S.K.Sharma, Dr.R.R.Shukula, Dr.V.P. Pratap, Dr.BB Lal, Dr AMJain Dr.Y.K.Mahendra\n\n" +
                    "Panellist: Dr.Shravan Yadav, Dr.Sandesh, Dr.Devanshu Ojha, Dr.Amit Patel, Dr. K Singh, Dr Omkar Gangwar\n\n" +
                    "(1) Dr.R.C.Gupta Expendings Horizons in Ophthalmoplasty and its future scope,20 min \n" +
                    "(2) Dr.Mahipal Singh Sachdev Femto lasers the new cutting tool ,20 min",
            "Chairperson:CHIEF GUEST-DR.D.RAMAMURTHY\n\n" +
                    "President, AIOS, \n" +
                    "Chairman,The Eye Foundation\n" +
                    "D.B.Road, Coimbatore 641002\n" +
                    "Pearls from the veterans : \n\n" +
                    "(1) Dr.A.M.Jain,5 min \n" +
                    "(2) Dr B.S.Goel (Life time acheivement Awards),5 min\n" +
                    "(3) Dr.A.M.Jain,5 min \n" +
                    "(4) Dr B.S.Goel (Life time acheivement Awards),5 min\n" +
                    "(5) Dr.A.M.Jain,5 min \n" +
                    "(6) Dr B.S.Goel (Life time acheivement Awards),5 min\n" +
                    "(7) Dr.A.M.Jain,5 min \n" +
                    "(8) Dr B.S.Goel (Life time acheivement Awards),5 min",
            "",
            "",
    };

    public ConferenceScheduleFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conference_schedule_fragment2, container, false);

        ListView listViewEvents = (ListView)view.findViewById(R.id.listConferenceSchedule2);

        for (int i = 0; i < titles.length; i++){
            modelAssociationEvent event = new modelAssociationEvent();
            event.setDate(dates[i]);
            event.setTime(times[i]);
            event.setTitle(titles[i]);
            event.setLocation(locations[i]);
            event.setDetails(details[i]);
            listEvents.add(event);
        }

        listViewEvents.setAdapter(new AssociationEventsAdapter(getActivity(), listEvents));
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AssociationConferenceScheduleDetailsActivity.class);
                intent.putExtra("ScheduleTitle", listEvents.get(position).getTitle());
                intent.putExtra("ScheduleTime", listEvents.get(position).getTime());
                intent.putExtra("ScheduleDetails", listEvents.get(position).getDetails());

                startActivity(intent);
            }
        });
        return view;
    }

}

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
public class ConferenceScheduleFragment3 extends Fragment {

    ArrayList<modelAssociationEvent> listEvents = new ArrayList();

    String [] dates = new String [] {
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",
            "20th Nov\n 2016",

    };

    String [] times = new String[] {
            "08 : 30 am\n to\n 09 : 30 am",
            "08 : 30 am\n to\n 09 : 30 am",
            "08 : 00 am\n to\n 10 : 00 am",
            "10 : 50 am\n to\n 12 : 50 pm",
            "10 : 10 am\n to\n 11 : 10 am",
            "10 : 10 am\n to\n 11 : 30 am",
            "11 : 20 am\n to\n 12 : 30 pm",
            "11 : 20 am\n to\n 12 : 30 pm",
            "11 : 40 am\n to\n 01 : 10 pm",
            "01 : 00 pm\n to\n 02 : 00 pm"

    };

    String [] titles = new String [] {
            "IC - 1 (Session 17) - Phacoemulsification in preoperated eyes",
            "IC - 2 (Session 18) - Keratoconus and corneal etasia decoded",
            "(Session 21) - AIOS-SESSION (120 min)",
            "(Session 19) - Indian Stalwarts IN UPSOS",
            "(Session 22) - Video Session (60 min)",
            "(Session 25) - Cornea-Update (80 min)",
            "(Session 23) - WOS (70 min)",
            "(Session 24) - Best of best for AIOS 2018",
            "(Session 26) - Kaleidoscope (90 min)",
            "(Session 20) - Court Martial - Dr.Partha Biswas"
    };

    String [] locations = new String [] {
            "HALL A",
            "HALL A",
            "HALL B",
            "HALL A",
            "HALL B",
            "HALL C",
            "HALL B",
            "HALL C",
            "HALL C",
            "HALL A",
    };

    String [] details =  new String [] {
            "Dr.S.K.Sharma, Dr.Namrata Sharma, Dr.Ajay Arora, Dr. R.Y.S. Yadav, Dr.Navendu Rai (90 min)\t\n\n" +
                    "(1) Dr.Sukant Pandey - Phaco after scleral buckling \n" +
                    "(2) Dr.Mohit Khattri - Phaco in vitrectomised eyes\n" +
                    "(3) Dr.Arup Chakrabarti - Phaco in trabeculectomy\n" +
                    "(4) Dr.Shalini Mohan - Phaco after Keratoplasty\n" +
                    "(5) Dr.Harbans Lal - Phaco after Lasik",
            "Chief Instructor : Dr.Sudhir Srivastava\t\n\n" +
                    "(1) Dr.Pushpraj Singh - Clinical Diagnosis of corneal ectasia disorder \n" +
                    "(2) Dr.Bela - Diagnostic modalities\n" +
                    "(3) Dr.Sudhir Srivastava - Management\n" +
                    "(4) Dr.Bela,Surgical management\n" +
                    "(5) Dr.Pushpraj Singh - Cataract Surgery in keratoconus\n" +
                    "(6) Dr.Dilpreet Singh - Post lasik ectasia diagnosis and management",
            "Dr. D Rammurthy \n" +
                    "Dr.Shashank Srivastava \n" +
                    "Dr. Som \n" +
                    "*ARC - SYMPOSIUM - Cataract & Glaucoma \n" +
                    "*ARC FOR YOU, Dr.Partha Biswas\n" +
                    "*CATARACT SYMPOSIUM \n\n" +
                    "Panelist:(Dr.D.P.Singh, Dr.Ashish Jaswal, Dr.P.K.Rai)\n\n" +
                    "(1) Happy Patient After Cataract Surgery, Dr.D.Ramamurthy\n" +
                    "(2)\tChallenging Cases, Dr.Partha Biswas\n" +
                    "(3)\tMarch to zero Endophthalmitis, Dr.Prashant Bhawankule\n" +
                    "Changing Paradigms of Cataract Surgery(3mins Presentation & 2 Mins Discussion)\t\n" +
                    "(a)\tFemto - Phaco For Dr.Shashank Srivastava  Against Dr.Dharmendra Nath\n" +
                    "(b)\tMultifocal v/s Monofocal For Dr.Vipin Sahni Against Dr.Kapil Agarwal\n" +
                    "(c)\tIntra cameral antibiotics For Dr.J.Wahi  Against\tDr.Malay Chaturedi\n" +
                    "GLAUCOMA SYMPOSIUM\t\n\n" +
                    "Chairman:(Dr.Devendra Sood, Dr.R.R.Shukul)\n\n" +
                    "Co-Chairman:(Dr Shalini Mohan, Dr.Amit Patel)\n\n" +
                    "Modrator: (Dr.Tirupati Nath, Dr.Deven Tulli)\n\n" +
                    "Panelist: (Dr.Bhaskar , Dr.N.K.Singh(M), Dr.Deepak Mishra)\n\n" +
                    "(1)\tDecoding Glaucoma Diagnostics - Key Points, Dr.Vaibhav Jain\n" +
                    "(2)\tMedical Management of Glaucoma – How to begin … How to end, Dr.Mukesh Kumar\n" +
                    "(3)\tSurgery for Glaucoma – When to Intervene, Dr.Devendra Sood\n\n" +
                    "The Great Glaucoma Debate : (3mins Presentation and 2 Mins Discussion)\n\n" +
                    "(a)\tEarly lens extraction for patients with PACG FOR Dr.Monika Gupta  AGAINST Dr.Tirupati Nath\n" +
                    "(b)\tTube vs Trab… the future of glaucoma surgery FOR Dr.Shalini Mohan  AGAINST Dr.Amit Solanki\n" +
                    "(c)\tNeuro protection…. Myth or Reality < FOR\tDr.R.N.Kushwaha  AGAINST Dr.Jimmy Mittal",
            "Chairperson : Dr.D.Ramamurthy, Dr.M Vanathi, Dr.Sunil Sah, Dr.Anurag Tandon Dr.Vinod K Arora, Dr.Anil K Srivastava, Dr.Verendra Agarwal\t\n\n" +
                    "Panelists : Dr.Rajendra Prasad, Dr.Srikant, Dr.Richa Srivastava, Dr.Anjum Jain Dr.B B Lal, Dr.Ashutosh Shukla\t\n\n" +
                    "(1) Dr.D Ramamurthy, Clinical Diagnosis of corneal ectasia disorder (10 min) \n" +
                    "(2) Dr.Mahipal S. Sachdev, SMILE - ADVANTAGES & LIMITATIONS (8 min)\n" +
                    "(3) Dr.Aroop Chakrabarti, Femtocataract-nucleotomy simplified (8 min)\n" +
                    "(4) Dr.OPS Maurya, Conquering the Swollen Lens \n" +
                    "(5) Dr.S.K.Bhaskar, Lameller keratoplasty (8 min)\n" +
                    "(6) Dr.Sandeep Saxena, OCT Imaging in Glaucoma (8 min)\n" +
                    "(7) Dr. M Vanathi, Focus on Ellipsoid Zone on SD-OCT in Diabetic Macular Edema (8 min)\n" +
                    "(8) Dr.Rajendra Prasad, Horizon of corneal surgery: Revisited (8 min)\n" +
                    "(9) Dr.Prashant Bawankule, Terminal Chop (8 min)\n" +
                    "(10) Dr.Dilpreet Singh, Diabetic macular edema:current perspective (8 min)\n" +
                    "(11) Listen to Masters open house discussion on 30 min FEMTO CATARACT SURGERY: DO WE REALLY NEED IT Dr.A.K.Grover, Dr.Harbans Lal, Dr.Shashank Srivastava , Dr. Anil Kumar Srivastava, Dr.Vinod Kumar Arora, Dr. Khursheed Khan, Dr.D.Ramamurthy",
            "Chairpersons :(Dr.Nitin Kumar, Dr.Ramesh Chandra, Dr.Amit Patel, Dr.Sanjeev Singh, Dr.Devanshu Ojha, Dr.Sandesh, Dr.Sangeeta Srivastava\t\n\n" +
                    "Panelist :(Dr.Shrawan Yadav, Dr.Vishnu K Agarwal, Dr.Akansha Agarwal, Dr.Ramesh Singh) \n\n" +
                    "(1.)Dr.Arup Chakrabarti\t  5 min\n" +
                    "(2.)Dr.Amit Tarafdar\t  5 min\n" +
                    "(3.)Dr.Dharmendra Nath\t  5 min\n" +
                    "(4.)Dr.Kapil Agarwal\t  5 min\n" +
                    "(5.)Dr.Romi singh\t  5 min\n" +
                    "(6.)Dr.Satyam Gupta\t  5 min\n" +
                    "(7.)Dr.Shobhit Chwala\t  5 min\n" +
                    "(8.)Dr.Anil K Srivastava\t  5 min\n" +
                    "(9.)Dr.Prakash Gupta\t  5 min\n" +
                    "(10.)Dr.Manish Mahendra\t  5 min",
            "Chairpersons :(Dr.D.J.Pandeyi, Dr.Dilpreet Singh, Dr.Awadh Dubey, Dr.A.M.Jain, Dr.Gautam (Col)) Panelist :(Dr.Ashok Singh, Dr.David, Dr.Prasoon Pandey, Dr.Ram PukarSingh, Dr.R.N.Agarwal)\n\n" +
                    "(1.)Lamellar corneal Transplant, Dr.Neeti Gupta  8 min\n" +
                    "(2.)Keratoprosthesis:the final frontier, Dr.Ashi Khurana  8 min\n" +
                    "(3.)Management of non healing corneal ulcer, Dr.Jayita Sharan  8 min\n" +
                    "(4.)Surface ablation and trans epithelial PRK, Dr.V.Sambasiva Rao  8 min, \n" +
                    "(5.)Femto laser in keratoplasty\tDr.Sunil Sah  8 min\n" +
                    "(6.)Outcomes of DALK with different graft size, Dr.Prasoon Pandey  8 min\n" +
                    "(7.)Subluxated lens and weak zonules management, Dr.Harbans Lal  8 min\n" +
                    "(8.)Ocular Surface Squamous Neoplasia, Dr.D.J.Pandey  8 min\n" +
                    "(9.)Keratoprosthesis: the final frontier, Dr.Srikant  8 min",
            "Chairpersons :(Dr.Vinita Singh, Dr.Alka Gupta, Dr.Rubi Malhotra, Dr.Veena Mall\tDr.Panna Mishra, Dr.Naheed Akhtar, Dr.Neelima Jain)\n\n" +
                    "(1.)WOS : Policies and way ahead, Dr.Alka Gupta  8 min\n" +
                    "(2.)Mentor mentee program, Dr.Jimmy Mittal  6 min\n" +
                    "(3.)Success in Private practice (start up challenges and solutions) -\tDr.Eram  Parveen  6 min\n" +
                    "(4.)Success in government setup - 2 speakers, Dr.Kshama Dwivedi  6 min, Dr.Naheed Akhtar  6 min\n" +
                    "(5.)Success as a leader, Dr.Smita Agarwal   6 min\n" +
                    "(6.)Success in a corporate set up\tDr.Neelima Mehrotra   6 min\n" +
                    "(7.)Ophthalmic subspiciality specialisation where is the bottom line Dr.Vinita Singh   8 min\n" +
                    "(8.)B Scan - easy to understand, Dr.Kirti Mittal\t  6 min\n" +
                    "(9.)Basic principals of oculoplasty to improve surgical out come, Dr.Pragati Garg   6 min\n" +
                    "(10.)Computer vision syndrome, Dr.Sangeeta Agrawal\t  6 min",
            "TOTAL 6 PAPERS FOR FINAL FREE PAPER SESSION",
            "Chairpersons :(Dr.V.P.Singh, Dr.R.K.Jaiswal, Dr.R.N.Kushwaha, Dr.Amit Tarafdar, Dr.Kapil Agarwal) Panelist :(Dr.Rajeev Agarwal, Dr.Tahir Zaidi, Dr.Vishal Katiyar, Dr.K.D.Singh, Dr S Thapper)\n\n" +
                    "(1.)Comparing different designs of presbyopia correcting IOLs, Dr.Amit Solanki  8 min\n" +
                    "(2.)Apprach to infectious keratitis, Dr. M Vanathi  8 min\n" +
                    "(3.)Topical Phaco:The best way to do it, Dr.Neelima mehrotra  8 min\n" +
                    "(4.)Management of traumatic subluxated cataract, Dr.Kapil Agarwal  8 min, (5.)Challeenging cases,its not over till its over, Dr.Amit Tarafdar  8 min\n" +
                    "(6.)Visual field analysis, Dr.B.P.Sinha  8 min\n" +
                    "(7.)Post Phacoemulsification cystoid macular edema assesment with OCT, Dr Girjesh Kain  8 min\n" +
                    "(8.)Code to crack, Dr.Malay Chaturvedi  8 min\n" +
                    "(9.)ROP Where do we stand, Dr.Dinesh Garg  8 min\n" +
                    "(10.)Toric in small pupil, Dr.Anjum Jain  8 min\n" +
                    "(11.)Post cataract surgery corneal edema, Dr.Rajat Jain  8 min",
            "Dr D Ramamurthy, Dr Mahipal Sachdev, Dr Amit Solanki, Dr.Sunil Sah Dr.Sudhir Srivastava, Dr.Upsam Goel, Dr.Kapil Agarwal, Dr.Shobhit Chawala Dr.Kamaljeet Singh Dr.Amit Tarafdar",
    };

    public ConferenceScheduleFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conference_schedule_fragment3, container, false);

        ListView listViewEvents = (ListView)view.findViewById(R.id.listConferenceSchedule3);

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

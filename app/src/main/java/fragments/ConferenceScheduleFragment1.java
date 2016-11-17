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



public class ConferenceScheduleFragment1 extends Fragment {

    String [] dates = new String [] {
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",

    };

    String [] times = new String[] {
            "08 : 30 am\n to\n 10 : 00 am",
            "09 : 00 am\n to\n 10 : 00 am",
            "09 : 00 am\n to\n 10 : 00 am",
            "10 : 10 am\n to\n 11 : 30 am",
            "10 : 10 am\n to\n 11 : 30 am",
            "10 : 00 am\n to\n 02 : 00 pm",
            "11 : 40 am\n to\n 01 : 00 pm",
            "01 : 30 pm\n to\n 02 : 30 pm",
            "03 : 00 pm\n to\n 07 : 00 pm",
            "07 : 30 pm\n Onwards",
    };

    String [] titles = new String [] {
            "(Session 1) - Free Paper 1",
            "(Session 4) - Awards",
            "(Session 6) - Quiz",
            "(Session 2) - Cataract & IOL",
            "(Session 5) - Dr. Mohan Lal Gold Medal",
            "(Session 3D)",
            "(Session 3) - Retina",
            "LUNCH",
            "(Session 7) - Live Surgery",
            "DINNER"
    };

    String [] locations = new String [] {
            "HALL A, BRD Medical college, Gorakhpur",
            "HALL B, BRD Medical college, Gorakhpur",
            "BRD Medical college, Gorakhpur",
            "HALL A, BRD Medical college, Gorakhpur",
            "HALL B, BRD Medical college, Gorakhpur",
            "HALL C, BRD Medical college, Gorakhpur",
            "HALL A, BRD Medical college, Gorakhpur",
            "",
            "Raj Eye Hospital, near university Chhatra Sangh Chauraha, Gorakhpur",
            "Raj Eye Hospital, near university Chhatra Sangh Chauraha, Gorakhpur",
    };

    String [] details = new String[]{
            "Dr.S.K.Sharma, Dr.Namrata Sharma, Dr.Ajay Arora, Dr.R.Y.S. Yadav, Dr.Navendu Rai (90 min)\t\n\n" +
                    "(1) Accidentalfoveal burn by double frequency Nd:Yag laser with subsequent improved visual acuity in 4 yrs follow up, Dr.Kankambari Pandey \n" +
                    "(2) Amniotic membrane transplantation with anterior stromal puncture and Dr.Prasoon\n" +
                    "Bowman's cautery for treatment of painful bullous keratopathy\t(3) A study of silicone rod frontalis in the management of congenital blepharoptosis, Dr.Anuj S Kushwaha\n" +
                    "(4) A novel technique to recanalize the nasolacrimal duct with endonasal approach, Dr.Alok Singh\n" +
                    "(5) Benign presentation of eyelid Malignancies-our experience, Dr.Surabhi Sinha\n" +
                    "(6) Goldenhar's syndrome 5 years case series analysis, Dr. Manoj Kumari \n" +
                    "(7) Microbiological Isolates in Patients of Chronic Dacrocystitis, Dr.Sukriti Ahuja\n" +
                    "(8) Evaluation of outcome of Cutler-Beard Procedure, Dr.Ajay Arya\n" +
                    "(9) Clinical profile and outcome of restrictive strabismus - A clinical study, Dr.Gitanjali Sharma\n" +
                    "(10) Migration of Cortical Fluid behind the Posterior Capsule, Dr.Mukesh Kumar without Zonular Dialysis or PCR\t\n" +
                    "(11) B-Scan ultrasonography before surgery in eyes with dens cataract, Dr.Deepti Yadav\n" +
                    "(12) A case report of HZO induced oculomotor nerve palsy, Dr.Chaya Verma\n" +
                    "Two papers will be selected for final on DAY-3",
            "Dr. P. K. Pandey (6 mins. each)\n\n" +
                    "(1) Profile of refrective errors at tertiary care centre. Dr.Surbhi Singh \n" +
                    "(2) Prevalence of Ocular Morbidity in School Going ChildrenDr. Alok Ranjan\n" +
                    "(3) Clinical Profile of retinoblastomain tertiary eye care centre in eastern UP. Dr.Tanmay\n" +
                    "(4) A study on clinical profile of amblyopia in children in tertiary eye care centre. Dr.Kankambari Pandey\n" +
                    "(5) Ocular surface disorders in patients admitted in intensive care unit of a tertiary care unit. Dr.Sanket Singh\n" +
                    "(6) Incidence of different types of corneal ulcers at tertiary eye care centre. Dr.Jay Vardhan \n" +
                    "(7) Organizing a Comprehensive Eye Care project in an Urban slum. Dr. Ziya Siddiqui\n" +
                    "(8) The prevalence of ROP with risk factors at a tertiary care centre. Dr.Neetu Saharan\n" +
                    "(9) Economic and Ocular impact of digital strain. Dr.Upma Awasthi\n" +
                    "(10) Demographic Profile and clinical Features of patients with endogenous endophthalmitis, presenting in a tertiary eye care centre in Western UP. Dr.Abhishek Varshney\n" +
                    "(11) Prevalence amd risk factors of age related macular degeneration(ARMD) in suburban population presenting in a tertiary hospital. Dr.Vijay Singh\n" +
                    "Two papers will be selected for final on DAY-3\n" +
                    "\nDr. V. N. Raizada - Gold Medal (6 mins. each)\n\n" +
                    "(1) Serum homocysteine is a novel biomarker for retinal nerve fibre layer thinning in diabetic retinopathy, Dr.Ankita \n" +
                    "(2) New OCT parameters as a tool for predicting visual prognosis, Dr.Upma Awasthi\n" +
                    "\nDr. Awadh Dubey - Award (6 mins. each)\n\n" +
                    "(1) Changes incentral corneal thickness and intraocular pressure during MC, Dr. Deepak Mishra \n" +
                    "(2) Intraocular Cysticercosis-Subretinal (+ 2 min video), Dr.Manjusha Agarwal\n" +
                    "\nDr. Jitendra Agarawal - Award is given to Prof. Apjit Kumar(6 mins. each)\n\n",
            "By Dr. Santosh Honavar",
            "Chairpersons :( Dr.Ashi Khurana, Dr.Vinod Rai, Dr.Anjum Jain, Dr.Richa Srivastava Dr.Ranjit Singh, Dr.B.M. Rao)\n\n" +
                    "Panelists: (Dr.Pankhuri Johari, Dr.Sangeeta Agarwal, Dr.Santosh Chaudhary, Dr.Unaiza Asif. Dr.Y.Singh Dr.V.P.Tripathi)\n\n" +
                    "(1) Phacoemulsification in corneal haze, Dr.Namrata Sharma - 10 min \n" +
                    "(2) Fixing malpositioned IOLs, Dr.Rajesh Sinha - 10 min\n" +
                    "(3) Fight toricity with toric IOLs, Dr.Sudhir Srivastava - 10 min\n" +
                    "(4) CSF and IOP : The correlation, Dr.Shalini Mohan - 10 min\n" +
                    "(5) Misadventure with IOL, Dr.Subhash Prasad - 10 min\n" +
                    "(6) Recent trends in Pediatric Cataract Surgery, Dr.Arti Elhence - 10 min\n" +
                    "(7) The unhappy patient after cataract surgery, Dr.Mohan Rajan - 10 min\n" +
                    "(8) Management of Black,White and Posterior polar Cataracts, Dr.Veena Mall - 10 min",
            "Chairpersons :(Dr.Vinod Biala Dr.V.K.Tewari , Dr.,Ashish Jaswal, Dr.Mayank Srivastava, Dr.Jitendra Kumar, Dr.R.N.Kushwaha Dr.Vasant K. Singh)\n\n" +
                    "(1) To evaluate the potential association of raised HbA1c, Dyslipidaemia, hypertension,anaemia and nephropathy with Severity of Diabetic Retinopathy, Dr.Vanlalhruaii \n" +
                    "(2) Study of awareness about their disese and the causes od delay in ophhalmicintervention in a set of urban patients of diabetic retinopathy, Dr. Varun Kharbanda - 10 min\n" +
                    "(3) Retinal imaging techniques as a tool for predicting visual prognosis in DR, Dr.Upma Awasthi\n" +
                    "(4) A comparative study on different modalities of treatment in Central serous chorioretinopathy, Dr.Nisha Bathla\n" +
                    "(5) SD-OCT based novel imaging biomarkers of macular edema for progression of diabetic retinopathy, Dr.Manila Khatri\n" +
                    "(6) Study on visual outcome,visual recovery time,recurrence in patients of CSR treated with acetazolamide, Dr.Nisha Bathla\n" +
                    "(7) A study to assess changes in contrastsensitivity in PDR patients undergoing anti-VEGF treatment, Dr.Vishal Yadav\n" +
                    "(8) Variation in intra-ocular pressure after intravitreal injuction of anti-vegf, Dr.Hemendra Singh\n" +
                    "(8) Prevalence and pattern of macular oedema in diabetes mellitus type2, Dr.Vijay Singh\n" +
                    "(8) Long term results of Deep Anterior Lamellar v/s Penitrating keratoplasty, Dr.Pranav kumar Srivastava",
            "(1) Know How's in Oculoplastic Surgeries-Key Note, Dr.R.C.Gupta \n" +
                    "(2) 3D - Mind the mimic, Recognize the real, Dr.Santosh Honavar\n",
            "Chairpersons :(Dr.Durgesh Srivastava, Dr.Malay Chaturvedi, Dr.RYS Yadav Dr.Navendu Rai, Dr.Kirti Mittal)\n\n" +
                    "Panelists: (Dr.Raj Kumar Singh, Dr.V K Agarwal, Dr.Dinesh Singh, Dr.Abdul Rashid Dr.K.Singh, Dr.Ajaypati Tripathi)\n\n" +
                    "(1) Management of lens in RD surgery, Dr.Namrata Sharma - 10 min \n" +
                    "(2) Retinopathy of prematurity - an Overview, Dr.Rajesh Sinha - 10 min\n" +
                    "(3) ROP Screening made easy, Dr.Sudhir Srivastava - 10 min\n" +
                    "(4) Management of Giant Retinal Tear, Dr.Shalini Mohan - 10 min\n" +
                    "(5) Retinopathy of prematurity…screening and treatment protocol in Indian scenario, Dr.Subhash Prasad - 10 min\n" +
                    "(6) Recent advances in management of Diabetic Macular Edema, Dr.Aloy Majumdar - 10 min\n" +
                    "(7) Recent in CSR, Dr.Kamaljeet Singh - 10 min\n" +
                    "(8) Innovations in MIVS: Quality without compromise new device to my credit for giving Intravitreal Injections + Arora's Intravitreal injection Device (AIID), Dr.Ajay Arora - 12 min",
            "Lunch",
            "Chairperson: (Dr.Kamaljeet Singh, Dr.Yogesh C Shah, Dr.Sambasiva Rao, Dr.Satanshu Mathur, Dr Ashi Khurana, Dr Shobhit Chawala, Dr R K Ojha, Dr Abhishek Chandra, Dr.Malay Chaturvedi, Dr.Dinesh Talwar, Dr Hemant Kumar)\n\n" +
                    "(1) SICS, DR. A K SINGH \n" +
                    "(2) PHACO, DR.S.P.SINGH\n" +
                    "(3) GLUED IOL, DR.DHARMENDRA NATH\n" +
                    "(4) FEMTOCATARACT, DR. MAHIPAL S.SACHDEV\n" +
                    "(5) FEMTO LASIK, DR.A.K.SRIVASTAVA\n" +
                    "(6) SMART TORIC IOL, DR. MALAY CHATURVEDI\n" +
                    "(7) INTRAVITREAL INJECTION, DR.DINESH TALWAR, DR.JAYANT GUHA\n" +
                    "(8) PHACO-TORIC IOL Sx, DR.HARBANS LAL\n" +
                    "(9) PHACO HARD ROCK, DR.HARSHUL TAK\n" +
                    "(10) TRAB, DR.SHALINI MOHAN\n" +
                    "(11) FEMTO-CATARACT, DR.SUDHIR SRIVASTAVA\n" +
                    "(12) PTOSIS, DR. ABHISHEK CHANDRA\n" +
                    "(13) VITRECTOMY, DR.SHOBHIT CHAWLA\n" +
                    "(14) IPCL, DR.ANJUM MAZAHARI\n" +
                    "(15) SHOWING PHACO ON CENTURIAN, DR.SUBHASH PRASAD\n" +
                    "(16) PHACO, DR.RAJENDRA PRASAD\n" +
                    "(17) PPV + EXPLANATION OF DISOLCATED IOL+FLUED IOL, DR.AVANINDRA GUPTA",
            "Dinner"
    };

    public ConferenceScheduleFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conference_schedule_fragment1, container, false);

        ListView listViewEvents = (ListView)view.findViewById(R.id.listConferenceSchedule1);
        final ArrayList<modelAssociationEvent> listEvents = new ArrayList();

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

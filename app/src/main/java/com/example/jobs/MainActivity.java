package com.example.jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.JobsModel;
import com.example.jobs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText etCity,etState,etPincode,classNum,etName,etInstitution,etQualification,etFee,etRemarks;
    RadioGroup rMode,jobType;
    Button btnApply,btnHashTag;
    LinearLayout linLay1,linLay2;
    TextView tvLocation;
    String City,State,Pincode,Institution,mySub;

    private FirebaseFirestore fStore;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner=findViewById(R.id.Spinner);
        etCity=findViewById(R.id.city);
        etState=findViewById(R.id.state);
        etPincode=findViewById(R.id.pincode);
        classNum=findViewById(R.id.Class);
        rMode=findViewById(R.id.mode);
        etName=findViewById(R.id.name);
        etInstitution=findViewById(R.id.institution);
        jobType=findViewById(R.id.jobType);
        etQualification=findViewById(R.id.qualification);
        etFee=findViewById(R.id.fee);
        etRemarks=findViewById(R.id.remarks);
        btnApply=findViewById(R.id.btnApply);
        btnHashTag=findViewById(R.id.btnHashTag);
        linLay1=findViewById(R.id.linLay1);
        linLay2=findViewById(R.id.linLay2);
        tvLocation=findViewById(R.id.location);

        fStore= FirebaseFirestore.getInstance();
        collectionReference= fStore.collection("applicants");


        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Subject,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tvLocation.setVisibility(View.GONE);
        linLay2.setVisibility(View.GONE);
        etInstitution.setVisibility(View.GONE);

        /*rMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r = rMode.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(r);
                Toast.makeText(getApplicationContext(), radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/



        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name= etName.getText().toString().trim();
                final String Class=classNum.getText().toString().trim();
                mySub = spinner.getSelectedItem().toString();

                /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
                final String Mode=((RadioButton)findViewById(rMode.getCheckedRadioButtonId())).getText().toString();

                rMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(Mode=="Offline"){
                            tvLocation.setVisibility(View.VISIBLE);
                            linLay2.setVisibility(View.VISIBLE);

                            City=etCity.getText().toString().trim();
                            State=etState.getText().toString().trim();
                            Pincode=etPincode.getText().toString().trim();
                        }
                    }
                });

                final String JobType=((RadioButton)findViewById(jobType.getCheckedRadioButtonId())).getText().toString();

                /*jobType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(JobType=="Institution"){
                            etInstitution.setVisibility(View.VISIBLE);
                            Institution=etInstitution.getText().toString().trim();
                        }
                    }
                });*/

                String Qualification = etQualification.getText().toString().trim();
                String Fee = etFee.getText().toString().trim();
                String Remarks =etRemarks.getText().toString().trim();
                String State = etState.getText().toString().trim();
                String City = etCity.getText().toString().trim();
                String Pincode = etPincode.getText().toString().trim();
                String Institution = etInstitution.getText().toString().trim();


                final JobsModel jobsModel= new JobsModel();
                jobsModel.setName(name);
                jobsModel.setSubject(mySub);
                jobsModel.setCls(Class);
                jobsModel.setMode(Mode);
                jobsModel.setCity(City);
                jobsModel.setState(State);
                jobsModel.setPin(Pincode);
                jobsModel.setInstitution(Institution);
                jobsModel.setQualification(Qualification);
                jobsModel.setFee(Fee);
                jobsModel.setRemarks(Remarks);
                jobsModel.setJobType(JobType);

                collectionReference.add(jobsModel);

            }
        });

    }

    public void radioOffline(View view) {
        linLay2.setVisibility(View.VISIBLE);
    }

    public void radioInstitution(View view) {
        etInstitution.setVisibility(View.VISIBLE);
    }

    public void seePostedJobs(View view) {
        Intent intent = new Intent(this,RecyclerViewJobs.class);
        startActivity(intent);
    }

    public void hashTag(View view) {
        Intent intent = new Intent(this , HashTagActivity.class);
        startActivity(intent);
    }
}
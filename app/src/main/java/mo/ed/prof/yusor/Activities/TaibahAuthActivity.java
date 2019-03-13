package mo.ed.prof.yusor.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dd.processbutton.iml.GenerateProcessButton;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mo.ed.prof.yusor.Adapter.CustomSpinnerAdapter;
import mo.ed.prof.yusor.GenericAsyncTasks.RetrieveDepartmentsAsyncTask;
import mo.ed.prof.yusor.Network.VerifyConnection;
import mo.ed.prof.yusor.R;
import mo.ed.prof.yusor.Volley.JsonParser;
import mo.ed.prof.yusor.helpers.Config;
import mo.ed.prof.yusor.helpers.Designsers.ProgressGenerator;
import mo.ed.prof.yusor.helpers.Room.StudentsEntity;
import mo.ed.prof.yusor.helpers.SessionManagement;

public class TaibahAuthActivity extends AppCompatActivity implements RetrieveDepartmentsAsyncTask.OnDepartmentsRetrievalTaskCompleted,
ProgressGenerator.OnCompleteListener{


    String URL="http://fla4news.com/Yusor/api/v1/departments?fbclid=IwAR3iTd672TyD6ZHfFdJvf_DWgrjBVqOE649MB_oUZWLI2zO0PsVBhimTOcA";

    ArrayList<StudentsEntity> FacultiesList;
    private String Category;

    @BindView(R.id.Edit_first_name)
    EditText Edit_first_name;

    @BindView(R.id.Edit_last_name)
    EditText Edit_last_name;

    @BindView(R.id.Edit_email)
    EditText Edit_email;

    @BindView(R.id.email_type)
    TextView email_type;

    @BindView(R.id.Edit_Username)
    EditText Edit_Username;

    @BindView(R.id.Edit_password)
    EditText Edit_password;

    @BindView(R.id.radioGenderGroup)
    RadioGroup radioGenderGroup;

    @BindView(R.id.Faculties_spinner)
    Spinner Faculties_spinner;

    @BindView(R.id.btnUpload)
    GenerateProcessButton btnUpload;

//    @BindView(R.id.btnRegister)
//    ActionProcessButton actionProcessButtonRegister;
    private String FirstName;
    private String LastName;
    private String Email;
    private String UserName;
    private String Password;
    private RadioButton checkedRadioButtion;
    private String selectedGender;
    private String DepartmentName;
    private String PersonName;
    private ProgressGenerator progressGenerator;
    private SessionManagement sessionManagement;

    @Override
    protected void onResume() {
        super.onResume();
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        if (verifyConnection.isConnected()){
            RetrieveDepartmentsAsyncTask retrieveDepartmentsAsyncTask= new RetrieveDepartmentsAsyncTask(this, getApplicationContext());
            retrieveDepartmentsAsyncTask.execute(URL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taibah_auth);
        ButterKnife.bind(this);
        FacultiesList = new ArrayList<StudentsEntity>();

        sessionManagement= new SessionManagement(getApplicationContext());

        FirstName= Edit_first_name.getText().toString();
        LastName=Edit_last_name.getText().toString();
        PersonName=FirstName+" "+LastName;
        Email=email_type.getText().toString();
        UserName=Edit_Username.getText().toString();
        Password=Edit_password.getText().toString();
        checkedRadioButtion=(RadioButton)radioGenderGroup.findViewById(radioGenderGroup.getCheckedRadioButtonId());
        selectedGender= checkedRadioButtion.getText().toString();


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnUpload.setEnabled(false);
                progressGenerator = new ProgressGenerator((ProgressGenerator.OnCompleteListener) getApplicationContext(), getApplicationContext());
                progressGenerator.start(btnUpload, PersonName, Email, UserName, Password, selectedGender, DepartmentName);
            }
        });

        Faculties_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DepartmentName = FacultiesList.get(position).getDepartmentName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDepartmentsRetrievalApiTaskCompleted(ArrayList<StudentsEntity> result) {
        if (result.size() > 0) {
            Config.FacultiesList=result;
            CustomSpinnerAdapter customSpinnerAdapterFaculties = new CustomSpinnerAdapter(getApplicationContext(), result);
            Faculties_spinner.setAdapter(customSpinnerAdapterFaculties);
        }
    }


    @Override
    public void onComplete(ArrayList<StudentsEntity> studentsEntities) {
        if (studentsEntities!=null){
            if (studentsEntities.size()>0){
                SharedPrefThenGalleryHomeRedirect(studentsEntities);
            }
        }
    }

    private void SharedPrefThenGalleryHomeRedirect(ArrayList<StudentsEntity> studentsEntities) {
        for (StudentsEntity studentsEntity: studentsEntities){
            PersonName= studentsEntity.getPersonName();
            Email=studentsEntity.getEmail();
            UserName=studentsEntity.getUserName();
            selectedGender=studentsEntity.getGender();
            DepartmentName=studentsEntity.getDepartmentName();
        }
        sessionManagement.createLoginSession(PersonName,Email,UserName,selectedGender, DepartmentName);
        sessionManagement.createLoginSessionType("EP");
        Intent intent_create=new Intent(this,MainActivity.class);
        startActivity(intent_create);
    }
}
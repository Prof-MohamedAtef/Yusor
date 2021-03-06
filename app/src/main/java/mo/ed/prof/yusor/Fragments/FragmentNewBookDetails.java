package mo.ed.prof.yusor.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import mo.ed.prof.yusor.Adapter.AuthorsSpinnerAdapter;
import mo.ed.prof.yusor.Adapter.FacultiesSpinnerAdapter;
import mo.ed.prof.yusor.GenericAsyncTasks.FacultiesAsyncTask;
import mo.ed.prof.yusor.GenericAsyncTasks.RetrieveAuthorsAsyncTask;
import mo.ed.prof.yusor.Listeners.UploadBookApi;
import mo.ed.prof.yusor.Network.SnackBarClassLauncher;
import mo.ed.prof.yusor.Network.VerifyConnection;
import mo.ed.prof.yusor.R;
import mo.ed.prof.yusor.Volley.MakeVolleyRequests;
import mo.ed.prof.yusor.helpers.Config;
import mo.ed.prof.yusor.helpers.RetrofitUtils.RetrofitClient;
import mo.ed.prof.yusor.helpers.Room.StudentsEntity;
import mo.ed.prof.yusor.helpers.SessionManagement;
import mo.ed.prof.yusor.helpers.RetrofitUtils.UploadCallbacks;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static mo.ed.prof.yusor.helpers.Config.AuthURL;
import static mo.ed.prof.yusor.helpers.Config.BASE_URL;
import static mo.ed.prof.yusor.helpers.Config.FacultiesURL;
import static mo.ed.prof.yusor.helpers.Config.currentImagePAth;
import static mo.ed.prof.yusor.helpers.Config.selectedImagePath;

/**
 * Created by Prof-Mohamed Atef on 3/15/2019.
 */

public class FragmentNewBookDetails extends Fragment implements
        RetrieveAuthorsAsyncTask.OnAuthorsRetrievalTaskCompleted,
        FacultiesAsyncTask.OnFacultiesRetrievalTaskCompleted ,
        MakeVolleyRequests.OnRetrofitCompleteListener,
        MakeVolleyRequests.OnFailureListener,
        UploadCallbacks {

    @BindView(R.id.camera)
    ImageView Camera;

    @BindView(R.id.BookImage)
    ImageView BookImage;

    @BindView(R.id.Auth_spinner)
    Spinner Auth_spinner;

    @BindView(R.id.CircleImageLinear)
    LinearLayout CircleImageLinear;

    @BindView(R.id.Faculty_spinner)
    Spinner Faculty_spinner;

    @BindView(R.id.Edit_addBook)
    EditText Edit_addBook;

    @BindView(R.id.EditAuthorName)
    EditText EditAuthorName;

    @BindView(R.id.Edit_isbnNum)
    EditText Edit_isbnNum;

    @BindView(R.id.Edit_PublishYear)
    EditText Edit_PublishYear;

    @BindView(R.id.Edit_enterDescription)
    EditText Edit_enterDescription;

    @BindView(R.id.Next_BTN)
    Button Next_BTN;

    @BindView(R.id.Back_BTN)
    Button Back_BTN;

    private SnackBarClassLauncher snackBarLauncher;
    private java.lang.String KEY_AuthPosition="KEY_AuthPosition";
    private String KEY_AUTHList="KEY_AUTHList";
    private String KEY_FacultiesLIST ="KEY_FacultiesLIST";
    private String KEY_POSITION="KEY_POSITION";
    private java.lang.String SampleDateFormat_KEY="yyyyMMdd_HHmmss";
    private String JPEG_KEY="JPEG_";
    private VerifyConnection verifyConnection;
    private String IMAGE_TYPE="image/*";
    final static int SELECT_PICTURE=12;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 55;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final int GALLERY_PICTURE = 1;
    final static int RESULT_LOAD_IMAGE=11;
    private String FILE_EXTENSION="file:";
    private String DATA_KEY="data";
    Bitmap imageBitmap;
    String [] filePathColumn;
    Uri selectedImage;
    File fileNaming;
    Bitmap bitmap;
    String imageFileName;
    private java.lang.String JPG_EXTENSION=".jpg";
    private String UploadedImage1;
    private Uri ImageFileUri;
    private String imageName;
    private String ImageURL_KEY="imageFileUri";
    private boolean HasImage=false;
    private String BookTitle;
    private String BookID;
    private String BookName;
    private String AuthorName;
    private String ISBN_Num;
    private String PublishYear;
    private Uri ImageUri;
    private String FacultyName;
    private String KEY_BookName="KEY_BookName";
    private String KEY_AutherName="KEY_AutherName";
    private String KEY_ISBN_Num="KEY_ISBN_Num";
    private String KEY_FacultyName="KEY_FacultyName";
    private String KEY_PublishYear="KEY_PublishYear";
    private MakeVolleyRequests makeVolleyRequests;
    private String KEY_BookDescription="KEY_BookDescription";
    private String KEY_AuthorID="KEY_AuthorID";
    private String KEY_FacultyID="KEY_FacultyID";
    private SessionManagement sessionManagement;
    private HashMap<String, String> user;
    private String ApiToken;
    UploadBookApi mService;
    private ProgressDialog progressDialog;

    private UploadBookApi getAPIUpload(){
        return RetrofitClient.getClient(BASE_URL).create(UploadBookApi.class);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Auth_spinner
        verifyConnection=new VerifyConnection(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        user =sessionManagement.getUserDetails();
        if (user!=null) {
            ApiToken = user.get(SessionManagement.KEY_idToken);
        }

        mService=getAPIUpload();
//        Bundle bundle=getArguments();
//        if (bundle!=null){
//            BookID= bundle.getString(BookID_KEY);
//            BookTitle=bundle.getString(BookTitle_KEY);
//            Config.BookName=BookTitle;
//            Config.BookID=BookID;
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_new_book_details, container, false);
        ButterKnife.bind(this,rootView);
        snackBarLauncher=new SnackBarClassLauncher();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Config.AuthList!=null){
            outState.putSerializable(KEY_AUTHList, Config.AuthList);
        }
        outState.putInt(KEY_AuthPosition,Config.AuthPosition);
        if (Config.FacultiesList!=null){
            outState.putSerializable(KEY_FacultiesLIST, Config.FacultiesList);
        }
        outState.putInt(KEY_POSITION,Config.BookPosition);
        if (Config.ImageFileUri!=null){
            outState.putString(ImageURL_KEY, Config.ImageFileUri.toString());
        }
        if (Config.BookName!=null){
            outState.putString(KEY_BookName,Config.BookName);
        }
        if (EditAuthorName.getText().length()>0) {
            Config.AuthorTitle = EditAuthorName.getText().toString();
            outState.putString(KEY_AutherName, Config.AuthorTitle);
        }
        if (Config.AuthorID!=null){
            outState.putString(KEY_AuthorID, Config.AuthorID);
        }
        if (Config.ISBN_Number!=null){
            outState.putString(KEY_ISBN_Num,Config.ISBN_Number);
        }
        if (Config.FacultyName!=null){
            outState.putString(KEY_FacultyName,Config.FacultyName);
        }
        if (Config.FacultyID!=null){
            outState.putString(KEY_FacultyID, Config.FacultyID);
        }
        if (Config.PublishYear!=null){
            outState.putString(KEY_PublishYear,Config.PublishYear);
        }
        if (Config.BookDescription!=null){
            outState.putString(KEY_BookDescription, Config.BookDescription);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED&&data!=null){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    imageFileName= data.getData().getPath();
                    fileNaming=new File(imageFileName);
                    fileNaming= fileNaming.getAbsoluteFile();
                    imageName= fileNaming.getName();
                    ImageFileUri =data.getData();
                    Config.ImageFileUri=ImageFileUri;
                    imageBitmap = (Bitmap) extras.get(DATA_KEY);
                    setBitmapToImageView(imageBitmap);
                    try{
                        createImageFile();
                        addPicToPhone();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle selectedImage = data.getExtras();
                    ImageFileUri=data.getData();
                    Config.ImageFileUri=ImageFileUri;
                    imageFileName=data.getData().getPath();
                    fileNaming=new File(imageFileName);
                    imageName= fileNaming.getName();
                    filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                    String filePath =  MediaStore.Images.Media.DATA ;
                    Bitmap  imagebitmap=(Bitmap)selectedImage.get(DATA_KEY);
                    Config.imageBitmap=imagebitmap.toString();
                    Cursor c = getActivity().getContentResolver().query(Uri.parse(filePath), filePathColumn, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = c.getString(columnIndex);
                    Config.image_name=selectedImagePath;
                    c.close();
                    if (selectedImagePath != null) {
                        bitmap = BitmapFactory.decodeFile(selectedImagePath);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        setBitmapToImageView(bitmap);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.canceled),
                            Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                if (data!=null){
                    selectedImage = data.getData();
                    ImageFileUri =data.getData();
                    imageFileName=data.getData().getPath();
                    String FileName= getImageFilePath(ImageFileUri);
//                    String FileName= getImagePath(ImageFileUri);
                    fileNaming=new File(imageFileName);
                    imageName= fileNaming.getName();
                    Config.ImageFileUri=ImageFileUri;
                    if (selectedImage!=null){
                        imageBitmap= LoadThenDecodeBitmap(FileName);
                        setBitmapToImageView(imageBitmap);
                    }else {
                        Bundle selectedImage = data.getExtras();
                        imageFileName=data.getData().getPath();
                        fileNaming=new File(imageFileName);
                        imageName= fileNaming.getName();
                        ImageFileUri =data.getData();
                        Config.ImageFileUri=ImageFileUri;
                        filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                        imageBitmap=(Bitmap)selectedImage.get(DATA_KEY);
                        Config.imageBitmap=imageBitmap.toString();
                        if (imageBitmap!= null) {
                            bitmap = Bitmap.createScaledBitmap(imageBitmap, 400, 400, false);
                            setBitmapToImageView(bitmap);
                        }
                    }
                }
            }else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
                if (data!=null){
                    selectedImage = data.getData();
                    ImageFileUri =data.getData();
                    imageFileName=data.getData().getPath();

                    String FileName= getImageFilePath(ImageFileUri);
//                    String FileName= getImagePath(ImageFileUri);
                    fileNaming=new File(imageFileName);
                    imageName= fileNaming.getName();

                    Config.ImageFileUri=ImageFileUri;
//                    filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                    if (selectedImage!=null){
                        imageBitmap= LoadThenDecodeBitmap(FileName);
                        setBitmapToImageView(imageBitmap);
                    }else {
                        Bundle selectedImage = data.getExtras();
                        imageFileName=data.getData().getPath();
                        fileNaming=new File(imageFileName);
                        imageName= fileNaming.getName();
                        ImageFileUri =data.getData();
                        Config.ImageFileUri=ImageFileUri;
                        filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                        imageBitmap=(Bitmap)selectedImage.get(DATA_KEY);
                        Config.imageBitmap=imageBitmap.toString();
                        if (imageBitmap!= null) {
                            bitmap = Bitmap.createScaledBitmap(imageBitmap, 400, 400, false);
                            setBitmapToImageView(bitmap);
                        }
                    }
                }
            }
            if (Config.currentImagePAth!=null){
                UploadedImage1= Config.currentImagePAth;
            }else if (Config.imageBitmap!=null){
                UploadedImage1= Config.imageBitmap;
            }else if (Config.selectedImagePath!=null){
                UploadedImage1=Config.selectedImagePath;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getImageFilePath(Uri imageUri) {
        String wholeID = DocumentsContract.getDocumentId(imageUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getActivity().getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private void setUriToImageView(Uri uriToImageView) {
        if (BookImage.getDrawable()==null){
            BookImage.setImageURI(uriToImageView);
        }else {
            BookImage.setImageURI(uriToImageView);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
            if (savedInstanceState.getSerializable(KEY_AUTHList)!=null){
                Config.AuthList = (ArrayList<StudentsEntity>) savedInstanceState.getSerializable(KEY_AUTHList);
                Config.AuthPosition = savedInstanceState.getInt(KEY_AuthPosition);
            }
            if (Config.AuthList != null) {
                PopulateExistingAuthorsList(Config.AuthList, Config.AuthPosition);
            }
            if (savedInstanceState.getSerializable(KEY_FacultiesLIST)!=null){
                Config.FacultiesList= (ArrayList<StudentsEntity>) savedInstanceState.getSerializable(KEY_FacultiesLIST);
                Config.FacultyPosition = savedInstanceState.getInt(KEY_POSITION);
            }
            if (Config.FacultiesList != null) {
                PopulateExistingFacultiesList(Config.FacultiesList, Config.FacultyPosition);
            }
            if (savedInstanceState.getString(ImageURL_KEY)!=null){
                ImageFileUri= Uri.parse(savedInstanceState.getString(ImageURL_KEY));
                if (ImageFileUri!=null){
                    setUriToImageView(ImageFileUri);
                }
            }
            if (savedInstanceState.getString(KEY_BookName)!=null){
                Config.BookName=savedInstanceState.getString(KEY_BookName);
                Edit_addBook.setText(Config.BookName);
            }
            if (Config.Author_Edit){
                if (savedInstanceState.getString(KEY_AutherName)!=null){
                    Config.AuthorTitle= savedInstanceState.getString(KEY_AutherName);
                    EditAuthorName.setText(Config.AuthorTitle);
                }
                if (savedInstanceState.getString(KEY_AuthorID)!=null){
                    Config.AuthorID= savedInstanceState.getString(KEY_AuthorID);
                }
                EditAuthorName.setVisibility(View.VISIBLE);
            }
            if (Config.ISBN_Number!=null) {
                Edit_isbnNum.setText(Config.ISBN_Number);
            }
            if (Config.PublishYear!=null) {
                Edit_PublishYear.setText(Config.PublishYear);
            }
            if (Config.BookDescription!=null){
                Edit_enterDescription.setText(Config.BookDescription);
            }
        }else {
            if (verifyConnection.isConnected()) {
                RetrieveAuthorsAsyncTask retrieveAuthorsAsyncTask = new RetrieveAuthorsAsyncTask((RetrieveAuthorsAsyncTask.OnAuthorsRetrievalTaskCompleted) FragmentNewBookDetails.this, getActivity());
                retrieveAuthorsAsyncTask.execute(AuthURL);
                FacultiesAsyncTask facultiesAsyncTask= new FacultiesAsyncTask((FacultiesAsyncTask.OnFacultiesRetrievalTaskCompleted) FragmentNewBookDetails.this, getActivity());
                facultiesAsyncTask.execute(FacultiesURL);
            }
        }

        CircleImageLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAuthorName.setVisibility(View.VISIBLE);
                Config.Author_Edit=true;
                Auth_spinner.setEnabled(false);
            }
        });

        Back_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentNewBookDetails.OnBackButtonPressed) getActivity()).OnBackButtonPressed();
            }
        });

        Next_BTN.setEnabled(true);
        Next_BTN.setOnClickListener(new View.OnClickListener() {
            public String AuthTit;

            @Override
            public void onClick(View v) {
                if (verifyConnection.isConnected()){
                    makeVolleyRequests=new MakeVolleyRequests(getActivity(),FragmentNewBookDetails.this, FragmentNewBookDetails.this);
                    BookName= Edit_addBook.getText().toString();
                    Config.BookName=BookName;
                    if (Config.Author_Edit){
                        AuthorName=EditAuthorName.getText().toString();
                        Config.AuthorTitle=AuthorName;
                        if (AuthorName!=null){
                            if (AuthorName.length()>0){
                                Config.AuthorID=null;
                            }
                        }
                    }
                    ISBN_Num= Edit_isbnNum.getText().toString();
                    Config.ISBN_Number=ISBN_Num;
                    PublishYear= Edit_PublishYear.getText().toString();
                    Config.BookDescription=Edit_enterDescription.getText().toString();
                    Config.PublishYear=PublishYear;
                    ImageUri=Config.ImageFileUri;
                    if (Config.BookName!=null&&Config.BookName.length()>0){
                        if (Config.BookDescription!=null&&Config.BookDescription.length()>0){
                            if (Config.PublishYear!=null&&Config.PublishYear.length()>0){
                                if (Config.FacultyID!=null){
                                    if (user!=null) {
                                        ApiToken = user.get(SessionManagement.KEY_idToken);
                                        if (ApiToken != null) {
                                            if (Config.ISBN_Number != null && Config.ISBN_Number.length() > 0) {
                                                if (Config.ImageFileUri != null && Config.ImageFileUri.toString().length() > 0) {
                                                    if (Config.AuthorID != null) {
                                                        Next_BTN.setEnabled(false);
                                                        makeVolleyRequests.sendBookDetails(mService, Config.BookName, Config.BookDescription, Config.AuthorID, Config.PublishYear, Config.FacultyID, Config.ISBN_Number, null, Config.ImageFileUri, ApiToken);
                                                    } else {
                                                        if (Config.AuthorTitle != null) {
                                                            Next_BTN.setEnabled(false);
                                                            makeVolleyRequests.sendBookDetails(mService, Config.BookName, Config.BookDescription, null, Config.PublishYear, Config.FacultyID, Config.ISBN_Number, Config.AuthorTitle, Config.ImageFileUri, ApiToken);
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity(), getString(R.string.enter_image), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), getString(R.string.enter_isbn_num), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }else {
                                    Toast.makeText(getActivity(), getString(R.string.enter_faculty_id), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), getString(R.string.enter_publish_year), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), getString(R.string.enter_desc), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), getString(R.string.enter_book_name), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.cannot_start_chat), Toast.LENGTH_LONG).show();
                }
            }
        });

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialougeChooseCameraOrGallery();
            }
        });

        Auth_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Config.AuthorID= Config.AuthList.get(position).getAuthorID();
                Config.AuthPosition= position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Faculty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Config.FacultyName = Config.FacultiesList.get(position).getDepartmentName();
                Config.FacultyID= Config.FacultiesList.get(position).getDepartmentID();
                Config.FacultyPosition=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void sendBookDetails(final String bookName, final String bookDescription, final String authorID,
                                 final String publishYear, final String facultyID, final String isbn_num,
                                 final String authorName, final Uri photo, final String api_token) {
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMessage("Uploading ...");
//        progressDialog.setIndeterminate(false);
//        progressDialog.setMax(100);
//        progressDialog.setCancelable(false);
//        if (!progressDialog.isShowing()){
//            progressDialog.show();
//        }
//        File file = FileUtils.getFile(getActivity(), photo);
//        ProgressRequestBody requestFile = new ProgressRequestBody(file, this);
//        final MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
//
//        final RequestBody b_name=RequestBody.create(MultipartBody.FORM,bookName);
//        final RequestBody b_desc=RequestBody.create(MultipartBody.FORM,bookDescription);
//        final RequestBody b_authorID=RequestBody.create(MultipartBody.FORM,authorID);
//        final RequestBody b_publishYear=RequestBody.create(MultipartBody.FORM,publishYear);
//        final RequestBody b_deaprtID=RequestBody.create(MultipartBody.FORM,facultyID);
//        final RequestBody b_isbn=RequestBody.create(MultipartBody.FORM,isbn_num);
//        final RequestBody b_authorName=RequestBody.create(MultipartBody.FORM,authorName);
//        final RequestBody b_apiToken=RequestBody.create(MultipartBody.FORM,api_token);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mService.uploadFileAndTextData(body,b_name,b_desc,
//                        b_authorID,b_publishYear,
//                        b_deaprtID,b_isbn,
//                        b_authorName,b_apiToken)
//                        .enqueue(new Callback<String>() {
//                            @Override
//                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                if (response.isSuccessful()){
//                                        try {
//                                            JsonParser jsonParser = new JsonParser();
//                                            ArrayList<StudentsEntity> studentsEntities = jsonParser.parseAddedBooksJsonDetails(response);
//                                            if (studentsEntities != null) {
//                                                if (studentsEntities.size() > 0) {
//                                                    mListener.onProgressComplete(studentsEntities);
//                                                }
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                progressDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onFailure(Call<String> call, Throwable t) {
//                                progressDialog.dismiss();
//                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//            }
//        }).start();
    }

    private void PopulateExistingAuthorsList(ArrayList<StudentsEntity> AuthorssList, int auth_Position) {
        AuthorsSpinnerAdapter authorsSpinnerAdapter= new AuthorsSpinnerAdapter(getActivity(), AuthorssList);
        Auth_spinner.setAdapter(authorsSpinnerAdapter);
        Auth_spinner.setSelection(auth_Position);
    }

    @Override
    public void onAuthorsRetrievalApiTaskCompleted(ArrayList<StudentsEntity> result) {
        if (result.size() > 0) {
            Config.AuthList=result;
            PopulateExistingAuthorsList(result, 0);
        }
    }

    @Override
    public void onFacultiesRetrievalApiTaskCompleted(ArrayList<StudentsEntity> result) {
        if (result.size() > 0) {
            Config.FacultiesList=result;
            PopulateExistingFacultiesList(result, 0);
        }
    }

    private void PopulateExistingFacultiesList(ArrayList<StudentsEntity> result, int position) {
        FacultiesSpinnerAdapter customSpinnerAdapterFaculties = new FacultiesSpinnerAdapter(getActivity(), result);
        Faculty_spinner.setAdapter(customSpinnerAdapterFaculties);
        Faculty_spinner.setSelection(position);
    }

    private void DialougeChooseCameraOrGallery() {
        Intent pickIntent = new Intent();
        pickIntent.setType(IMAGE_TYPE);
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = this
                .getResources()
                .getString(R.string.chooser_Intent_select_or_take_picture); // Or
        // get
        // from
        // strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent,
                pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                new Intent[] { takePhotoIntent });
        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    private void setBitmapToImageView(Bitmap imageBitmap) {
        if (BookImage.getDrawable()==null){
            BookImage.setImageBitmap(imageBitmap);
            BookImage.setVisibility(View.VISIBLE);
            HasImage=true;
        }else {
            BookImage.setImageBitmap(imageBitmap);
            HasImage=true;
        }
    }

    public String getImagePath(Uri uri) {
        String selectedImagePath;
        // 1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        } else {
            selectedImagePath = null;
        }

        if (selectedImagePath == null) {
            // 2:OI FILE Manager --- call method: uri.getPath()
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }

    private Bitmap LoadThenDecodeBitmap(String fileName){
//        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        selectedImagePath= cursor.getString(columnIndex);
        imageBitmap= decodeSampledBitmapFromResource(fileName,100,100);
        selectedImagePath=selectedImagePath;
        Config.imageBitmap=imageBitmap.toString();
        Config.image_name=selectedImagePath;
        return imageBitmap;
    }


    public static Bitmap decodeSampledBitmapFromResource(String selectedImagePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath,options);
        Config.selectedImagePath=selectedImagePath;
        Config.image_name=selectedImagePath;
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeFile(selectedImagePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private File createImageFile() throws IOException {
        //create image name
        File image = null;
        CreateImageFileName();
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Config.StorageDir=storageDirectory;
        image = CreateTempFileMethod(storageDirectory);
        return image;
    }

    private void CreateImageFileName(){
        String timpstamp = new SimpleDateFormat(SampleDateFormat_KEY).format(new Date());
        imageFileName = timpstamp+"1" + JPEG_KEY ;
        Config.image_name=imageFileName;
    }

    @NonNull
    private File CreateTempFileMethod(File storageDirectory) throws IOException {
        File image;
        image = File.createTempFile(imageFileName, JPG_EXTENSION, storageDirectory);
        //save file name
        currentImagePAth = FILE_EXTENSION + image.getAbsolutePath();
        currentImagePAth=currentImagePAth;
        return image;
    }

    private void  addPicToPhone(){
        Intent mediaScanIntent= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f= new File(currentImagePAth);
        Uri contentUri= Uri.fromFile(f);
        selectedImage=contentUri;
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

//    @Override
//    public void onProgressComplete(ArrayList<StudentsEntity> studentsEntities) {

//        if (studentsEntities!=null){
////            if (fileNaming!=null){
//                if (studentsEntities.size()>0){
//                    for (StudentsEntity studentsEntity:studentsEntities){
//                        if (studentsEntity.getException()!=null){
//                            Toast.makeText(getActivity(), studentsEntity.getException().toString(),Toast.LENGTH_SHORT).show();
//                            Next_BTN.setEnabled(true);
//                        }else {
//                            ((FragmentNewBookDetails.OnNextDetailsRequired) getActivity()).onNextNewBookNameDetailsNeeded(studentsEntity.getBookTitle(),studentsEntity.getBookID());
//                        }
//                    }
//                }
////            }
//        }
//    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onSuccess() {
        ((FragmentNewBookDetails.OnBookSelectionNeeded)getActivity()).OnOfferAdditionNeeded();
    }

    @Override
    public void onFailure() {
        Next_BTN.setEnabled(true);
        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }


    public interface OnNextDetailsRequired{
        void onNextNewBookNameDetailsNeeded(String BookName, String BookID);
    }

    public interface OnBookSelectionNeeded{
        void OnOfferAdditionNeeded();
    }

    public interface OnBackButtonPressed{
        void OnBackButtonPressed();
    }
}
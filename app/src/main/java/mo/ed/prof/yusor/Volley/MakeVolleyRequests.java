package mo.ed.prof.yusor.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONException;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import mo.ed.prof.yusor.Listeners.UploadBookApi;
import mo.ed.prof.yusor.R;
import mo.ed.prof.yusor.helpers.Config;
import mo.ed.prof.yusor.helpers.RetrofitUtils.ProgressRequestBody;
import mo.ed.prof.yusor.helpers.RetrofitUtils.StatusError;
import mo.ed.prof.yusor.helpers.RetrofitUtils.UploadCallbacks;
import mo.ed.prof.yusor.helpers.Room.AppDatabase;
import mo.ed.prof.yusor.helpers.Room.Helper.InsertClass;
import mo.ed.prof.yusor.helpers.Room.StudentsEntity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Prof-Mohamed Atef on 3/15/2019.
 */

public class MakeVolleyRequests implements UploadCallbacks{

    private final OnMyBookCompleteListener onMyBookCompleteListener;
    private final OnSearchSuggestedBooksCompListener onSearchSuggestedBooksCompListener;
    private final AppDatabase mDatabase;
    Context mContext;
    private String KEY_BOOKTitle="title";
    private String KEY_BOOKDescription="desc";
    private String KEY_AuthorID="author_id";
    private String KEY_PublishYear="publish_year";
    private String KEY_DepartID="department_id";
    private String KEY_ISBN="ISBN_num";
    private String KEY_AuthorName="name";
    private String KEY_PHOTO="photo";
    private String KEY_BOOKID="book_id";
    private String KEY_APIKEY="api_token";
    private ProgressDialog progressDialog;
    private RequestBody b_authorName;
    private RequestBody b_authorID;


    public MakeVolleyRequests(Context context, OnCompleteListener onCompleteListener, AppDatabase mDatabase){
        this.mContext=context;
        this.mListener= (OnCompleteListener) onCompleteListener;
        onMyBookCompleteListener = null;
        onSearchSuggestedBooksCompListener = null;
        this.mDatabase=mDatabase;
    }

    public MakeVolleyRequests(Context context, OnCompleteListener onCompleteListener){
        this.mContext=context;
        this.mListener= (OnCompleteListener) onCompleteListener;
        onMyBookCompleteListener = null;
        onSearchSuggestedBooksCompListener = null;
        this.mDatabase=null;
    }

    public MakeVolleyRequests(Context context, OnRetrofitCompleteListener onRetrofitCompleteListener, OnFailureListener onFailureListener){
        this.mContext=context;
        this.onRetrofitListener= (OnRetrofitCompleteListener) onRetrofitCompleteListener;
        onMyBookCompleteListener = null;
        onSearchSuggestedBooksCompListener = null;
        this.onFailureListener=onFailureListener;
        mDatabase = null;
    }

    public MakeVolleyRequests(Context context, OnMyBookCompleteListener onMyBookCompleteListener){
        this.mContext=context;
        this.onMyBookCompleteListener= (OnMyBookCompleteListener) onMyBookCompleteListener;
        onSearchSuggestedBooksCompListener = null;
        mDatabase = null;
    }

    public MakeVolleyRequests(Context context, OnSearchSuggestedBooksCompListener onSearchSuggestedBooksCompListener){
        this.mContext=context;
        this.onSearchSuggestedBooksCompListener= (OnSearchSuggestedBooksCompListener) onSearchSuggestedBooksCompListener;
        onMyBookCompleteListener = null;
        mDatabase = null;
    }


    public void approveBillRequest(final String billID, final String apiToken) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/update_buyer_atatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.ApproveBill(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",apiToken);
                hashMap.put("bill_id",billID);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String KEY_BOOKNAME="book_name";

    public void searchSuggestedBooks(final String BookID, final String BookName, final String tokenID) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/similar_books",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();

                                ArrayList<StudentsEntity> studentsEntities = jsonParser.getSimilarBooks(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        onSearchSuggestedBooksCompListener.OnSearchSuggestedBooksCompleted(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put(KEY_BOOKID,BookID);
                hashMap.put(KEY_BOOKNAME,BookName);
                hashMap.put(KEY_APIKEY,tokenID);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getPreviousAddedBooks() {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.signUpJsonParse(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
//                hashMap.put(KEY_BOOKNAME,personName);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void sendBookDetails(final UploadBookApi uploadBookApi, final String bookName, final String bookDescription, final String authorID, final String publishYear, final String facultyID, final String isbn_num, final String authorName, final Uri photo, final String api_token) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
        File file = FileUtils.getFile(mContext, photo);
        ProgressRequestBody requestFile = new ProgressRequestBody(file, this);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        final RequestBody b_name=RequestBody.create(MultipartBody.FORM,bookName);
        final RequestBody b_desc=RequestBody.create(MultipartBody.FORM,bookDescription);
        if (authorID!=null){
            b_authorID=RequestBody.create(MultipartBody.FORM,authorID);
        }
        final RequestBody b_publishYear=RequestBody.create(MultipartBody.FORM,publishYear);
        final RequestBody b_deaprtID=RequestBody.create(MultipartBody.FORM,facultyID);
        final RequestBody b_isbn=RequestBody.create(MultipartBody.FORM,isbn_num);
        if (authorName!=null){
            b_authorName=RequestBody.create(MultipartBody.FORM,authorName);
        }
        final RequestBody b_apiToken=RequestBody.create(MultipartBody.FORM,api_token);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (authorID==null){
                    uploadBookApi.uploadFileAndTextDatawithoutAuthID(body,b_name,b_desc, b_publishYear,
                            b_deaprtID,b_isbn,b_authorName, b_apiToken)
                            .enqueue(new Callback<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    if (response.isSuccessful()) {
                                        onRetrofitListener.onSuccess();
                                    } else {
                                        onFailureListener.onFailure();
                                    }
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                                    onFailureListener.onFailure();
                                }
                            });
                }else {
                    uploadBookApi.uploadFileAndTextDatawithAuthID(body,b_name,b_desc,
                            b_authorID,b_publishYear,
                            b_deaprtID,b_isbn, b_apiToken)
                            .enqueue(new Callback<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    if (response.isSuccessful()) {
                                        onRetrofitListener.onSuccess();
                                    } else {
                                        onFailureListener.onFailure();
                                    }
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                                    onFailureListener.onFailure();
                                }
                            });
                }
            }
        }).start();
    }

    private OnCompleteListener mListener;
    private OnRetrofitCompleteListener onRetrofitListener;
    private OnFailureListener onFailureListener;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private StatusError getStatusError(ResponseBody responseBody) {
        StatusError statusError = null;

        if (responseBody != null) {
            try {
                BufferedSource source = responseBody.source();

                if (source != null) {
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = StandardCharsets.UTF_8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(StandardCharsets.UTF_8);
                    }

                    String string = buffer.clone().readString(charset);

                    if (!TextUtils.isEmpty(string)) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        statusError = gson.fromJson(string, StatusError.class);
                        String msg=statusError.message;
                        ArrayList<String> errors= statusError.errors;
                    }
                }
            } catch (Exception e) {
                Log.e("Error:", "Impossible to get StatusError stream", e);
            }
        }
        return statusError;
    }

    public void getAllBooksForSale(final String api_token) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/books_student",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                            ArrayList<StudentsEntity> studentsEntities=new ArrayList<>();
                            studentsEntities.clear();
                            mListener.onComplete(studentsEntities);
                       }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.parseAllBooksForSale(mContext, response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                        InsertClass insertClass = new InsertClass();
                                        insertClass.TryInsertGalleryBooksData(mDatabase, studentsEntities, mListener);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put(KEY_APIKEY,api_token);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getSoldBills(final String apiToken) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/Bills_sold",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.getSoldBills(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }else {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put(KEY_APIKEY,apiToken);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getAllBooksForUser(final String apiToken) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/Mybooks",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.getMyBooks(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }else if (studentsEntities.size()==0){
                                        Toast.makeText(mContext,mContext.getResources().getString(R.string.no_books_exist),Toast.LENGTH_LONG).show();
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",apiToken);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getAllBooksForUser_Similar(final String apiToken) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/Mybooks_similar",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                CopyOnWriteArrayList<StudentsEntity> studentsEntities = jsonParser.getMyBooksForSimilar(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        onMyBookCompleteListener.OnMyBookCompleted(studentsEntities);
                                    }else if (studentsEntities.size()==0){
                                        Toast.makeText(mContext,mContext.getResources().getString(R.string.no_books_exist),Toast.LENGTH_LONG).show();
                                        onMyBookCompleteListener.OnMyBookCompleted(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",apiToken);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void ApproveBill(final String billID, final String apiToken) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/update_buyer_atatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.ApproveBill(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",apiToken);
                hashMap.put("bill_id",billID);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateBookDetails(final String bookName, final String bookDescription, final String author_id, final String publishYear, final String facultyID,
                                  final String isbn_number, final String apiToken, final String bookID, final String bookStatus, final String availability,
                                  final String transactionTypesId, final String price , final String authorTitle) {
        //http://fla4news.com/Yusor/api/v1/update_book
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/update_book",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.updateBook(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("title",bookName);
                hashMap.put("desc",bookDescription);
                hashMap.put("author_id",author_id);
                hashMap.put("publish_year",publishYear);
                hashMap.put("department_id",facultyID);
                hashMap.put("ISBN_num",isbn_number);
                hashMap.put("api_token",apiToken);
                hashMap.put("book_id",bookID);
                hashMap.put("book_status",bookStatus);
                hashMap.put("availability",availability);
                hashMap.put("transaction_types_id",transactionTypesId);
                hashMap.put("price",price);
                hashMap.put("name",authorTitle);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void removeBook(final String apiToken, final String bookID) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/delete_book",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.deleteBook(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",apiToken);
                hashMap.put("book_id",bookID);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void sendReport(final String apiToken, final String text_sharedIdeas) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/reports",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.sendReports(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",apiToken);
                hashMap.put("text",text_sharedIdeas);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getProfile(final String tokenID) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.getProfile(response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_token",tokenID);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    public void sendResetEmail(final String email) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/reset-password",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.resetPassword(mContext,response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",email);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void sendResetCode(final String code) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/check_code",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.resetCode(mContext,response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("code",code);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void ResetPassConfirmation(final String pass_1, final String pass_2, final String studentID) {
        final RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://fla4news.com/Yusor/api/v1/update_password",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                JsonParser jsonParser = new JsonParser();
                                ArrayList<StudentsEntity> studentsEntities = jsonParser.updatePassword(mContext,response);
                                if (studentsEntities != null) {
                                    if (studentsEntities.size() > 0) {
                                        mListener.onComplete(studentsEntities);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("password",pass_1);
                hashMap.put("password_confirmation",pass_2);
                hashMap.put("student_id",studentID);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public interface OnCompleteListener {
        public void onComplete(ArrayList<StudentsEntity> studentsEntities);
    }

    public interface OnFailureListener {
        public void onFailure();
    }

    public interface OnMyBookCompleteListener {
        public void OnMyBookCompleted(CopyOnWriteArrayList<StudentsEntity> studentsEntities);
    }

    public interface OnSearchSuggestedBooksCompListener{
        public void OnSearchSuggestedBooksCompleted(ArrayList<StudentsEntity> studentsEntities);
    }

    public interface OnRetrofitCompleteListener {
        public void onSuccess();
    }
}

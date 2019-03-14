package mo.ed.prof.yusor.GenericAsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mo.ed.prof.yusor.R;
import mo.ed.prof.yusor.helpers.Room.StudentsEntity;

/**
 * Created by Prof-Mohamed Atef on 3/12/2019.
 */

public class RetrieveDepartmentsAsyncTask extends AsyncTask<String, Void, ArrayList<StudentsEntity>> {

    private final String LOG_TAG = RetrieveDepartmentsAsyncTask.class.getSimpleName();
    public JSONObject DepartmentsJson;
    public JSONArray DepartmentsDataArray;
    public JSONObject oneDepartmentData;
    private ProgressDialog dialog;
    public RetrieveDepartmentsAsyncTask retrieveDepartmentsAsyncTask;
    private ArrayList<StudentsEntity> list = new ArrayList<StudentsEntity>();
    RetrieveDepartmentsAsyncTask.OnDepartmentsRetrievalTaskCompleted onDepartmentsRetrievalTaskCompleted;
    Context mContext;
    private String ID_STR;
    private String NAME_STR;
    private StudentsEntity studentsEntity;
    private String ID_KEY="id";
    private String Name_KEY="name";
    private Activity activity;

    public RetrieveDepartmentsAsyncTask(RetrieveDepartmentsAsyncTask.OnDepartmentsRetrievalTaskCompleted onDepartmentsRetrievalTaskCompleted, Context context) {
        this.onDepartmentsRetrievalTaskCompleted = onDepartmentsRetrievalTaskCompleted;
        dialog = new ProgressDialog(context);
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            dialog.setOwnerActivity((Activity) mContext);
            activity = dialog.getOwnerActivity();
            if (dialog != null && dialog.isShowing()) {
                this.dialog.dismiss();
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                    this.dialog.setMessage(mContext.getResources().getString(R.string.loading));
                    if (!activity.isFinishing()) {
                        this.dialog.show();
                    }
                } else {
                    this.dialog.setMessage(mContext.getResources().getString(R.string.loading));
                    if (!activity.isFinishing()) {
                        this.dialog.show();
                    }
                }
            }
        } catch (Exception e) {
            Log.v(LOG_TAG, "Problem in ProgressDialogue");
        }
    }

    @Override
    protected void onPostExecute(ArrayList<StudentsEntity> result) {
        super.onPostExecute(result);
        if (result != null) {
            if (onDepartmentsRetrievalTaskCompleted != null) {
                onDepartmentsRetrievalTaskCompleted.onDepartmentsRetrievalApiTaskCompleted(result);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    @Override
    protected ArrayList<StudentsEntity> doInBackground(String... strings) {

        String Articles_JsonSTR = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        if (strings.length == 0) {
            return null;
        }

        try {

            URL url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Articles_JsonSTR  = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            Articles_JsonSTR = buffer.toString();
            Log.v(LOG_TAG, "Articles JSON String: " + Articles_JsonSTR );
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error here Exactly ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getDepartmentsJson(Articles_JsonSTR );
        } catch (JSONException e) {
            Log.e(LOG_TAG, "didn't got Articles Data from getJsonData method", e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<StudentsEntity> getDepartmentsJson(String usersDesires_jsonSTR) throws JSONException {
        DepartmentsJson = new JSONObject(usersDesires_jsonSTR);
        DepartmentsDataArray= DepartmentsJson.getJSONArray("data");
        list.clear();
        for (int i = 0; i < DepartmentsDataArray.length(); i++) {
            try {
                oneDepartmentData = DepartmentsDataArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ID_STR= oneDepartmentData.getString(ID_KEY);
            NAME_STR= oneDepartmentData.getString(Name_KEY);


            if (ID_STR==null){
                ID_STR="";
            }
            if (NAME_STR==null){
                NAME_STR="";
            }

            studentsEntity = new StudentsEntity(ID_STR, NAME_STR);
            list.add(studentsEntity);
        }
        return list;
    }

    public interface OnDepartmentsRetrievalTaskCompleted {
        void onDepartmentsRetrievalApiTaskCompleted(ArrayList<StudentsEntity> result);
    }
}
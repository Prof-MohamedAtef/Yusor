package mo.ed.prof.yusor.helpers.Room;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import mo.ed.prof.yusor.helpers.Room.Dao.BooksDao;

/**
 * Created by Prof-Mohamed Atef on 4/15/2019.
 */

@Database(entities = {StudentsEntity.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase INSTANCE;
    private static String DATABASE_NAME="Books-database";

    public abstract BooksDao booksDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getAppDatabase(Context context, final AppExecutors executors) {
        if (INSTANCE == null) {
            try {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                        // allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
                        .allowMainThreadQueries()
                        .build();
                // Add a delay to simulate a long-running operation
//                addDelay();
                // Generate the data for pre-population
                AppDatabase database = AppDatabase.getInstance(context, executors);
                // notify that the database was created and it's ready to be used
                database.setDatabaseCreated();
            }catch (Exception e){
                return null;
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }


    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE== null) {
                    INSTANCE= getAppDatabase(context.getApplicationContext(), executors);
                    INSTANCE.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }
}

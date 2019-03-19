package mo.ed.prof.yusor.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mo.ed.prof.yusor.Adapter.Chat.MessageAdapter;
import mo.ed.prof.yusor.R;
import mo.ed.prof.yusor.helpers.Config;
import mo.ed.prof.yusor.helpers.Firebase.ChatHandler.FirebaseChatHandler;
import mo.ed.prof.yusor.helpers.Firebase.FirebaseEntites;
import mo.ed.prof.yusor.helpers.SessionManagement;

import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.AuthorName_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.BookDescription_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.BookID_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.BookName_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.BookOwnerID_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.BookSellerID_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.ISBN_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.Price_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.PublishYear_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.SellerEmail_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.SellerFacultyName_KEY;
import static mo.ed.prof.yusor.Adapter.BooksGalleryAdapter.Transaction_KEY;

public class ChatActivity extends AppCompatActivity {

    private String Messages_KEY="messages";
    private String FacultyName;
    private String TransactionType;
    private String Price;
    private String SellerEmail;
    private String BookDescription;
    private String PublishYear;
    private String AuthorName;
    private String ISBN;
    private String BookOwnerID;
    private String BookID;
    private String BookName;
    FirebaseChatHandler firebaseChatHandler;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mMessagesDatabase;
    private FirebaseStorage mFirebaseStorage;
    private ListView mListViewMessage;
    private EditText mEditTextMessage;
    private LinearLayout mButtonSend;
    private int DEFAULT_MSG_LENGTH_LIMIT=500;
    private MessageAdapter mMessageAdapter;
    private SessionManagement sessionManagement;
    private HashMap<String, String> user;
    private String UserName;
    private FirebaseEntites firebaseEntities;
    private String UserID;
    private String SellerID;
    public static String BuyerSellerMessagingKey;
    private String Buyer="buyer";
    private String Seller="seller";
    private String SellerBuyerMessagingKey;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTheme(R.style.AppTheme);
        sessionManagement=new SessionManagement(getApplicationContext());
        user=sessionManagement.getUserDetails();
        if (user!=null) {
            UserName= user.get(SessionManagement.KEY_UserName);
            UserID= user.get(SessionManagement.KEY_userID);
        }

        Intent intent=getIntent();
        BookOwnerID = intent.getExtras().getString(BookOwnerID_KEY);
        BookID = intent.getExtras().getString(BookID_KEY);
        SellerID= intent.getExtras().getString(BookSellerID_KEY);
        BookName= intent.getExtras().getString(BookName_KEY);
        BookDescription= intent.getExtras().getString(BookDescription_KEY);
        PublishYear= intent.getExtras().getString(PublishYear_KEY);
        AuthorName= intent.getExtras().getString(AuthorName_KEY);
        ISBN= intent.getExtras().getString(ISBN_KEY);
        Price= intent.getExtras().getString(Price_KEY);
        TransactionType= intent.getExtras().getString(Transaction_KEY);
        SellerEmail= intent.getExtras().getString(SellerEmail_KEY);
        FacultyName= intent.getExtras().getString(SellerFacultyName_KEY);
        firebaseChatHandler=new FirebaseChatHandler();
        BuyerSellerMessagingKey =UserID+Buyer+SellerID+Seller;
        SellerBuyerMessagingKey=UserID+Buyer+SellerID+Seller;
        if (mMessagesDatabase ==null){
            database= FirebaseDatabase.getInstance();
            mMessagesDatabase =database.getReference().child(Messages_KEY);
            if (Config.Buyer){
//                .child(BuyerSellerMessagingKey).child(UserID+Buyer);
//                mMessagesDatabase =database.getReference();
                mMessagesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(BuyerSellerMessagingKey).exists()){
                            if (dataSnapshot.child(BuyerSellerMessagingKey).child(UserID+Buyer).exists()){
                                //do nothing
                            }else {
                                firebaseChatHandler.setWelcomeMessage(UserName+" started the Conversation");
                                mMessagesDatabase.child(BuyerSellerMessagingKey).child(UserID+Buyer).setValue(firebaseChatHandler);
                            }
                        }else {
                            firebaseChatHandler.setWelcomeMessage(UserName+" started the Conversation");
                            mMessagesDatabase.child(BuyerSellerMessagingKey).child(UserID+Buyer).setValue(firebaseChatHandler);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else {
                mMessagesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(SellerBuyerMessagingKey).exists()){
                            if (dataSnapshot.child(SellerBuyerMessagingKey).child(SellerID+Seller).exists()){
                                //do nothing
                            }else {
                                firebaseChatHandler.setWelcomeMessage(UserName+" started the Conversation");
                                mMessagesDatabase.child(SellerBuyerMessagingKey).child(SellerID+Seller).setValue(firebaseChatHandler);
                            }
                        }else {
                            firebaseChatHandler.setWelcomeMessage(UserName+" started the Conversation");
                            mMessagesDatabase.child(SellerBuyerMessagingKey).child(SellerID+Seller).setValue(firebaseChatHandler);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

//            mDatabase.child(messaging_key).child(buyer_id+"buyer");
//            String key= mDatabase.child(messaging_key).child(buyer_id+"buyer").push().getKey();

            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseStorage = FirebaseStorage.getInstance();

//            mMessagesDatabase.keepSynced(true);
        }

        mListViewMessage = findViewById(R.id.listViewMessage);
        mEditTextMessage = findViewById(R.id.editTextMessage);
        mButtonSend = findViewById(R.id.buttonSend);

        List<FirebaseChatHandler> chatMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(getApplicationContext(), R.layout.item_message, chatMessages);
        mListViewMessage.setAdapter(mMessageAdapter);

        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mButtonSend.setClickable(true);
                } else {
                    mButtonSend.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEditTextMessage.setFilters(new InputFilter[] {new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseChatHandler= new FirebaseChatHandler(mEditTextMessage.getText().toString(), UserName, UserID, SellerID,null);
                firebaseChatHandler.setWelcomeMessage(null);
                firebaseEntities=new FirebaseEntites(mMessagesDatabase);

                if (mMessagesDatabase!=null){
                    database= FirebaseDatabase.getInstance();
                    mMessagesDatabase =database.getReference().child(Messages_KEY).child(BuyerSellerMessagingKey).child(UserID+Buyer);
                }else {
                    database= FirebaseDatabase.getInstance();
                    mMessagesDatabase =database.getReference().child(Messages_KEY).child(BuyerSellerMessagingKey).child(UserID+Buyer);
                }
//                mMessagesDatabase
                if (Config.Buyer){
                    if (mMessagesDatabase!=null){
                        database= FirebaseDatabase.getInstance();
                        mMessagesDatabase =database.getReference().child(Messages_KEY).child(BuyerSellerMessagingKey).child(UserID+Buyer);
                    }else {
                        database= FirebaseDatabase.getInstance();
                        mMessagesDatabase =database.getReference().child(Messages_KEY).child(BuyerSellerMessagingKey).child(UserID+Buyer);
                    }
                    firebaseEntities.AddMessage(mMessagesDatabase, BuyerSellerMessagingKey,firebaseChatHandler,UserID,SellerID);
                }else {
                    if (mMessagesDatabase!=null){
                        database= FirebaseDatabase.getInstance();
                        mMessagesDatabase =database.getReference().child(Messages_KEY).child(SellerBuyerMessagingKey).child(SellerID+Seller);
                    }else {
                        database= FirebaseDatabase.getInstance();
                        mMessagesDatabase =database.getReference().child(Messages_KEY).child(SellerBuyerMessagingKey).child(SellerID+Seller);
                    }
                    firebaseEntities.AddMessage(mMessagesDatabase, SellerBuyerMessagingKey,firebaseChatHandler,UserID,SellerID);
                }

                mEditTextMessage.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //initial firebase commponent
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.chat_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();
        /* Settings menu item clicked */
        if (id == R.id.call_icon) {
            //implement call function
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

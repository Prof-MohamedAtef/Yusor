package mo.ed.prof.yusor.helpers.Room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

/**
 * Created by Prof-Mohamed Atef on 2/5/2019.
 */
@Entity(tableName = "Books")
public class StudentsEntity implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int ID;

    @Nullable
    @ColumnInfo(name = "BuyerFirebaseUiD")
    public String BuyerFirebaseUiD;

    @NonNull
    @ColumnInfo(name = "SellerFirebaseUid")
    public String SellerFirebaseUid;

    @Nullable
    @ColumnInfo(name = "BillID")
    public String BillID;

    @Nullable
    @ColumnInfo(name = "OwnerStatus")
    public String OwnerStatus;

    @Nullable
    @ColumnInfo(name = "BuyerStatus")
    public String BuyerStatus;

    @Nullable
    @ColumnInfo(name = "BuyerID")
    public String BuyerID;

    @NonNull
    @ColumnInfo(name = "BillStatus")
    public String BillStatus;


    @Ignore
    @NonNull
    @ColumnInfo(name = "StudentID")
    public String StudentID;

    @NonNull
    @ColumnInfo(name = "PivotID")
    public String PivotID;

    @NonNull
    @ColumnInfo(name = "SellerEmail")
    public String SellerEmail;

    @NonNull
    @ColumnInfo(name = "SellerGender")
    public String SellerGender;

    @NonNull
    @ColumnInfo(name = "SellerUserName")
    public String SellerUserName;

    @NonNull
    @ColumnInfo(name = "SellerDepartmentID")
    public String SellerDepartmentID;

    @NonNull
    @ColumnInfo(name = "SellerID")
    public String SellerID;

    @NonNull
    @ColumnInfo(name = "TransactionType")
    public String TransactionType;

    @NonNull
    @ColumnInfo(name = "Availability")
    public String Availability;

    @NonNull
    @ColumnInfo(name = "ISBN_NUM")
    public String ISBN_NUM;

    @Ignore
    @NonNull
    @ColumnInfo(name = "BookPhoto")
    public String BookPhoto;

    @NonNull
    @ColumnInfo(name = "BookDescription")
    public String BookDescription;

    @NonNull
    @ColumnInfo(name = "PublishYear")
    public String PublishYear;

    @NonNull
    @ColumnInfo(name = "BookID")
    public String BookID;

    @NonNull
    @ColumnInfo(name = "BookTitle")
    public String BookTitle;


    @NonNull
    @ColumnInfo(name = "BookPrice")
    public String BookPrice;


    @NonNull
    @ColumnInfo(name = "BookImage")
    public String BookImage;


    @NonNull
    @ColumnInfo(name = "BookOwnerID")
    public String BookOwnerID;

    @NonNull
    @ColumnInfo(name = "BookStatus")
    public String BookStatus;


    @NonNull
    @ColumnInfo(name = "DepartmentID")
    public String DepartmentID;

    @NonNull
    @ColumnInfo(name = "DepartmentName")
    public String DepartmentName;

    @Ignore
    @NonNull
    @ColumnInfo(name = "UserName")
    public String UserName;

    @NonNull
    @ColumnInfo(name = "PersonName")
    public String PersonName;

    @Ignore
    @NonNull
    @ColumnInfo(name = "Email")
    public String Email;


    public String Password;

    @Ignore
    @NonNull
    @ColumnInfo(name = "Gender")
    public String Gender;

    String API_TOKEN;


    public static Object FBAccessToken;

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String billStatus) {
        BillStatus = billStatus;
    }


    @Ignore
    public StudentsEntity(String bookID_str, String book_title_str, String book_description_str,
                          String publishYear_str, String isbn_str, String photo_str, String authorID_str,
                          String departmentID_str, String studentID_str, String price_str, String bookStatus_str,
                          String availability_str, String transactionType_str, String SellerFBUID) {
        this.BookID=bookID_str;
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.ISBN_NUM=isbn_str;
        this.BookPhoto=photo_str;
        this.AuthorTitle=authorID_str;
        this.DepartmentName=departmentID_str;
        this.StudentID=studentID_str;
        this.BookPrice=price_str;
        this.BookStatus=bookStatus_str;
        this.Availability=availability_str;
        this.TransactionType=transactionType_str;
        this.SellerFirebaseUid=SellerFBUID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String CreatedAt;

    @Ignore
    public StudentsEntity(String userID_str, String userName_str, String gender_str,
                          String email_str, String firebaseUserID_str, String department_str) {
        this.UserID=userID_str;
        this.UserName=userName_str;
        this.Gender=gender_str;
        this.Email=email_str;
        this.FirebaseUiD=firebaseUserID_str;
        this.DepartmentName=department_str;
    }

    @Ignore
    public StudentsEntity(String studentID_str, String firebaseUserID_str, String s, String s1, String s2) {
        this.StudentID=studentID_str;
        this.FirebaseUiD=firebaseUserID_str;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String UpdatedAt;



    public String BuyerEmail;
    public String BuyerUserName;
    public String BuyerPName;
    public String BuyerGender;
    public String BuyerDepartmentID;



    public String SellerPersonName;
    public String FirebaseUiD;
    public String ProfilePhoto;

    @Ignore
    public StudentsEntity(String billID_str, String price_str, String ownerStatus_str, String buyerStatus_str, String studentID_str,
                          String buyerID_str, String bookID_str, String book_title_str, String book_description_str,
                          String publishYear_str, String authorID_str, String departmentID_str, String isbn_str, String photo_str,
                          String studentID_str1, String bookID_str1, String sellerPName_str, String sellerUserName_str,
                          String sellerGender_str, String sellerEmail_str, String departmentID_str1, String sellerFireBUiD_str,
                          String buyerPName_STR,String buyerUserName_STR, String buyerGender_STR, String buyerEmail_STR,
                          String buyerDepartmentID_STR, String buyerFireBUiD_STR) {
        this.BillID = billID_str;
        this.BookPrice = price_str;
        this.OwnerStatus = ownerStatus_str;
        this.BuyerStatus = buyerStatus_str;
        this.SellerID = studentID_str;
        this.BuyerID = buyerID_str;
        this.BookID = bookID_str;
        this.BookTitle = book_title_str;
        this.BookDescription = book_description_str;
        this.SellerFirebaseUid = sellerFireBUiD_str;
        this.BuyerPName = buyerPName_STR;
        this.BuyerGender = buyerGender_STR;
        this.BuyerDepartmentID = buyerDepartmentID_STR;
        this.AuthorID = authorID_str;
        this.DepartmentID = departmentID_str;
        this.ISBN_NUM = isbn_str;
        this.BookPhoto = photo_str;
        this.SellerPersonName = sellerPName_str;
        this.SellerUserName = sellerUserName_str;
        this.SellerGender = sellerGender_str;
        this.SellerEmail = sellerEmail_str;
        this.BuyerUserName = buyerUserName_STR;
        this.BuyerEmail = buyerEmail_STR;
        this.BuyerFirebaseUiD = buyerFireBUiD_STR;
    }

    @Ignore
    public StudentsEntity(String billID_str, String bookID_str, String buyerID_str, String ownerStatus_str, String buyerStatus_str,
                          String studentID_str, String book_title_str, String book_description_str, String publishYear_str,
                          String authorID_str, String departmentID_str, String isbn_str) {
        this.BillID=billID_str;
        this.BookID=bookID_str;
        this.BuyerID=buyerID_str;
        this.OwnerStatus=ownerStatus_str;
        this.BuyerStatus=buyerStatus_str;
        this.SellerID=studentID_str;
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.AuthorID=authorID_str;
        this.DepartmentID=departmentID_str;
        this.ISBN_NUM=isbn_str;
    }

    public String getBuyerFirebaseUiD() {
        return BuyerFirebaseUiD;
    }

    public void setBuyerFirebaseUiD(String buyerFirebaseUiD) {
        BuyerFirebaseUiD = buyerFirebaseUiD;
    }

    public String getBuyerEmail() {
        return BuyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        BuyerEmail = buyerEmail;
    }

    public String getBuyerUserName() {
        return BuyerUserName;
    }

    public void setBuyerUserName(String buyerUserName) {
        BuyerUserName = buyerUserName;
    }

    public String getBuyerPName() {
        return BuyerPName;
    }

    public void setBuyerPName(String buyerPName) {
        BuyerPName = buyerPName;
    }

    public String getBuyerGender() {
        return BuyerGender;
    }

    public void setBuyerGender(String buyerGender) {
        BuyerGender = buyerGender;
    }

    public String getBuyerDepartmentID() {
        return BuyerDepartmentID;
    }

    public void setBuyerDepartmentID(String buyerDepartmentID) {
        BuyerDepartmentID = buyerDepartmentID;
    }

    public String getSellerFirebaseUid() {
        return SellerFirebaseUid;
    }

    public void setSellerFirebaseUid(String sellerFirebaseUid) {
        SellerFirebaseUid = sellerFirebaseUid;
    }

    public String getBillID() {
        return BillID;
    }

    public void setBillID(String billID) {
        BillID = billID;
    }

    public String getOwnerStatus() {
        return OwnerStatus;
    }

    public void setOwnerStatus(String ownerStatus) {
        OwnerStatus = ownerStatus;
    }

    public String getBuyerStatus() {
        return BuyerStatus;
    }

    public void setBuyerStatus(String buyerStatus) {
        BuyerStatus = buyerStatus;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public void setBuyerID(String buyerID) {
        BuyerID = buyerID;
    }

    public String getSellerPersonName() {
        return SellerPersonName;
    }

    public void setSellerPersonName(String sellerPersonName) {
        SellerPersonName = sellerPersonName;
    }

    //get bills
    @Ignore
    public StudentsEntity(String billID_str, String bookID_str, String buyerID_str, String ownerStatus_str, String buyerStatus_str,
                          String studentID_str, String book_title_str, String book_description_str, String publishYear_str,
                          String authorID_str, String departmentID_str, String isbn_str, String sellerID_str, String sellerPersonName_str,
                          String sellerUserName_str, String sellerGender_str, String sellerEmail_str, String sellerFacultyID_str,
                          String sellerFireBUiD_str, String buyerID_str1, String buyerPersonName_str, String buyerUserName_str,
                          String buyerGender_str, String buyerEmail_str, String buyerDepartmentID_str, String buyerFireBUiD_str,
                          String created_at, String updated_at,String bookPrice, String x1, String x2) {
        this.BillID=billID_str;
        this.BookID=bookID_str;
        this.BuyerID=buyerID_str;
        this.OwnerStatus=ownerStatus_str;
        this.BuyerStatus=buyerStatus_str;
        this.SellerID=studentID_str;
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.AuthorID=authorID_str;
        this.DepartmentID=departmentID_str;
        this.ISBN_NUM=isbn_str;
        this.SellerID=sellerID_str;
        this.SellerPersonName=sellerPersonName_str;
        this.SellerUserName=sellerUserName_str;
        this.SellerGender=sellerGender_str;
        this.SellerEmail=sellerEmail_str;
        this.SellerDepartmentID=sellerFacultyID_str;
        this.SellerFirebaseUid=sellerFireBUiD_str;
        this.BuyerPName=buyerPersonName_str;
        this.BuyerUserName=buyerUserName_str;
        this.BuyerGender=buyerGender_str;
        this.BuyerEmail=buyerEmail_str;
        this.BuyerDepartmentID=buyerDepartmentID_str;
        this.BuyerFirebaseUiD=buyerFireBUiD_str;
        this.CreatedAt=created_at;
        this.UpdatedAt=updated_at;
        this.BookPrice=bookPrice;
    }

    public String getFirebaseUiD() {
        return FirebaseUiD;
    }

    public void setFirebaseUiD(String firebaseUiD) {
        FirebaseUiD = firebaseUiD;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }


    public String getPivotID() {
        return PivotID;
    }

    public void setPivotID(String pivotID) {
        PivotID = pivotID;
    }

    public String getSellerEmail() {
        return SellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        SellerEmail = sellerEmail;
    }

    public String getSellerGender() {
        return SellerGender;
    }

    public void setSellerGender(String sellerGender) {
        SellerGender = sellerGender;
    }

    public String getSellerUserName() {
        return SellerUserName;
    }

    public void setSellerUserName(String sellerUserName) {
        SellerUserName = sellerUserName;
    }

    public String getSellerDepartmentID() {
        return SellerDepartmentID;
    }

    public void setSellerDepartmentID(String sellerDepartmentID) {
        SellerDepartmentID = sellerDepartmentID;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    // OnSale Books
    @Ignore
    public StudentsEntity(String sellerID_str, String bookID_str, String price_str, String availability_str, String transactionType_str,
                          String bill_status_, String book_title_str, String book_description_str, String publishYear_str,
                          String authorID_str, String departmentID_str, String isbn_str, String photo_str, String studentID_str,
                          String personName_str, String userName_str, String gender_str, String email_str, String departmentID_str1,
                          String department_name, String author_name, String pivot_id_str, String firbase_id_STR, String bookStatus_STR,
                          String billID, String buyerID, String ownerStatus, String buyerStatus, String buyerFirebaseUiD) {
        this.PivotID=pivot_id_str;
        this.BookOwnerID=sellerID_str;
        this.BookID=bookID_str;
        this.BookPrice=price_str;
        this.Availability=availability_str;
        this.TransactionType=transactionType_str;
        this.BillStatus=bill_status_;
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.AuthorID=authorID_str;
        this.DepartmentID=departmentID_str;
        this.ISBN_NUM=isbn_str;
        this.BookImage=photo_str;
        this.PersonName=personName_str;
        this.SellerUserName=userName_str;
        this.SellerGender=gender_str;
        this.SellerEmail=email_str;
        this.SellerDepartmentID=departmentID_str1;
        this.AuthorTitle=author_name;
        this.DepartmentName=department_name;
        this.SellerFirebaseUid=firbase_id_STR;
        this.BookStatus=bookStatus_STR;
        this.BillID=billID; this.BuyerID=buyerID;
        this.OwnerStatus=ownerStatus;
        this.BuyerStatus=buyerStatus;
        this.BuyerFirebaseUiD=buyerFirebaseUiD;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    String UserID;



    String ServerMessage;

    public String getServerMessage() {
        return ServerMessage;
    }

    public void setServerMessage(String serverMessage) {
        ServerMessage = serverMessage;
    }

    String FacultyName;
    String FacultyID;

    @Ignore
    public StudentsEntity(String book_title_str, String book_description_str, String publishYear_str,
                          String photo_str, String isbn_str, String authorName_str, String department_str) {
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.BookPhoto=photo_str;
        this.ISBN_NUM=isbn_str;
        this.AuthorTitle=authorName_str;
        this.DepartmentName=department_str;
    }

    @Ignore
    public StudentsEntity(String book_title_str, String book_description_str, String publishYear_str,
                          String photo_str, String isbn_str, String authorName_str, String bookID,String s, String s2, String s3) {
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.BookPhoto=photo_str;
        this.ISBN_NUM=isbn_str;
        this.AuthorTitle=authorName_str;
        this.BookID=bookID;
    }




    //suggest Books
    @Ignore
    public StudentsEntity(String book_title_str, String book_description_str, String publishYear_str, String photo_str, String isbn_str, String authorName_str, String price_str, String BookStatus, String Book_Availability, String Transaction_Type, String book_id) {
        this.BookTitle=book_title_str;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.BookImage=photo_str;
        this.ISBN_NUM=isbn_str;
        this.AuthorTitle=authorName_str;
        this.BookPrice=price_str;
        this.BookStatus=BookStatus;
        this.Availability=Book_Availability;
        this.TransactionType=Transaction_Type;
        this.BookID=book_id;
    }

    String DoneStatus   ;

    public String getDoneStatus() {
        return DoneStatus;
    }

    public void setDoneStatus(String doneStatus) {
        DoneStatus = doneStatus;
    }

    @Ignore
    public StudentsEntity(String msgStr) {
        this.Exception=msgStr;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getISBN_NUM() {
        return ISBN_NUM;
    }

    public void setISBN_NUM(String ISBN_NUM) {
        this.ISBN_NUM = ISBN_NUM;
    }

    public String getBookPhoto() {
        return BookPhoto;
    }

    public void setBookPhoto(String bookPhoto) {
        BookPhoto = bookPhoto;
    }

    public String getBookDescription() {
        return BookDescription;
    }

    public void setBookDescription(String bookDescription) {
        BookDescription = bookDescription;
    }

    public String getPublishYear() {
        return PublishYear;
    }

    public void setPublishYear(String publishYear) {
        PublishYear = publishYear;
    }

    //added book details
    @Ignore
    public StudentsEntity(String book_title_str, String book_id,String book_description_str, String publishYear_str, String photo_str, String isbn_str, String details, String details1) {
        this.BookTitle=book_title_str;
        this.BookID=book_id;
        this.BookDescription=book_description_str;
        this.PublishYear=publishYear_str;
        this.BookImage=photo_str;
        this.ISBN_NUM=isbn_str;
    }

    public String getFacultyName() {
        return FacultyName;
    }

    public void setFacultyName(String facultyName) {
        FacultyName = facultyName;
    }

    public String getFacultyID() {
        return FacultyID;
    }

    public void setFacultyID(String facultyID) {
        FacultyID = facultyID;
    }

    @NonNull
    @ColumnInfo(name = "AuthorTitle")
    String AuthorTitle;

    @NonNull
    @ColumnInfo(name = "AuthorID")
    public String AuthorID;

    public String getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(String authorID) {
        AuthorID = authorID;
    }

    public String getAuthorTitle() {
        return AuthorTitle;
    }

    public void setAuthorTitle(String authorTitle) {
        AuthorTitle = authorTitle;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    String Exception;

    public String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
    }


    public String getAPI_TOKEN() {
        return API_TOKEN;
    }

    public void setAPI_TOKEN(String API_TOKEN) {
        this.API_TOKEN = API_TOKEN;
    }

    public StudentsEntity(){

    }

    //Registration Constructor
    @Ignore
    public StudentsEntity(String api_token_str, String personName_str, String email_str, String userName_str, String gender_str, String department_str, String user_id,String firbase_id,String x2) {
        this.API_TOKEN=api_token_str;
        this.PersonName=personName_str;
        this.Email=email_str;
        this.UserName=userName_str;
        this.Gender=gender_str;
        this.DepartmentName=department_str;
        this.UserID=user_id;
        this.FirebaseUiD=firbase_id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    @Ignore
    public StudentsEntity(String DepartmentID, String DepartmentName){
        this.DepartmentID=DepartmentID;
        this.DepartmentName=DepartmentName;
    }

    @Ignore
    public StudentsEntity(String authorID, String authorTitle,String x, String y){
        this.AuthorID=authorID;
        this.AuthorTitle=authorTitle;
    }

    @Ignore
    public StudentsEntity(String BookID, String BookName,String x){
        this.BookID=BookID;
        this.BookTitle=BookName;
    }

    public String getBookOwnerID() {
        return BookOwnerID;
    }

    public void setBookOwnerID(String bookOwnerID) {
        BookOwnerID = bookOwnerID;
    }

    public String getBookStatus() {
        return BookStatus;
    }

    public void setBookStatus(String bookStatus) {
        BookStatus = bookStatus;
    }


    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }

    public String getBookImage() {
        return BookImage;
    }

    public void setBookImage(String bookImage) {
        BookImage = bookImage;
    }
}

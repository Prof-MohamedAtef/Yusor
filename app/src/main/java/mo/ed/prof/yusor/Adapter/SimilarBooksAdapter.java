package mo.ed.prof.yusor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import mo.ed.prof.yusor.R;
import mo.ed.prof.yusor.helpers.Config;
import mo.ed.prof.yusor.helpers.Room.StudentsEntity;

/**
 * Created by Prof-Mohamed Atef on 2/12/2019.
 */

public class SimilarBooksAdapter extends RecyclerView.Adapter<SimilarBooksAdapter.ViewHOlder> implements Serializable {

    Context mContext;
    ArrayList<StudentsEntity> feedItemList;
    boolean TwoPane;
    private String BaseImage;

    public SimilarBooksAdapter(Context mContext, ArrayList<StudentsEntity> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;
    }

    @NonNull
    @Override
    public SimilarBooksAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_books_list_item, null);
        RecyclerView.ViewHolder viewHolder = new SimilarBooksAdapter.ViewHOlder(view);
        if (feedItemList != null && feedItemList.size() > 0) {
            if (feedItemList.get(0).getBookTitle().equals("not offered")) {
                return null;
            } else {
                return (SimilarBooksAdapter.ViewHOlder) viewHolder;
            }
        } else {
            return (SimilarBooksAdapter.ViewHOlder) viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarBooksAdapter.ViewHOlder holder, final int position) {
        final StudentsEntity feedItem = feedItemList.get(position);
        if (feedItem != null) {
            if (feedItem.getBookTitle() != null) {
                if (!feedItem.getBookTitle().equals("not offered")){
                    holder.Title.setText(feedItem.getBookTitle());
                    if (feedItem.getAuthorTitle() != null) {
                        holder.AuthorName.setText(feedItem.getAuthorTitle());
                        if (feedItem.getAvailability() != null) {
                            if (feedItem.getAvailability().equals("0")) {
                                holder.BookAvailability.setText(mContext.getResources().getString(R.string.book_exists));
                                holder.BookAvailability.setTextColor(mContext.getResources().getColor(R.color.green));
                            } else {
                                holder.BookAvailability.setText(mContext.getResources().getString(R.string.book_not_exists));
                                holder.BookAvailability.setTextColor(mContext.getResources().getColor(R.color.red));
                            }
                            if (feedItem.getBookStatus() != null) {

                                if (feedItem.getBookStatus().equals(mContext.getResources().getString(R.string.new_book))) {
                                    holder.BookStatus.setText(mContext.getResources().getString(R.string._new));
                                } else if (feedItem.getBookStatus().equals(mContext.getString(R.string.intermediate_book))) {
                                    holder.BookStatus.setText(mContext.getResources().getString(R.string.Intermediate));
                                } else if (feedItem.getBookStatus().equals(mContext.getString(R.string.not_bad_book))) {
                                    holder.BookStatus.setText(mContext.getResources().getString(R.string.not_bad));
                                }
                                if (feedItem.getBookPrice() != null) {
                                    holder.Price.setText(feedItem.getBookPrice());
                                    if (feedItem.getBookImage() != null) {
                                        BaseImage= Config.IMAGEBaseUrl+feedItem.getBookImage();
                                        Picasso.with(mContext).load(BaseImage)
                                                .error(R.drawable.logo)
                                                .into(holder.Image);
                                    }
                                } else {
                                    holder.Price.setText("");
                                }
                            } else {
                                holder.BookStatus.setText("");
                            }
                        } else {
                            holder.BookAvailability.setText("");
                        }
                    } else {
                        holder.AuthorName.setText("");
                    }
                }else {

                }
            } else {
                holder.Title.setText("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class ViewHOlder extends RecyclerView.ViewHolder {
        protected TextView BookStatus;
        protected TextView BookAvailability;
        protected TextView AuthorName;
        protected TextView Title;
        protected TextView Price;
        protected ImageView Image;

        public ViewHOlder(View converview) {
            super(converview);
            this.Title = (TextView) converview.findViewById(R.id.book_name);
            this.Price= (TextView) converview.findViewById(R.id.book_price_val);
            this.AuthorName =(TextView)converview.findViewById(R.id.author_name);
            this.BookAvailability =(TextView)converview.findViewById(R.id.book_availability_val);
            this.BookStatus=(TextView)converview.findViewById(R.id.book_status_val);
            this.Image =(ImageView)converview.findViewById(R.id.book_image);
        }
    }
}
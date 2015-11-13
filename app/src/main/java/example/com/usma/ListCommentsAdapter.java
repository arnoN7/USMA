package example.com.usma;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arnaud Rover on 12/11/2015.
 */
public class ListCommentsAdapter extends
        RecyclerView.Adapter<ListCommentsAdapter.CommentViewHolder> {
    private List<CommentSportEvent> comments;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy H:m",
            Locale.FRANCE);

    public ListCommentsAdapter(List<CommentSportEvent> comments) {
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment, parent, false);
        // set the view's size, margins, paddings and layout parameters
        CommentViewHolder vh = new CommentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentSportEvent comment = comments.get(position);
        Calendar cal = USMAApplication.DateToCalendar(comment.getCreatedAt());
        holder.mTextAuthor.setText(ParseUser.getCurrentUser().get(User.FIRSTNAME) + " " +
                ParseUser.getCurrentUser().get(User.NAME));
        holder.mTextComment.setText(comment.getText());
        holder.mDateComment.setText(dateFormatter.format(cal.getTime()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextAuthor, mTextComment, mDateComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mTextAuthor = (TextView) itemView.findViewById(R.id.comment_author);
            mTextComment = (TextView) itemView.findViewById(R.id.comment_message);
            mDateComment = (TextView) itemView.findViewById(R.id.comment_date);
        }
    }
}

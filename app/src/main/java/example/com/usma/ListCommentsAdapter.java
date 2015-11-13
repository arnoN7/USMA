package example.com.usma;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arnaud Rover on 12/11/2015.
 */
public class ListCommentsAdapter extends
        RecyclerView.Adapter<ListCommentsAdapter.CommentViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private List<CommentSportEvent> comments;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy H:m",
            Locale.FRANCE);
    private SportEvent sportEvent;
    private boolean isCommentsLoading;

    private static TextView mTitleEvent, mDescriptionEvent, mAddressEvent, mDayOfWeek, mDayOfMonth, mYear,
            mMonth, mNewTextComment, mCommentLoadingTitle;
    private static Button mButtonNewComment;
    private static ProgressBar progressBar;
    private static ProgressBar progressBarNewComment;
    private static Activity activity;

    public ListCommentsAdapter(Activity activity, List<CommentSportEvent> comments,
                               SportEvent sportEvent, boolean isCommentsLoading) {
        this.comments = comments;
        this.sportEvent = sportEvent;
        this.activity = activity;
        this.isCommentsLoading = isCommentsLoading;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CommentViewHolder vh = new CommentViewHolder(v, TYPE_ITEM);
            return vh;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_consult_sport_header, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CommentViewHolder vh = new CommentViewHolder(v, TYPE_HEADER);
            if (isCommentsLoading) {
                showLoadingComment(true);
            } else {
                showLoadingComment(false);
            }
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_consult_sport_footer, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CommentViewHolder vh = new CommentViewHolder(v, TYPE_FOOTER);
            return vh;
            //Footer
        }
        // create a new view

    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        if(holder.holderID == TYPE_HEADER) {
            mTitleEvent.setText(sportEvent.getName());
            mDescriptionEvent.setText(sportEvent.getDescription());
            mAddressEvent.setText(sportEvent.getAddress());
            Calendar cal = USMAApplication.DateToCalendar(sportEvent.getDate());
            mYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
            mMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            mDayOfMonth.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            mDayOfWeek.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK,
                    Calendar.LONG, Locale.getDefault()));
        } else if (holder.holderID == TYPE_ITEM) {
            CommentSportEvent comment = comments.get(position - 1);
            Calendar cal = USMAApplication.DateToCalendar(comment.getCreatedAt());
            holder.mTextAuthor.setText(ParseUser.getCurrentUser().get(User.FIRSTNAME) + " " +
                    ParseUser.getCurrentUser().get(User.NAME));
            holder.mTextComment.setText(comment.getText());
            holder.mDateComment.setText(dateFormatter.format(cal.getTime()));

        } else {
            //Footer
            mButtonNewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CommentSportEvent comment = new CommentSportEvent();
                    comment.setAuthor(ParseUser.getCurrentUser().getString(User.FIRSTNAME) + " " +
                            ParseUser.getCurrentUser().getString(User.NAME));
                    comment.setText(mNewTextComment.getText().toString());
                    comment.setSportEvent(sportEvent);
                    progressBarNewComment.setVisibility(View.VISIBLE);
                    mButtonNewComment.setVisibility(View.INVISIBLE);
                    comment.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                sportEvent.getComments(new FindCallback<CommentSportEvent>() {
                                    @Override
                                    public void done(List<CommentSportEvent> objects, ParseException e) {
                                        progressBarNewComment.setVisibility(View.GONE);
                                        mButtonNewComment.setVisibility(View.VISIBLE);
                                        mNewTextComment.setText("");
                                        ((MainActivity) activity).hideKeyboard();
                                        comments = objects;
                                        notifyDataSetChanged();
                                    }
                                }, true);
                            }
                        }
                    });
                    try {
                        comment.pin();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return comments.size() + 2; // the number of items in the list will be +2 including the header and footer view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        int viewType = TYPE_ITEM;
        if (position == 0) {
            viewType = TYPE_HEADER;
        }
        if(position == getItemCount() - 1) {
            viewType = TYPE_FOOTER;
        }
        return viewType;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
    private boolean isPositionFooter (int position) {
        return position == getItemCount();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private int holderID;

        private TextView mTextAuthor, mTextComment, mDateComment;


        public CommentViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == TYPE_ITEM) {
                mTextAuthor = (TextView) itemView.findViewById(R.id.comment_author);
                mTextComment = (TextView) itemView.findViewById(R.id.comment_message);
                mDateComment = (TextView) itemView.findViewById(R.id.comment_date);
                holderID = TYPE_ITEM;
            } else if (viewType == TYPE_HEADER) {
                mTitleEvent = (TextView) itemView.findViewById(R.id.sport_event_name);
                mDescriptionEvent = (TextView) itemView.findViewById(R.id.sport_event_description);
                mAddressEvent = (TextView) itemView.findViewById(R.id.sport_event_address);
                mDayOfWeek = (TextView) itemView.findViewById(R.id.day_of_week_event);
                mDayOfMonth = (TextView) itemView.findViewById(R.id.day_of_month_event);
                mMonth = (TextView) itemView.findViewById(R.id.month_event);
                mYear = (TextView) itemView.findViewById(R.id.year_event);
                progressBar = (ProgressBar) itemView.findViewById(R.id.comment_loading_progress);
                mCommentLoadingTitle = (TextView) itemView.findViewById(R.id.comment_loading_title);
                holderID = TYPE_HEADER;
            } else {
                //Footer
                mNewTextComment = (TextView) itemView.findViewById(R.id.new_comment);
                mButtonNewComment = (Button) itemView.findViewById(R.id.button_new_comment);
                progressBarNewComment = (ProgressBar) itemView.
                        findViewById(R.id.comment_new_progress);
                holderID = TYPE_FOOTER;
            }

        }
    }

    public void showLoadingComment(boolean show) {
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
            mCommentLoadingTitle.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            mCommentLoadingTitle.setVisibility(View.GONE);
        }
    }
    public void showError(boolean show) {
        if(show) {
            progressBar.setVisibility(View.GONE);
            mCommentLoadingTitle.setVisibility(View.VISIBLE);
            mCommentLoadingTitle.setText(R.string.comment_cant_load);
            mCommentLoadingTitle.setTextColor(activity.getResources().getColor(R.color.darkred));
        } else {
            progressBar.setVisibility(View.GONE);
            mCommentLoadingTitle.setVisibility(View.GONE);
        }
    }
}

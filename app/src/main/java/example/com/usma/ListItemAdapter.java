package example.com.usma;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arnaud Rover on 18/10/15.
 */
public abstract class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder> {
    protected Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener{
        // each data item is just a string in this case
        public TextView mTextViewName, mTextViewDescription, mTextViewYear, mTextViewMonth,
                mTextViewDay, mTextViewDayOfWeek, mTextViewTypeSportEvent;
        public ImageView mIconItem;
        private ClickListener clickListener;
        private Button mSignInGroupButton;
        private PopupMenu.OnMenuItemClickListener onMenuItemClickListener;
        private ProgressBar progressBar;

        public ItemViewHolder(View v, final Context context) {
            super(v);
            mTextViewName = (TextView) v.findViewById(R.id.item_name);
            mTextViewDescription = (TextView) v.findViewById(R.id.item_description);
            mTextViewYear = (TextView) v.findViewById(R.id.year_list);
            mTextViewMonth = (TextView) v.findViewById(R.id.month_list);
            mTextViewDay = (TextView) v.findViewById(R.id.day_of_month_list);
            mTextViewDayOfWeek = (TextView) v.findViewById(R.id.day_of_week_list);
            mIconItem = (ImageView) v.findViewById(R.id.image_item);
            mTextViewTypeSportEvent = (TextView) v.findViewById(R.id.type_details);
            mSignInGroupButton = (Button) v.findViewById(R.id.sign_in_group_button);
            progressBar = (ProgressBar) v.findViewById(R.id.sign_in_group_progress);

            v.setClickable(true);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        /* Interface for handling clicks - both normal and long ones. */
        public interface ClickListener {

            /**
             * Called when the view is clicked.
             *
             * @param v view that is clicked
             * @param position of the clicked item
             * @param isLongClick true if long click, false otherwise
             */
            public void onClick(View v, int position, boolean isLongClick);

        }

        /* Setter for listener. */
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {

            // If not long clicked, pass last variable as false.
            if(clickListener != null) {
                clickListener.onClick(v, getPosition(), false);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            // If long clicked, passed last variable as true.
            clickListener.onClick(v, getPosition(), true);
            return true;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListItemAdapter(Context context) {
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ItemViewHolder vh = new ItemViewHolder(v, context);
        return vh;
    }

    public void onBindSportEventView(final SportEvent sportEvent, ItemViewHolder holder) {
        Calendar cal = USMAApplication.DateToCalendar(sportEvent.getDate());
        holder.mTextViewName.setText(sportEvent.getName());
        holder.mTextViewDescription.setText(sportEvent.getDescription());

        holder.mTextViewYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
        holder.mTextViewMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        holder.mTextViewDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        holder.mTextViewDayOfWeek.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.LONG, Locale.getDefault()));
        SportsEventType sportEventType = sportEvent.getSportEventType(context.getResources(),
                sportEvent.getType(context.getResources()));
        holder.mIconItem.setImageResource(sportEventType.getIconID());
        holder.mTextViewTypeSportEvent.setText(sportEventType.
                getSportEventType(context.getResources()));
        holder.mTextViewYear.setVisibility(View.VISIBLE);
        holder.mTextViewMonth.setVisibility(View.VISIBLE);
        holder.mTextViewDay.setVisibility(View.VISIBLE);
        holder.mTextViewDayOfWeek.setVisibility(View.VISIBLE);
        holder.mTextViewTypeSportEvent.setVisibility(View.VISIBLE);
        holder.setClickListener(new ItemViewHolder.ClickListener() {
            @Override
            public void onClick(View v, final int position, boolean isLongClick) {
                if(isLongClick) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.inflate(R.menu.context_list_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // handle item selection
                            NavigationMenu type = sportEvent.getType(context.getResources());
                            switch (item.getItemId()) {
                                case R.id.delete_item:
                                        ((MainActivity) context).getSportEvent(type).
                                                get(position).deleteEventually();
                                        /*((MainActivity) context).getSportEvent(type).
                                                get(position).unpin();*/
                                        ((ListFragmentSportEvent) ((MainActivity) context).
                                                getLoadedRootFragment(type)).notifyDataSetChanged();

                                    return true;
                                case R.id.modify_item:
                                    ((ListFragmentSportEvent) ((MainActivity) context).
                                            getLoadedRootFragment(type)).modifyItem(position);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                } else {
                    ((ListFragment)((MainActivity)context).
                            getCurrentFragment()).consultItemAction(position);
                }
            }
        });
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public abstract void onBindViewHolder(ItemViewHolder holder, int position);

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public abstract int getItemCount();

    public static class ListItemAdapterTraining extends ListItemAdapter {
        private List<SportEvent> trainings;
        public ListItemAdapterTraining(Context context) {
            super(context);
            this.trainings = ((MainActivity) context).getSportEvent(NavigationMenu.TRAINING);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            SportEvent currentRace = trainings.get(position);
            onBindSportEventView(currentRace, holder);
        }

        @Override
        public int getItemCount() {
            return trainings.size();
        }
    }

    public static class ListItemAdapterRace extends ListItemAdapter {
        private List<SportEvent> races;

        public ListItemAdapterRace(Context context) {
            super(context);
            this.races = ((MainActivity)context).getSportEvent(NavigationMenu.RACES);
        }
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            SportEvent currentRace = races.get(position);
            onBindSportEventView(currentRace, holder);
        }

        @Override
        public int getItemCount() {
            return races.size();
        }

    }

    public static class ListItemAdapterUser extends ListItemAdapter {
        private List<ParseUser> users;

        public ListItemAdapterUser(Context context) {
            super(context);
            this.users = ((MainActivity)context).getUsers();
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            ParseUser user = users.get(position);
            holder.mTextViewName.setText(user.get(User.FIRSTNAME) + " " +
                    user.get(User.NAME));
            holder.mTextViewDescription.setText(user.getEmail());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    public static class ListItemAdapterGroup extends ListItemAdapter {
        private List<ParseRole> groups;

        public ListItemAdapterGroup(Context context) {
            super(context);
            if (context != null) {
                this.groups = ((MainActivity) context).getGroups(false);
            }
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            ParseRole group = groups.get(position);
            holder.mTextViewName.setText(group.getString(GroupUsers.NAME));
            holder.mTextViewDescription.setText(group.getString(GroupUsers.DESCRIPTION));
            holder.mSignInGroupButton.setVisibility(View.VISIBLE);
            group.getUsers().getQuery().
                    findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getUsername().
                                        equals(ParseUser.getCurrentUser().getUsername())) {
                                    setSignedInGroup(holder);
                                    break;
                                }
                            }
                        }
                    });

            holder.mSignInGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mSignInGroupButton.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    ParseRole group = groups.get(position);
                    if (holder.mSignInGroupButton.getText().toString().
                            equals(context.getString(R.string.action_sign_in_group))) {
                        group.getUsers().add(ParseUser.getCurrentUser());
                        Toast.makeText(context, group.getName() + " group " + position, Toast.LENGTH_SHORT).show();
                    } else {
                        group.getUsers().remove(ParseUser.getCurrentUser());
                    }
                    try {
                        group.save();
                        ((MainActivity) context).initFragments();
                        holder.progressBar.setVisibility(View.GONE);
                        holder.mSignInGroupButton.setVisibility(View.VISIBLE);
                        if (holder.mSignInGroupButton.getText().toString().
                                equals(context.getString(R.string.action_sign_in_group))) {
                            setSignedInGroup(holder);
                        } else {
                            setSignedOutGroup(holder);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void setSignedOutGroup(ItemViewHolder holder) {
            holder.mSignInGroupButton.setText(context.getString(R.string.action_sign_in_group));
            holder.mSignInGroupButton.setBackground(context.getResources().
                    getDrawable(R.drawable.button_light));
            holder.mSignInGroupButton.setTextColor(context.getResources().getColor(R.color.black));

        }

        private void setSignedInGroup(ItemViewHolder holder) {
            holder.mSignInGroupButton.setText(context.
                    getString(R.string.signed_in_group));
            holder.mSignInGroupButton.setBackground(context.getResources().
                    getDrawable(R.drawable.button_dark));
            holder.mSignInGroupButton.setTextColor(context.getResources().getColor(R.color.white));
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }
    }
}


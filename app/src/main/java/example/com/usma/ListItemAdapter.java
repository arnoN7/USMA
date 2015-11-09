package example.com.usma;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseRole;
import com.parse.ParseUser;

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
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewName, mTextViewDescription, mTextViewYear, mTextViewMonth,
                mTextViewDay, mTextViewDayOfWeek;
        public ItemViewHolder(View v, final Context context) {
            super(v);
            mTextViewName = (TextView) v.findViewById(R.id.item_name);
            mTextViewDescription = (TextView) v.findViewById(R.id.item_description);
            mTextViewYear = (TextView) v.findViewById(R.id.year_list);
            mTextViewMonth = (TextView) v.findViewById(R.id.month_list);
            mTextViewDay = (TextView) v.findViewById(R.id.day_of_month_list);
            mTextViewDayOfWeek = (TextView) v.findViewById(R.id.day_of_week_list);

            v.setClickable(true);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListFragment)((MainActivity)context).
                            getCurrentFragment()).consultItemAction(getPosition());
                }
            });
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

    public void onBindSportEventView(SportEvent sportEvent, ItemViewHolder holder) {
        Calendar cal = USMAApplication.DateToCalendar(sportEvent.getDate());
        holder.mTextViewName.setText(sportEvent.getName());
        holder.mTextViewDescription.setText(sportEvent.getDescription());

        holder.mTextViewYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
        holder.mTextViewMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        holder.mTextViewDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        holder.mTextViewDayOfWeek.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.LONG, Locale.getDefault()));

        holder.mTextViewYear.setVisibility(View.VISIBLE);
        holder.mTextViewMonth.setVisibility(View.VISIBLE);
        holder.mTextViewDay.setVisibility(View.VISIBLE);
        holder.mTextViewDayOfWeek.setVisibility(View.VISIBLE);
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
            this.groups = ((MainActivity) context).getGroups();
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            ParseRole group = groups.get(position);
            holder.mTextViewName.setText(group.getString(GroupUsers.NAME));
            holder.mTextViewDescription.setText(group.getString(GroupUsers.DESCRIPTION));
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }
    }
}


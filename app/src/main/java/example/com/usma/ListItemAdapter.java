package example.com.usma;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arnaud Rover on 18/10/15.
 */
public abstract class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder> {
    protected List<Item> mDataset;
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
    public ListItemAdapter(List<Item> myDataset, Context context) {
        mDataset = myDataset;
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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextViewName.setText(mDataset.get(position).getName());
        holder.mTextViewDescription.setText(mDataset.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ListItemAdapterTraining extends ListItemAdapter {
        public ListItemAdapterTraining(List<Item> myDataset, Context context) {
            super(myDataset, context);
        }
    }

    public static class ListItemAdapterRace extends ListItemAdapter {
        public ListItemAdapterRace(List<Item> myDataset, Context context) {
            super(myDataset, context);
        }
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            super.onBindViewHolder(holder,position);
            holder.mTextViewYear.setText(mDataset.get(position).getName());
        }

    }

    public static class ListItemAdapterUser extends ListItemAdapter {
        public ListItemAdapterUser(List<Item> myDataset, Context context) {
            super(myDataset, context);
        }
    }

    public static class ListItemAdapterGroup extends ListItemAdapter {
        public ListItemAdapterGroup(List<Item> myDataset, Context context) {
            super(myDataset, context);
        }
    }
}


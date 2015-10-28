package example.com.usma;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseImageView;

import java.util.List;

/**
 * Created by Arnaud Rover on 25/10/2015.
 */
public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.ViewHolderListFragment> {
    private List<Item> mDataset;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolderListFragment extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    CardView cardView;
    TextView title;
    TextView description;
    ParseImageView imageView;
    public ViewHolderListFragment(View itemView) {
        super(itemView);
        this.cardView = (CardView) itemView.findViewById(R.id.cv);
        this.title = (TextView) itemView.findViewById(R.id.title_item);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListFragmentAdapter(List<Item> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolderListFragment onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        ViewHolderListFragment vh = new ViewHolderListFragment(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolderListFragment holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset.get(position).getName());
        holder.description.setText(mDataset.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}


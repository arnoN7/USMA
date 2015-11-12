package example.com.usma;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseRole;

import java.util.List;

/**
 * Created by Arnaud Rover on 11/11/2015.
 */
public class ListGroupAdapter extends ArrayAdapter<ParseRole> implements CompoundButton.OnCheckedChangeListener {
    List<ParseRole> groups;
    SparseBooleanArray mCheckStates;

    //tweets est la liste des models à afficher
    public ListGroupAdapter(Context context, List<ParseRole> groups) {
        super(context, 0, groups);
        this.groups = groups;
        mCheckStates = new SparseBooleanArray(groups.size());
    }

    public int getHeight(ListView listView) {
        View item = getView(0, null, listView);
        item.measure(0, 0);
        int height = item.getMeasuredHeight();
        return height * getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_choose_group,
                    parent, false);
        }

        ChooseViewHolder viewHolder = (ChooseViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ChooseViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_group);
            viewHolder.titleGroup = (TextView) convertView.findViewById(R.id.group_name_checkbox);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        ParseRole group = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        viewHolder.titleGroup.setText(group.getName());
        viewHolder.checkBox.setTag(position);
        viewHolder.checkBox.setChecked(mCheckStates.get(position, false));
        viewHolder.checkBox.setOnCheckedChangeListener(this);

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }

    public SparseBooleanArray getmCheckStates() {
        return mCheckStates;
    }

    public void setChecked(List<ParseRole> groupsChecked) {
        for (ParseRole group: groupsChecked
             ) {
            for (int i = 0; i < groups.size(); i++) {
                if(groups.get(i).equals(group)) {
                    mCheckStates.put(i, true);
                    break;
                }
            }
        }
    }

    private class ChooseViewHolder{
        CheckBox checkBox;
        TextView titleGroup;
    }
}

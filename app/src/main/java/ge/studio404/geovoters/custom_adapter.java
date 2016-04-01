package ge.studio404.geovoters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class custom_adapter extends ArrayAdapter<String> {

    public custom_adapter(Context context, String[] questions) {
        super(context, R.layout.custom_row ,questions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater giosInflate = LayoutInflater.from(getContext());
        View customView = giosInflate.inflate(R.layout.custom_row, parent, false);
        String singleText = getItem(position);
        TextView questionText = (TextView)customView.findViewById(R.id.questionText);
        questionText.setText(singleText);
        return customView;
    }
}

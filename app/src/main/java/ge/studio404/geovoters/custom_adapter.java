package ge.studio404.geovoters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.StringTokenizer;


public class custom_adapter extends ArrayAdapter<String> {

    String singleText;
    String dateAndMember;
    String text;
    String date;
    String member;
    public custom_adapter(Context context, String[] questions) {
        super(context, R.layout.custom_row, questions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater giosInflate = LayoutInflater.from(getContext());
        View customView = giosInflate.inflate(R.layout.custom_row, parent, false);

        singleText = getItem(position);
        try {
            StringTokenizer tokens = new StringTokenizer(singleText, "^");
            while(tokens.hasMoreTokens()) {
                text = tokens.nextToken();
                dateAndMember = tokens.nextToken();
                StringTokenizer tokens2 = new StringTokenizer(dateAndMember, "@");
                while(tokens2.hasMoreTokens()) {
                    date = tokens2.nextToken();
                    member = tokens2.nextToken();
                }
            }
        }catch (Exception e){
            Log.i("Error_msg", e.toString());
        }

        TextView questionText = (TextView)customView.findViewById(R.id.questionText);
        TextView tarigi = (TextView)customView.findViewById(R.id.tarigi);
        TextView monawile = (TextView)customView.findViewById(R.id.monawile);

        questionText.setText(text);
        tarigi.setText(date);
        monawile.setText(member);

        return customView;
    }
}

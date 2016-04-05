package ge.studio404.geovoters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class custom_adapter extends ArrayAdapter<String> {

    String singleText;
    String dateAndMember;
    String text;
    String date;
    public Context mcon;
    String lastItemSplitted;
    String member;




    public custom_adapter(Context context, ArrayList<String> questions) {
        super(context, R.layout.custom_row, questions);
        mcon = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater giosInflate = LayoutInflater.from(getContext());
            convertView = giosInflate.inflate(R.layout.custom_row, parent, false);

        }
        singleText = getItem(position);
        String itemIdx = "";
        try {
            StringTokenizer tokens = new StringTokenizer(singleText, "^");
            while (tokens.hasMoreTokens()) {
                text = tokens.nextToken();
                dateAndMember = tokens.nextToken();
                StringTokenizer tokens2 = new StringTokenizer(dateAndMember, "#");
                while (tokens2.hasMoreTokens()) {
                    date = tokens2.nextToken();
                    lastItemSplitted = tokens2.nextToken();
                    StringTokenizer tokens3 = new StringTokenizer(lastItemSplitted, "%");
                    member = tokens3.nextToken();
                    itemIdx = tokens3.nextToken();
                }
            }
        } catch (Exception e) {
            Log.i("Error_msg", e.toString());
        }
        TextView questionText = (TextView)convertView.findViewById(R.id.questionText);
        TextView tarigi = (TextView)convertView.findViewById(R.id.tarigi);
        TextView monawile = (TextView)convertView.findViewById(R.id.monawile);
        TextView itemid = (TextView)convertView.findViewById(R.id.itemid);


        questionText.setText(text);
        tarigi.setText(date);
        monawile.setText(member);
        itemid.setText("# "+itemIdx);

        final String extraText = text;
        final String extraDate = date;
        final String extraMember = member;

        Button xmismicemaButton = (Button)convertView.findViewById(R.id.xmismicemaButton);
        xmismicemaButton.setTag(itemIdx);
        xmismicemaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tags = v.getTag().toString();
                Intent i = new Intent(mcon, make_a_vote.class);
                i.putExtra("itemidx", tags);
                i.putExtra("itemQuestion", extraText);
                i.putExtra("itemMember", extraMember);
                i.putExtra("itemDate", extraDate);
                mcon.startActivity(i);
            }
        });


        //itemid.setText("# "+itemIdx);

        return convertView;
    }
}

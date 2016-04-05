package ge.studio404.geovoters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class custom_adapter2 extends ArrayAdapter<String> {
    public Context mcon;
    String singleText;
    String answeridString;
    String answerPercentigeString;

    public custom_adapter2(Context context, ArrayList<String> questions) {
        super(context, R.layout.custom_row, questions);
        mcon = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null) {
            LayoutInflater giosInflate = LayoutInflater.from(getContext());
            convertView = giosInflate.inflate(R.layout.custom_row2, parent, false);
        }

        singleText = getItem(position);
        try{

            StringTokenizer tokens = new StringTokenizer(singleText, "^");
            while (tokens.hasMoreTokens()) {
                answeridString = tokens.nextToken();
                answerPercentigeString = tokens.nextToken();
            }

        }catch (Exception e){
            Log.i("ERROR_MSG",e.toString());
        }

        TextView answerid = (TextView)convertView.findViewById(R.id.answerid);
        TextView answerPercentige = (TextView)convertView.findViewById(R.id.answerPercentige);
        TextView mainVoteBox = (TextView)convertView.findViewById(R.id.mainVoteBox); // 100%
        int getWidth = mainVoteBox.getLayoutParams().width;


        answerPercentige.getLayoutParams().width = ((getWidth * Integer.parseInt(answerPercentigeString)) / 100);

        answerid.setText(answeridString);
        answerPercentige.setText(answerPercentigeString);

        return convertView;
    }
}

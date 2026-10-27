package com.voyager.lcars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/** LCARS number pad. Android Phone handles call confirmation and all calls. */
public class DialerActivity extends Activity {
    private static final int AMBER=0xffffb66e, VIOLET=0xff9188c7, BLUE=0xff90bce3, CORAL=0xfff29d89;
    private String number="";
    private TextView display;
    private Button dial;
    private int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}

    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        if(state!=null) number=state.getString("number", "");
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(Color.BLACK);
        LinearLayout body=new LinearLayout(this);body.setOrientation(LinearLayout.VERTICAL);body.setPadding(dp(18),dp(18),dp(18),dp(24));
        scroll.addView(body,new ScrollView.LayoutParams(-1,-2));setContentView(scroll);
        TextView title=label("NCC-74656 / COMMUNICATIONS",14,BLUE);body.addView(title);
        TextView heading=label("SUBSPACE CHANNEL",28,AMBER);body.addView(heading);
        View strip=new View(this);strip.setBackgroundColor(VIOLET);LinearLayout.LayoutParams line=new LinearLayout.LayoutParams(-1,dp(10));line.setMargins(0,dp(12),0,dp(16));body.addView(strip,line);
        display=label("",32,BLUE);display.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);display.setMinHeight(dp(84));display.setPadding(dp(12),dp(8),dp(12),dp(8));display.setBackground(shape(0xff211b2a));display.setContentDescription("Phone number");display.setTextIsSelectable(true);body.addView(display);
        LinearLayout edit=row();body.addView(edit);
        Button back=button("DELETE",CORAL);back.setContentDescription("Delete last digit");back.setOnClickListener(v->{if(!number.isEmpty())number=number.substring(0,number.length()-1);refresh();});add(edit,back);
        Button clear=button("CLEAR",VIOLET);clear.setOnClickListener(v->{number="";refresh();});add(edit,clear);
        String[] digits={"1","2","3","4","5","6","7","8","9","*","0","#"};
        String[] letters={"","ABC","DEF","GHI","JKL","MNO","PQRS","TUV","WXYZ","","+",""};
        for(int y=0;y<4;y++){
            LinearLayout keys=row();body.addView(keys);
            for(int x=0;x<3;x++){
                int index=y*3+x;String digit=digits[index];Button key=button(digit+(letters[index].isEmpty()?"":"\n"+letters[index]),AMBER);key.setTextSize(22);key.setMinHeight(dp(72));key.setContentDescription(digit.equals("0")?"0, hold for plus":digit);
                key.setOnClickListener(v->append(digit));if(digit.equals("0"))key.setOnLongClickListener(v->{if(number.isEmpty())append("+");else Toast.makeText(this,"Plus belongs at the start",Toast.LENGTH_SHORT).show();return true;});add(keys,key);
            }
        }
        dial=button("CONTINUE TO PHONE",BLUE);dial.setOnClickListener(v->openPhone());LinearLayout.LayoutParams callParams=new LinearLayout.LayoutParams(-1,dp(60));callParams.setMargins(0,dp(14),0,dp(8));body.addView(dial,callParams);
        body.addView(label("Review the number and tap Call in your phone app.\nHold 0 for +. Incoming and active calls use your existing phone app.",14,BLUE));
        Button home=button("RETURN TO BRIDGE",VIOLET);home.setOnClickListener(v->finish());body.addView(home,new LinearLayout.LayoutParams(-1,dp(52)));
        refresh();
    }
    private void append(String digit){if(number.length()>=40){Toast.makeText(this,"Number is too long",Toast.LENGTH_SHORT).show();return;}number+=digit;refresh();}
    private void refresh(){display.setText(number.isEmpty()?"ENTER NUMBER":number);dial.setEnabled(number.matches(".*[0-9].*"));dial.setAlpha(dial.isEnabled()?1f:.5f);}
    private void openPhone(){
        if(!number.matches("\\+?[0-9*#]{1,40}")||!number.matches(".*[0-9].*"))return;
        try{startActivity(new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",number,null)));}
        catch(android.content.ActivityNotFoundException e){Toast.makeText(this,"No phone app available",Toast.LENGTH_LONG).show();}
    }
    @Override protected void onSaveInstanceState(Bundle state){state.putString("number",number);super.onSaveInstanceState(state);}
    private LinearLayout row(){LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);return row;}
    private void add(LinearLayout row,Button button){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.setMargins(dp(3),dp(4),dp(3),dp(4));row.addView(button,p);}
    private GradientDrawable shape(int color){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(16));return d;}
    private Button button(String text,int color){Button b=new Button(this);b.setText(text);b.setTextColor(Color.BLACK);b.setTextSize(16);b.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD));b.setBackground(shape(color));b.setMinHeight(dp(52));b.setPadding(dp(6),dp(8),dp(6),dp(8));return b;}
    private TextView label(String text,int size,int color){TextView v=new TextView(this);v.setText(text);v.setTextSize(size);v.setTextColor(color);v.setTypeface(Typeface.create("sans-serif-condensed",Typeface.NORMAL));v.setPadding(0,dp(6),0,dp(6));return v;}
}

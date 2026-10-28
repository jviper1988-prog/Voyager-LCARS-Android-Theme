package com.jakebrowne.embercitadel;
import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;

public class DialerActivity extends Activity {
 private EditText number;
 private int dp(int n){return EmberStyle.dp(this,n);}
 @Override public void onCreate(Bundle saved){
  super.onCreate(saved);
  LinearLayout safe=new LinearLayout(this);safe.setOrientation(LinearLayout.VERTICAL);safe.setBackgroundColor(EmberStyle.INK);setContentView(safe);EmberStyle.insets(this,safe);
  ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);safe.addView(scroll,new LinearLayout.LayoutParams(-1,-1));
  LinearLayout body=new LinearLayout(this);body.setOrientation(LinearLayout.VERTICAL);body.setPadding(dp(24),dp(12),dp(24),dp(20));scroll.addView(body);
  Button back=EmberStyle.button(this,"Back to Home");back.setOnClickListener(v->finish());body.addView(back,new LinearLayout.LayoutParams(-1,dp(48)));
  TextView title=EmberStyle.text(this,"COMMUNICATIONS",32,EmberStyle.RED,true);title.setPadding(0,dp(20),0,dp(12));body.addView(title);
  number=new EditText(this);number.setSingleLine(true);number.setTextColor(EmberStyle.WHITE);number.setHintTextColor(EmberStyle.MUTED);number.setHint("Phone number");number.setTextSize(30);number.setInputType(InputType.TYPE_CLASS_PHONE);number.setShowSoftInputOnFocus(false);number.setGravity(Gravity.CENTER);number.setContentDescription("Phone number");body.addView(number,new LinearLayout.LayoutParams(-1,dp(72)));
  if(saved!=null)number.setText(saved.getString("number",""));
  String[] digits={"1","2","3","4","5","6","7","8","9","*","0","#"};
  for(int r=0;r<4;r++){LinearLayout row=new LinearLayout(this);body.addView(row);for(int c=0;c<3;c++){String digit=digits[r*3+c];Button key=EmberStyle.button(this,digit);key.setTypeface(EmberStyle.heading(this));key.setTextSize(32);key.setTextColor(EmberStyle.RED);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(68),1);p.setMargins(dp(4),dp(4),dp(4),dp(4));row.addView(key,p);key.setOnClickListener(v->insert(digit));if(digit.equals("0"))key.setOnLongClickListener(v->{insert("+");return true;});}}
  Button delete=EmberStyle.button(this,"Delete • hold to clear");delete.setOnClickListener(v->{int a=Math.max(0,number.getSelectionStart()),b=Math.max(0,number.getSelectionEnd());if(a!=b)number.getText().delete(Math.min(a,b),Math.max(a,b));else if(a>0)number.getText().delete(a-1,a);});delete.setOnLongClickListener(v->{number.setText("");return true;});body.addView(delete,new LinearLayout.LayoutParams(-1,dp(48)));
  Button call=EmberStyle.button(this,"Open in Phone");call.setTextColor(EmberStyle.RED);LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(56));cp.setMargins(0,dp(16),0,dp(8));body.addView(call,cp);call.setOnClickListener(v->{String n=number.getText().toString().trim();if(n.isEmpty()){Toast.makeText(this,"Enter a number first",Toast.LENGTH_SHORT).show();return;}try{startActivity(new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",n,null)));}catch(ActivityNotFoundException|SecurityException e){Toast.makeText(this,"No phone app is available",Toast.LENGTH_SHORT).show();}});
  TextView note=EmberStyle.text(this,"Your phone app confirms and places the call. Hold 0 for +.",14,EmberStyle.MUTED,false);body.addView(note);
 }
 private void insert(String digit){int a=Math.max(0,number.getSelectionStart()),b=Math.max(0,number.getSelectionEnd());number.getText().replace(Math.min(a,b),Math.max(a,b),digit);}
 @Override protected void onSaveInstanceState(Bundle out){out.putString("number",number.getText().toString());super.onSaveInstanceState(out);}
}

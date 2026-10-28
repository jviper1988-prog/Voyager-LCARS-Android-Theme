package com.jakebrowne.embercitadel;
import android.app.Activity;import android.graphics.*;import android.graphics.drawable.GradientDrawable;import android.os.Build;import android.view.*;import android.widget.*;
final class EmberStyle {
 static final int INK=0xff0c0b0d, RED=0xffec735f, WHITE=0xfff2e4df, MUTED=0xffc1aaa1;
 static int dp(Activity a,int n){return Math.round(n*a.getResources().getDisplayMetrics().density);}
 static android.graphics.Typeface heading(Activity a){try{return Typeface.createFromAsset(a.getAssets(),"fonts/Norsebold.otf");}catch(RuntimeException e){return Typeface.create("serif",Typeface.BOLD);}}
 static GradientDrawable panel(){GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{0xff383236,0xff121012,0xff262023});d.setCornerRadius(12);d.setStroke(2,0xff75615b);return d;}
 static TextView text(Activity a,String s,int size,int color,boolean title){TextView t=new TextView(a);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(title)t.setTypeface(heading(a));return t;}
 static Button button(Activity a,String s){Button b=new Button(a);b.setText(s);b.setTextColor(WHITE);b.setTextSize(13);b.setAllCaps(false);b.setBackground(panel());b.setMinHeight(dp(a,48));return b;}
 static void insets(Activity a,View v){if(Build.VERSION.SDK_INT>=30){a.getWindow().setDecorFitsSystemWindows(false);v.setOnApplyWindowInsetsListener((view,w)->{Insets i=w.getInsets(WindowInsets.Type.systemBars()|WindowInsets.Type.displayCutout()|WindowInsets.Type.ime());view.setPadding(i.left,i.top,i.right,i.bottom);return w;});v.requestApplyInsets();}else v.setFitsSystemWindows(true);}
}

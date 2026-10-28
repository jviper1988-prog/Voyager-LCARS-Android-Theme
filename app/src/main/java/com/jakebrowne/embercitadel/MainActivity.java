package com.jakebrowne.embercitadel;

import android.app.*;
import android.app.role.RoleManager;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.Uri;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.*;

public class MainActivity extends Activity {
 private final ArrayList<LauncherActivityInfo> apps=new ArrayList<>();
 private final ArrayList<LauncherActivityInfo> visible=new ArrayList<>();
 private final HashMap<String,Drawable> icons=new HashMap<>();
 private final ExecutorService worker=Executors.newSingleThreadExecutor();
 private final Handler timer=new Handler(Looper.getMainLooper());
 private Favorites favorites;
 private SharedPreferences prefs;
 private LinearLayout home, drawer, favoritesBox;
 private TextView clock,date,battery,count,empty;
 private EditText search;
 private GridView grid;
 private AppAdapter adapter;
 private boolean loading=false,showDrawer=false;
 private String loadError="";
 private final Runnable tick=new Runnable(){public void run(){updateClock();timer.postDelayed(this,15000);}};
 private int dp(int n){return EmberStyle.dp(this,n);}
 private String id(LauncherActivityInfo a){return a.getComponentName().flattenToString();}

 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  prefs=getSharedPreferences("ember",MODE_PRIVATE);favorites=new Favorites(prefs.getString("favorites",""));
  FrameLayout root=new FrameLayout(this);root.setBackgroundColor(EmberStyle.INK);
  ImageView art=new ImageView(this);art.setImageResource(R.drawable.fortress);art.setScaleType(ImageView.ScaleType.CENTER_CROP);root.addView(art,new FrameLayout.LayoutParams(-1,-1));
  View shade=new View(this);shade.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0x44000000,0x11000000,0xe6000000}));root.addView(shade,new FrameLayout.LayoutParams(-1,-1));
  LinearLayout safe=new LinearLayout(this);safe.setOrientation(LinearLayout.VERTICAL);root.addView(safe,new FrameLayout.LayoutParams(-1,-1));setContentView(root);EmberStyle.insets(this,safe);
  FrameLayout screens=new FrameLayout(this);safe.addView(screens,new LinearLayout.LayoutParams(-1,0,1));
  home=column();drawer=column();screens.addView(home,new FrameLayout.LayoutParams(-1,-1));screens.addView(drawer,new FrameLayout.LayoutParams(-1,-1));
  buildHome();buildDrawer();
  if(Build.VERSION.SDK_INT>=33)getOnBackInvokedDispatcher().registerOnBackInvokedCallback(android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,()->setPage(false));
  setPage(state!=null&&state.getBoolean("drawer",false));if(state!=null)search.setText(state.getString("query",""));
 }
 private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
 private LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
 private void buildHome(){
  ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);home.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  LinearLayout body=column();body.setPadding(dp(20),dp(12),dp(20),dp(8));scroll.addView(body,new ScrollView.LayoutParams(-1,-2));
  LinearLayout top=row();TextView brand=EmberStyle.text(this,"EMBER CITADEL",26,EmberStyle.WHITE,true);top.addView(brand,new LinearLayout.LayoutParams(0,-2,1));
  Button settings=EmberStyle.button(this,"Settings");settings.setContentDescription("Launcher settings");settings.setOnClickListener(v->settings());top.addView(settings,new LinearLayout.LayoutParams(dp(88),dp(48)));body.addView(top);
  clock=EmberStyle.text(this,"",62,EmberStyle.WHITE,true);clock.setShadowLayer(8,0,2,Color.BLACK);body.addView(clock);
  date=EmberStyle.text(this,"",15,EmberStyle.WHITE,false);body.addView(date);
  battery=EmberStyle.text(this,"",12,EmberStyle.MUTED,false);battery.setPadding(0,dp(6),0,dp(12));body.addView(battery);
  Button find=EmberStyle.button(this,"Search applications");find.setOnClickListener(v->{setPage(true);search.requestFocus();search.postDelayed(()->{if(!isFinishing())((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(search,InputMethodManager.SHOW_IMPLICIT);},150);});body.addView(find,new LinearLayout.LayoutParams(-1,dp(50)));
  View space=new View(this);body.addView(space,new LinearLayout.LayoutParams(1,dp(100),1));
  TextView heading=EmberStyle.text(this,"FAVORITES",22,EmberStyle.RED,true);heading.setPadding(0,dp(8),0,dp(8));body.addView(heading);
  favoritesBox=column();body.addView(favoritesBox);renderFavorites();
  LinearLayout dock=row();dock.setPadding(dp(12),dp(8),dp(12),dp(12));dock.setBackgroundColor(0xe0090809);home.addView(dock);
  dock(dock,"Phone",()->safeStart(new Intent(this,DialerActivity.class)));
  dock(dock,"Messages",()->safeStart(new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"))));
  dock(dock,"Apps",()->setPage(true));
  dock(dock,"Camera",()->safeStart(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)));
  dock(dock,"Web",()->safeStart(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/"))));
 }
 private void dock(LinearLayout row,String label,Runnable action){Button b=EmberStyle.button(this,label);b.setTextSize(11);b.setTextColor(EmberStyle.RED);b.setOnClickListener(v->action.run());LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(52),1);p.setMargins(dp(2),0,dp(2),0);row.addView(b,p);}
 private void buildDrawer(){
  drawer.setPadding(dp(16),dp(12),dp(16),dp(8));drawer.setBackgroundColor(0xf50c0b0d);
  LinearLayout top=row();TextView title=EmberStyle.text(this,"APPLICATIONS",29,EmberStyle.WHITE,true);top.addView(title,new LinearLayout.LayoutParams(0,-2,1));Button close=EmberStyle.button(this,"Home");close.setOnClickListener(v->setPage(false));top.addView(close,new LinearLayout.LayoutParams(dp(70),dp(48)));drawer.addView(top);
  search=new EditText(this);search.setSingleLine(true);search.setHint("Search installed apps");search.setTextColor(EmberStyle.WHITE);search.setHintTextColor(EmberStyle.MUTED);search.setTextSize(16);search.setBackground(EmberStyle.panel());search.setPadding(dp(12),dp(4),dp(12),dp(4));search.setContentDescription("Search installed applications");LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,dp(52));sp.setMargins(0,dp(12),0,dp(6));drawer.addView(search,sp);
  search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int f){}public void onTextChanged(CharSequence s,int a,int b,int c){filter();}public void afterTextChanged(Editable e){}});
  count=EmberStyle.text(this,"Loading applications…",12,EmberStyle.MUTED,false);count.setPadding(0,dp(6),0,dp(8));drawer.addView(count);
  FrameLayout list=new FrameLayout(this);drawer.addView(list,new LinearLayout.LayoutParams(-1,0,1));
  grid=new GridView(this);grid.setNumColumns(getResources().getConfiguration().screenWidthDp>=600?6:4);grid.setVerticalSpacing(dp(10));grid.setHorizontalSpacing(dp(6));grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);grid.setSelector(android.R.color.transparent);adapter=new AppAdapter();grid.setAdapter(adapter);list.addView(grid,new FrameLayout.LayoutParams(-1,-1));
  empty=EmberStyle.text(this,"",16,EmberStyle.MUTED,false);empty.setGravity(Gravity.CENTER);list.addView(empty,new FrameLayout.LayoutParams(-1,-1));
  grid.setOnItemClickListener((p,v,pos,id)->launch(visible.get(pos)));grid.setOnItemLongClickListener((p,v,pos,id)->{appMenu(visible.get(pos));return true;});
  TextView hint=EmberStyle.text(this,"Hold an app to pin it or open App info",12,EmberStyle.MUTED,false);hint.setPadding(0,dp(8),0,dp(4));drawer.addView(hint);
 }
 private void setPage(boolean appsPage){showDrawer=appsPage;if(appsPage)loadApps();drawer.setVisibility(appsPage?View.VISIBLE:View.GONE);home.setVisibility(appsPage?View.GONE:View.VISIBLE);if(!appsPage){search.clearFocus();((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(),0);renderFavorites();}}
 private void loadApps(){
  if(loading)return;loading=true;loadError="";
  worker.execute(()->{
   ArrayList<LauncherActivityInfo> next=new ArrayList<>();String error="";
   try{LauncherApps manager=(LauncherApps)getSystemService(LAUNCHER_APPS_SERVICE);for(LauncherActivityInfo a:manager.getActivityList(null,android.os.Process.myUserHandle()))if(!a.getComponentName().getPackageName().equals(getPackageName()))next.add(a);next.sort((a,b)->String.CASE_INSENSITIVE_ORDER.compare(a.getLabel().toString(),b.getLabel().toString()));}
   catch(RuntimeException ex){error="Could not load apps. Return Home and open Apps to retry.";}
   final String message=error;runOnUiThread(()->{loading=false;if(isDestroyed())return;apps.clear();apps.addAll(next);icons.clear();loadError=message;filter();renderFavorites();});
  });
 }
 private void filter(){if(adapter==null)return;String q=search.getText().toString();visible.clear();for(LauncherActivityInfo a:apps)if(Favorites.matches(a.getLabel().toString(),q))visible.add(a);adapter.notifyDataSetChanged();count.setText(visible.size()+" / "+apps.size()+" applications");empty.setText(!loadError.isEmpty()?loadError:loading?"Loading applications…":"No matching applications");empty.setVisibility(visible.isEmpty()?View.VISIBLE:View.GONE);}
 private void renderFavorites(){
  if(favoritesBox==null)return;favoritesBox.removeAllViews();ArrayList<LauncherActivityInfo> pinned=new ArrayList<>();for(String id:favorites.ids())for(LauncherActivityInfo a:apps)if(id(a).equals(id)){pinned.add(a);break;}
  if(pinned.isEmpty()){TextView text=EmberStyle.text(this,loading?"Loading your applications…":"Open Apps and hold an app to pin it here.",14,EmberStyle.WHITE,false);text.setPadding(0,dp(8),0,dp(20));favoritesBox.addView(text);return;}
  for(int i=0;i<pinned.size();i+=4){LinearLayout line=row();favoritesBox.addView(line);for(int j=0;j<4;j++){if(i+j<pinned.size()){LauncherActivityInfo app=pinned.get(i+j);View tile=tile(app);tile.setOnClickListener(v->launch(app));tile.setOnLongClickListener(v->{appMenu(app);return true;});line.addView(tile,new LinearLayout.LayoutParams(0,dp(104),1));}else line.addView(new View(this),new LinearLayout.LayoutParams(0,dp(104),1));}}
 }
 private View tile(LauncherActivityInfo app){
  LinearLayout box=column();box.setGravity(Gravity.CENTER_HORIZONTAL);box.setPadding(dp(3),dp(4),dp(3),dp(2));
  ImageView icon=new ImageView(this);icon.setBackground(EmberStyle.panel());icon.setPadding(dp(9),dp(9),dp(9),dp(9));icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
  Drawable d=icons.get(id(app));if(d==null){try{d=app.getBadgedIcon(0);}catch(RuntimeException ex){d=getDrawable(R.drawable.crest);}icons.put(id(app),d);}icon.setImageDrawable(d);
  if(prefs.getBoolean("emberIcons",false))icon.setColorFilter(new LightingColorFilter(0xffe57563,0));
  box.addView(icon,new LinearLayout.LayoutParams(dp(58),dp(58)));
  TextView label=EmberStyle.text(this,app.getLabel().toString(),12,EmberStyle.WHITE,false);label.setGravity(Gravity.CENTER);label.setMaxLines(2);label.setEllipsize(TextUtils.TruncateAt.END);label.setShadowLayer(4,0,1,Color.BLACK);box.addView(label,new LinearLayout.LayoutParams(-1,dp(36)));
  box.setContentDescription(app.getLabel()+ (favorites.contains(id(app))?", pinned":""));return box;
 }
 private class AppAdapter extends BaseAdapter {public int getCount(){return visible.size();}public Object getItem(int p){return visible.get(p);}public long getItemId(int p){return p;}public View getView(int p,View old,android.view.ViewGroup parent){View v=tile(visible.get(p));v.setLayoutParams(new android.widget.AbsListView.LayoutParams(-1,dp(104)));return v;}}
 private void launch(LauncherActivityInfo app){try{((LauncherApps)getSystemService(LAUNCHER_APPS_SERVICE)).startMainActivity(app.getComponentName(),android.os.Process.myUserHandle(),null,null);}catch(RuntimeException e){toast("This app is unavailable. Refreshing the list.");loadApps();}}
 private void appMenu(LauncherActivityInfo app){String key=id(app);boolean pinned=favorites.contains(key);String[] options=pinned?new String[]{"Unpin from Home","Move earlier","Move later","App info"}:new String[]{"Pin to Home","App info"};new AlertDialog.Builder(this).setTitle(app.getLabel()).setItems(options,(d,which)->{
  if(which==options.length-1){safeStart(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.fromParts("package",app.getComponentName().getPackageName(),null)));return;}
  if(which==0){if(pinned)favorites.remove(key);else if(!favorites.add(key)){toast("Home supports up to 12 favorites");return;}}
  else favorites.move(key,which==1?-1:1);
  prefs.edit().putString("favorites",favorites.encode()).apply();renderFavorites();adapter.notifyDataSetChanged();
 }).show();}
 private void settings(){String icon=prefs.getBoolean("emberIcons",false)?"Restore original app colors":"Tint app icons ember-red";new AlertDialog.Builder(this).setTitle("Ember Citadel").setItems(new String[]{"Set as default Home",icon,"Manage favorites","Android settings","About this build"},(d,i)->{
  if(i==0){if(Build.VERSION.SDK_INT>=29){RoleManager roles=getSystemService(RoleManager.class);if(roles.isRoleAvailable(RoleManager.ROLE_HOME)&&!roles.isRoleHeld(RoleManager.ROLE_HOME)){startActivityForResult(roles.createRequestRoleIntent(RoleManager.ROLE_HOME),10);return;}}safeStart(new Intent(Settings.ACTION_HOME_SETTINGS));}
  if(i==1){prefs.edit().putBoolean("emberIcons",!prefs.getBoolean("emberIcons",false)).apply();renderFavorites();adapter.notifyDataSetChanged();}
  if(i==2)manageFavorites();if(i==3)safeStart(new Intent(Settings.ACTION_SETTINGS));
  if(i==4)new AlertDialog.Builder(this).setTitle("Ember Citadel 0.1.0").setMessage("Original fantasy launcher prototype. Wallpaper generated with AI. Norse typeface by Joël Carrouché, embedded under his free-font license.\n\nCurrent-profile apps and up to 12 favorites. No widgets, folders, work-profile support, or system-screen replacement in this build. Native app icons belong to their respective owners and are read from your installed apps.").setPositiveButton("Close",null).show();
 }).show();}
 private void manageFavorites(){ArrayList<String> keys=new ArrayList<>(favorites.ids());String[] labels=new String[keys.size()];for(int i=0;i<keys.size();i++){labels[i]=keys.get(i);for(LauncherActivityInfo a:apps)if(id(a).equals(keys.get(i)))labels[i]=a.getLabel().toString();}new AlertDialog.Builder(this).setTitle("Tap a favorite to remove it").setItems(labels,(d,pos)->{favorites.remove(keys.get(pos));prefs.edit().putString("favorites",favorites.encode()).apply();renderFavorites();}).setNegativeButton("Close",null).show();}
 private void updateClock(){Date now=new Date();clock.setText(android.text.format.DateFormat.getTimeFormat(this).format(now));date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(now));Intent b=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));int level=b==null?-1:b.getIntExtra(BatteryManager.EXTRA_LEVEL,-1),scale=b==null?-1:b.getIntExtra(BatteryManager.EXTRA_SCALE,-1);battery.setText(level>=0&&scale>0?Math.round(level*100f/scale)+"% battery":"Battery unavailable");}
 private void safeStart(Intent intent){try{startActivity(intent);}catch(ActivityNotFoundException|SecurityException e){toast("No compatible app is available");}}
 private void toast(String text){Toast.makeText(this,text,Toast.LENGTH_SHORT).show();}
 @Override protected void onResume(){super.onResume();timer.removeCallbacks(tick);timer.post(tick);loadApps();}
 @Override protected void onPause(){timer.removeCallbacks(tick);super.onPause();}
 @Override protected void onNewIntent(Intent intent){super.onNewIntent(intent);setIntent(intent);setPage(false);}
 @Override public void onBackPressed(){setPage(false);}
 @Override protected void onSaveInstanceState(Bundle out){out.putBoolean("drawer",showDrawer);out.putString("query",search.getText().toString());super.onSaveInstanceState(out);}
 @Override protected void onDestroy(){worker.shutdownNow();timer.removeCallbacksAndMessages(null);super.onDestroy();}
}

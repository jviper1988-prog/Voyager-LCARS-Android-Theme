package com.jakebrowne.embercitadel;
import java.util.*;
/** Order-preserving favorites independent of Android for unit testing. */
public final class Favorites {
 public static final int LIMIT=12;
 private final List<String> ids=new ArrayList<>();
 public Favorites(String saved){if(saved!=null)for(String id:saved.split("\n")){if(id.contains("/")&&!ids.contains(id)&&ids.size()<LIMIT)ids.add(id);}}
 public List<String> ids(){return new ArrayList<>(ids);}
 public boolean contains(String id){return ids.contains(id);}
 public boolean add(String id){if(id==null||!id.contains("/")||id.contains("\n")||ids.contains(id)||ids.size()>=LIMIT)return false;ids.add(id);return true;}
 public void remove(String id){ids.remove(id);}
 public void move(String id,int delta){int from=ids.indexOf(id),to=from+delta;if(from>=0&&to>=0&&to<ids.size())Collections.swap(ids,from,to);}
 public String encode(){return String.join("\n",ids);}
 public static boolean matches(String label,String query){return label.toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT));}
}

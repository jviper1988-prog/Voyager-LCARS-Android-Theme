package com.jakebrowne.embercitadel;
import org.junit.Test;import static org.junit.Assert.*;
public class FavoritesTest {
 @Test public void persistenceAndOrder(){Favorites f=new Favorites("");f.add("one/A");f.add("two/B");f.move("two/B",-1);assertEquals("two/B",new Favorites(f.encode()).ids().get(0));}
 @Test public void duplicateAndInvalidSavedData(){Favorites f=new Favorites("one/A\none/A\ninvalid");assertEquals(1,f.ids().size());assertFalse(f.add("bad"));assertFalse(f.add("one/A"));}
 @Test public void limit(){Favorites f=new Favorites("");for(int i=0;i<12;i++)assertTrue(f.add("pkg"+i+"/A"));assertFalse(f.add("extra/A"));}
 @Test public void removalAndBoundaryMove(){Favorites f=new Favorites("one/A\ntwo/B");f.move("one/A",-1);assertEquals("one/A",f.ids().get(0));f.remove("one/A");assertFalse(f.contains("one/A"));}
 @Test public void search(){assertTrue(Favorites.matches("Samsung Notes"," NOTES "));assertFalse(Favorites.matches("Camera","mail"));assertTrue(Favorites.matches("Camera",""));}
}

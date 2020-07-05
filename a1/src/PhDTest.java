import static org.junit.Assert.*;

import org.junit.Test;

public class PhDTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
		PhD p1 = new PhD("Gries", 2018, 5);
		PhD p2 = new PhD("Ges", 2000, 5);
		PhD p3 = new PhD("Gis", 2018, 4, p1);
		PhD p4 = new PhD("ries", 2000, 6, p2, p1);
		PhD p5 = new PhD("res", 2000, 4, p4, p3);
		
		try {new PhD(null, 2000, 4);; fail("no exception thrown");}
		catch (AssertionError e) {if (e.getMessage() != null) fail();}
		
		try {new PhD("h", 2000, 13);; fail("no exception thrown");}
		catch (AssertionError e) {if (e.getMessage() != null) fail();}
		
		try {new PhD("lo", null, 4);; fail("no exception thrown");}
		catch (AssertionError e) {if (e.getMessage() != null) fail();}
		
		assert(!p4.isSiblingOf(p5));
		assert(p1.numAdvisees() == 2);
		assert(p5.gotBefore(p4));
	}

}

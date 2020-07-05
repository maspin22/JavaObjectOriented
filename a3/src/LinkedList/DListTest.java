package LinkedList;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DListTest {

	@Test
	void firstThree() {
		DList<Integer> d = new DList<>();
		d.prepend(2);
		d.prepend(1);
		d.prepend(0);	
		d.append(3);
		d.append(4);
		d.append(5);
		
		assertEquals(d.toStringRev(), "[5, 4, 3, 2, 1, 0]");
		
		d.prepend(-1);
		d.append(6);
		
		assertEquals(d.toString(), "[-1, 0, 1, 2, 3, 4, 5, 6]");
		assertEquals(d.toStringRev(), "[6, 5, 4, 3, 2, 1, 0, -1]");
				
	}
	
	@Test
	void lastThree() {
		DList<Integer> d = new DList<>();
		
		d.prepend(2);
		d.prepend(1);
		d.prepend(0);
		d.append(3);
		d.append(4);
		d.append(5);
		
		assertEquals(0, (int) d.getNode(0).value());
		assertEquals(5, (int) d.getNode(5).value());
		
		d.insertAfter(9, d.getNode(3));
		d.insertAfter(10, d.getNode(6));
		assertEquals(d.toString(), "[0, 1, 2, 3, 9, 4, 5, 10]");
		
		assertEquals(9, (int)d.getNode(4).value());
		assertEquals(10, (int)d.getNode(7).value());
		
		d.delete(d.getNode(0));
		d.delete(d.getNode(3));
		d.delete(d.getNode(5));
		
		assertEquals(d.toString(), "[1, 2, 3, 4, 5]");
		
		assertEquals(1, (int)d.getNode(0).value());
		assertEquals(5, (int)d.getNode(4).value());
		
	}
	
	@Test
	void stringTest() {
		DList<String> d = new DList<>();
		d.prepend("2");
		d.prepend("1");
		d.prepend("0");
		d.append("3");
		d.append("4");
		d.append("5");
		
		assertEquals(d.toStringRev(), "[5, 4, 3, 2, 1, 0]");
	}
}

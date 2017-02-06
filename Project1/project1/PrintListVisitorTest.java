package cs4321.project1;

import static org.junit.Assert.*;
import cs4321.project1.list.*;

import org.junit.Test;

public class PrintListVisitorTest {

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("+ 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 +", pv1.getResult());
	}
	
	@Test
	public void testStudentSetting1() {
		ListNode n1 = new NumberListNode(6.0);
		ListNode n2 = new UnaryMinusListNode();
		ListNode n14 = new UnaryMinusListNode();
		ListNode n3 = new NumberListNode(3.0);
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new AdditionListNode();
		ListNode n6 = new MultiplicationListNode();
		ListNode n7 = new NumberListNode(3.0);
		ListNode n8 = new NumberListNode(2.0);
		ListNode n9 = new NumberListNode(4.0);
		ListNode n10 = new UnaryMinusListNode();
		ListNode n11 = new SubtractionListNode();
		ListNode n12 = new SubtractionListNode();
		ListNode n13 = new DivisionListNode();
		n1.setNext(n2);
		n2.setNext(n14);
		n14.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		n5.setNext(n6);
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		n10.setNext(n11);
		n11.setNext(n12);
		n12.setNext(n13); 
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("6.0 ~ ~ 3.0 1.0 + * 3.0 2.0 4.0 ~ - - /", pv1.getResult());
	}
	
	@Test
	public void testStudentSetting2() {
		ListNode n1 = new MultiplicationListNode();
		ListNode n2 = new AdditionListNode();
		ListNode n3 = new MultiplicationListNode();
		ListNode n4 = new NumberListNode(6.5);
		ListNode n5 = new AdditionListNode();
		ListNode n6 = new UnaryMinusListNode();
		ListNode n7 = new NumberListNode(3.1);
		ListNode n8 = new UnaryMinusListNode();
		ListNode n9 = new NumberListNode(2.9);
		ListNode n10 = new DivisionListNode();
		ListNode n11 = new UnaryMinusListNode();
		ListNode n12 = new NumberListNode(7.5);
		ListNode n13 = new NumberListNode(3.0);
		ListNode n14 = new SubtractionListNode();
		ListNode n15 = new UnaryMinusListNode();
		ListNode n16 = new NumberListNode(24.4);
		ListNode n17 = new UnaryMinusListNode();
		ListNode n18 = new NumberListNode(20.4);
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		n5.setNext(n6);
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		n10.setNext(n11);
		n11.setNext(n12);
		n12.setNext(n13);
		n13.setNext(n14);
		n14.setNext(n15);
		n15.setNext(n16);
		n16.setNext(n17);
		n17.setNext(n18); 
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("* + * 6.5 + ~ 3.1 ~ 2.9 / ~ 7.5 3.0 - ~ 24.4 ~ 20.4",
				pv1.getResult());
	}
	
}

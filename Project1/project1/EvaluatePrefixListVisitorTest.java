package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.list.*;

public class EvaluatePrefixListVisitorTest {
	
	private static final double DELTA = 1e-15;

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}

	@Test
	public void testAdditionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n3.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new AdditionListNode();
		n6.setNext(n5);
		n5.setNext(n4);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionMultipleInstances() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		ListNode n4 = new NumberListNode(3.0);
		ListNode n5 = new AdditionListNode();
		n5.setNext(n4);
		n4.setNext(n3);
		n3.setNext(n2);
		n2.setNext(n1);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n5.accept(v1);
		assertEquals(6.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testStudentSettings1() {
		ListNode n1 = new DivisionListNode();
		ListNode n2 = new MultiplicationListNode();
		ListNode n3 = new UnaryMinusListNode();
		ListNode n14 = new UnaryMinusListNode();
		ListNode n4 = new NumberListNode(6.0);
		ListNode n5 = new AdditionListNode();
		ListNode n6 = new NumberListNode(3.0);
		ListNode n7 = new NumberListNode(1.0);
		ListNode n8 = new SubtractionListNode();
		ListNode n9 = new NumberListNode(3.0);
		ListNode n10 = new SubtractionListNode();
		ListNode n11 = new NumberListNode(2.0);
		ListNode n12 = new UnaryMinusListNode();
		ListNode n13 = new NumberListNode(4.0);
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
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(-8.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testStudentSettings2() {
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
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(166.0, v1.getResult(), DELTA);
	}

}

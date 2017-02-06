package cs4321.project1;

import static org.junit.Assert.*;
import cs4321.project1.list.*;

import org.junit.Test;

public class EvaluatePostfixListVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new AdditionListNode();
		n5.setNext(n4);
		n4.setNext(n6);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n5.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionMultipleInstances() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		ListNode n4 = new NumberListNode(3.0);
		ListNode n5 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5); //expression is 1 2 + 3 + 
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(6.0, v1.getResult(), DELTA);
	}

	@Test
	public void testStudentSettings1() {
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
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-8.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testStudentSettings2() {
		ListNode n1 = new NumberListNode(6.5);
		ListNode n2 = new NumberListNode(3.1);
		ListNode n3 = new UnaryMinusListNode();
		ListNode n4 = new NumberListNode(2.9);
		ListNode n5 = new UnaryMinusListNode();
		ListNode n6 = new AdditionListNode();
		ListNode n7 = new MultiplicationListNode();
		ListNode n8 = new NumberListNode(7.5);
		ListNode n9 = new UnaryMinusListNode();
		ListNode n10 = new NumberListNode(3.0);
		ListNode n11 = new DivisionListNode();
		ListNode n12 = new AdditionListNode();
		ListNode n13 = new NumberListNode(24.4);
		ListNode n14 = new UnaryMinusListNode();
		ListNode n15 = new NumberListNode(20.4);
		ListNode n16 = new UnaryMinusListNode();
		ListNode n17 = new SubtractionListNode();
		ListNode n18 = new MultiplicationListNode();
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
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(166.0, v1.getResult(), DELTA);
	}
	
}

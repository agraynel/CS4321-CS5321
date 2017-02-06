package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.*;
import cs4321.project1.list.*;

public class BuildPrefixExpressionTreeVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleLeafNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
        BuildPrefixExpressionTreeVisitor v1 = new BuildPrefixExpressionTreeVisitor();
		n1.accept(v1);
		ListNode result = v1.getResult();
		assertNull(result.getNext());
		assertTrue(result instanceof NumberListNode);
	}
	
	@Test
	public void testAdditionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new AdditionTreeNode(n1, n2);
		TreeNode n4 = new AdditionTreeNode(n2, n1);
		
        BuildPrefixExpressionTreeVisitor v1 = new BuildPrefixExpressionTreeVisitor();
		n3.accept(v1);
		ListNode result = v1.getResult();
		assertTrue(result instanceof AdditionListNode);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		
        BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		n4.accept(v2);
		result = v2.getResult();
		assertTrue(result instanceof AdditionListNode);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
	}
	
	@Test
	public void testUnaryMinusNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new UnaryMinusTreeNode(n1);
		
        BuildPrefixExpressionTreeVisitor v1 = new BuildPrefixExpressionTreeVisitor();
		n2.accept(v1);
		ListNode result = v1.getResult();
		assertTrue(result instanceof UnaryMinusListNode);
		ListNode first = result.getNext();
		assertTrue(first instanceof NumberListNode);
		assertEquals(((NumberListNode) first).getData(), 1.0, DELTA);
	}

	@Test
	public void testStudentSettings1() {
		TreeNode n1 = new LeafTreeNode(6.0);
		TreeNode n2 = new LeafTreeNode(3.0);
		TreeNode n3 = new LeafTreeNode(1.0);
		TreeNode n4 = new AdditionTreeNode(n2,n3);
		TreeNode n5 = new UnaryMinusTreeNode(n1);
		TreeNode n6 = new MultiplicationTreeNode(n5,n4);
		TreeNode n7 = new LeafTreeNode(3.0);
		TreeNode n8 = new LeafTreeNode(2.0);
		TreeNode n9 = new LeafTreeNode(4.0);
		TreeNode n10 = new UnaryMinusTreeNode(n9);
		TreeNode n11 = new SubtractionTreeNode(n8,n10);
		TreeNode n12 = new SubtractionTreeNode(n7,n11);
		TreeNode n13 = new DivisionTreeNode(n6,n12);
		BuildPrefixExpressionTreeVisitor v1 = new BuildPrefixExpressionTreeVisitor();
		n13.accept(v1);
		ListNode result = v1.getResult();
		assertTrue(result instanceof DivisionListNode);
		result = result.getNext();
		assertTrue(result instanceof MultiplicationListNode);
		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(6.0, ((NumberListNode)result).getData(), DELTA);
		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(3.0, ((NumberListNode)result).getData(), DELTA);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(1.0, ((NumberListNode)result).getData(), DELTA);
		result = result.getNext();
		assertTrue(result instanceof SubtractionListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(3.0, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof SubtractionListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(2.0, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof UnaryMinusListNode);
	    result = result.getNext();
	    assertEquals(4.0, ((NumberListNode)result).getData(), DELTA);
	    assertNull(result.getNext());
	}
	
	@Test
	public void testStudentSetting() {
		TreeNode n1 = new LeafTreeNode(6.5);
		TreeNode n2 = new LeafTreeNode(3.1);
		TreeNode n3 = new LeafTreeNode(2.9);
		TreeNode n4 = new UnaryMinusTreeNode(n2);
		TreeNode n5 = new UnaryMinusTreeNode(n3);
		TreeNode n6 = new AdditionTreeNode(n4,n5);
		TreeNode n7 = new MultiplicationTreeNode(n1,n6);
		TreeNode n8 = new LeafTreeNode(7.5);
		TreeNode n9 = new LeafTreeNode(3.0);
		TreeNode n10 = new UnaryMinusTreeNode(n8);
		TreeNode n11 = new DivisionTreeNode(n10,n9);
		TreeNode n12 = new AdditionTreeNode(n7,n11);
		TreeNode n13 = new LeafTreeNode(24.4);
		TreeNode n14 = new LeafTreeNode(20.4);
		TreeNode n15 = new UnaryMinusTreeNode(n13);
		TreeNode n16 = new UnaryMinusTreeNode(n14);
		TreeNode n17 = new SubtractionTreeNode(n15,n16);
		TreeNode n18 = new MultiplicationTreeNode(n12,n17);
		BuildPrefixExpressionTreeVisitor v1 = new BuildPrefixExpressionTreeVisitor();
		n18.accept(v1);
		ListNode result = v1.getResult();
		assertTrue(result instanceof MultiplicationListNode);
		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);
		result = result.getNext();
		assertTrue(result instanceof MultiplicationListNode);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(6.5, ((NumberListNode)result).getData(), DELTA);
		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);
	    result = result.getNext();
	    assertTrue(result instanceof UnaryMinusListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(3.1, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof UnaryMinusListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(2.9, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof DivisionListNode);
	    result = result.getNext();
	    assertTrue(result instanceof UnaryMinusListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(7.5, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(3.0, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof SubtractionListNode);
	    result = result.getNext();
	    assertTrue(result instanceof UnaryMinusListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(24.4, ((NumberListNode)result).getData(), DELTA);
	    result = result.getNext();
	    assertTrue(result instanceof UnaryMinusListNode);
	    result = result.getNext();
	    assertTrue(result instanceof NumberListNode);
	    assertEquals(20.4, ((NumberListNode)result).getData(), DELTA);
	    assertNull(result.getNext());
		
	}
	
}

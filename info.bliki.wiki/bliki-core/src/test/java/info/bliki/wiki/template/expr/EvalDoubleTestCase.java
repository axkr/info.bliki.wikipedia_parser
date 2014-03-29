package info.bliki.wiki.template.expr;

import info.bliki.wiki.template.expr.eval.DoubleEvaluator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests evaluation in <code>double</code> Mediwiki expression mode
 */
public class EvalDoubleTestCase extends TestCase {

	public EvalDoubleTestCase(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(EvalDoubleTestCase.class);
	}

	public void check(String in, String compareWith) {
		try {
			DoubleEvaluator engine = new DoubleEvaluator();
			double d = engine.evaluate(in);
			assertEquals(Double.valueOf(d).toString(), compareWith);
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("", e.getMessage());
		}
	}

	public void testEval001() {
		check("2+2*2", "6.0");
		check("((15782.316272965878)round((3))/1E5round0)E5", "1.5782E9");
		check("1e-92", "1.0E-92");
		check("42", "42.0");
		check("1.5", "1.5");
		check("-42", "-42.0");
		check("+42", "42.0");
		check("-42.1", "-42.1");
		check("+42.2", "42.2");
		check("2+2*2", "6.0");
		check("3-4-5-6-7-8", "-27.0");
		check("2*2*2*2*2*2*2*2*2", "512.0");
		check("3+4*7", "31.0");
		check("3+4*7*3", "87.0");
		check("1+2+3+4*7*3", "90.0");
	}

	public void testEval002() {
		check("-3/4", "-0.75");
		check("+3/4", "0.75");
		check("3 div 4", "0.75");
	}

	public void testEval003() {
		check("30 mod 7", "2.0");
		check("-8 mod -3", "-2.0");
	}

	public void testEval004() {
		check("((15782.316272965878)round((3))/1E5round0)E5", "1.5782E9");
		check("(4/7)*1e-290round300", "9.223372036854776E-282");
		check("30 / 7 round 0", "4.0");
		check("30 / 7 round 4", "4.2857");
		check("30 / 7 round 1", "4.3");
		check("1911 round -2", "1900.0");
		check("2.5 round 0", "3.0");
		check("-2.5 round 0", "-3.0");
	}

	public void testEval005() {
		check("trunc1.2", "1.0");
		check("trunc-1.2", "-1.0");
		check("floor 1.2", "1.0");
		check("floor -1.2", "-2.0");
		check("ceil 1.2", "2.0");
		check("ceil-1.2", "-1.0");
	}

}
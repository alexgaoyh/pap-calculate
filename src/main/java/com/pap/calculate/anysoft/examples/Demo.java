package com.pap.calculate.anysoft.examples;

import java.util.Date;

import com.pap.calculate.anysoft.formula.DataProvider;
import com.pap.calculate.anysoft.formula.DateUtil;
import com.pap.calculate.anysoft.formula.DefaultFunctionHelper;
import com.pap.calculate.anysoft.formula.Expression;
import com.pap.calculate.anysoft.formula.Function;
import com.pap.calculate.anysoft.formula.FunctionHelper;
import com.pap.calculate.anysoft.formula.Parser;

public class Demo {

	public static void simple(String[] args) {
		Parser parser = new Parser();

		String formula = "1>2 || 3<5 && 2";

		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(null));
	}

	public static void withDataProvider(String[] args) {
		Parser parser = new Parser();

		String formula = "1+2+to_long(id)";

		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(new DataProvider() {
			@Override
			public String getValue(String varName, Object context,
					String defaultValue) {
				if (varName.equals("id")) {
					return "100";
				}
				return null;
			}

			@Override
			public Object getContext(String varName) {
				return new Object();
			}
		}));
	}

	public static void withDefaultFunctionHelper(String[] args) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		String formula = "1+2+choice(2>1,100,2000)";
		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(null));
	}

	public static void withFunctionHelper(String[] args) {
		Parser parser = new Parser(new FunctionHelper() {
			public Expression customize(String funcName) {
				if (funcName.equals("choice")) {
					return new Function.Choice();
				}
				return null;
			}
		});

		String formula = "1+2+choice(2>1,100,2000)";

		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(null));
	}

	public static void testDate(String[] args) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		String formula = "to_char(to_date(now,'yyyyMMdd'),'yyyyMMddhh24miss')";
		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(new DataProvider() {
			@Override
			public String getValue(String varName, Object context,
					String defaultValue) {
				if (varName.equals("now")) {
					return DateUtil.formatDate(new Date(), "yyyyMMdd");
				}
				return null;
			}

			@Override
			public Object getContext(String varName) {
				return new Object();
			}
		}));
	}

	public static void testString(String[] args) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		String formula = "substr(hello,instr(hello,'world'),9)+match(hello,'Hello*')+strlen(hello)";
		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(new DataProvider() {
			@Override
			public String getValue(String varName, Object context,
					String defaultValue) {
				if (varName.equals("hello")) {
					return "Hello world";
				}
				return null;
			}

			@Override
			public Object getContext(String varName) {
				return new Object();
			}
		}));
	}

	public static void testNvl(String[] args) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		String formula = "nvl(null_var,1000) + to_long(nvl(id,'20'))";
		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(new DataProvider() {
			@Override
			public String getValue(String varName, Object context,
					String defaultValue) {
				if (varName.equals("id")) {
					return "100";
				}
				return null;
			}

			@Override
			public Object getContext(String varName) {
				return new Object();
			}
		}));
	}
	
	public static void testPMT(String[] args) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		String formula = "PMT(0.00515,360,-770000,0,0)";
		Expression expr = parser.parse(formula);

		System.out.println(expr.toString());
		System.out.println(expr.getValue(new DataProvider() {
			@Override
			public String getValue(String varName, Object context,
					String defaultValue) {
				return null;
			}

			@Override
			public Object getContext(String varName) {
				return new Object();
			}
		}));
	}
	
	public static Object testMyFormulaUtilss(String formula) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		Expression expr = parser.parse(formula);

		Object returnObj = (expr.getValue(new DataProvider() {
			@Override
			public String getValue(String varName, Object context,
					String defaultValue) {
				return null;
			}

			@Override
			public Object getContext(String varName) {
				return new Object();
			}
		}));
		return returnObj;
	}

	public static void main(String[] args) {
		String[] str = new String[]{"PMT(0.00515,360,-770000,0,0)", "FV(0.00515, -770000, 4706.02, 0, 1)",
				"NPER(0.00515, 4706.02, -770000, 0, 1)", "PV(0.00515, 360, -4706.02, 0, 1)",
				"DB(100,0,10,1,12)", "DDB(2400,0,5,1,2)",
				"RATE(4,10000,-30000,0,0,0.1)", "EFFECT(0.0525,4)",
				"1+2*3+(3+5*6+7*(78-70))",
				"1+2*3+(3+5*6+(PMT(0.00515,360,-770000,0,0))*(78-70))",
				"1+2*3+(3+5*6+(PMT(RATE(4,10000,-30000,0,0,0.1),360,-770000,0,0))*(78-70))",
				"IF(30=30, IF(24=24, 0.08, 0.11), IF(24=24, 0.11, 0.14))",
				"IF(20=30,IF(24=24,0.08,0.11),IF(24=24,0.11,0.14))",
				"20.0+IF(20=30,IF(24=24,0.08,0.11),IF(24=24,0.11,0.14))",
				"(11+11+11+11+11)*(20+IF(20=30,IF(24=24,0.08,0.11),IF(24=24,0.11,0.14)))",
				"3.0/100",
				"DEVIDE(10, 3)",
				"ROUND(123.134678795, 2)",
				"ROUND(123.134678795, 0)",
				"ROUND(123.134678795, -1)",
				"(1000+1000+(1000)+(1000)+1000+1000+1000)*IF(TOINT(0.20*100)>=30,0.08,0.11)",
				"(1000+1000+(1000)+(1000)+1000+1000+1000)*IF(TOINT(0.30*100)>=30,0.08,0.11)",
				"E_NUMBER(128000*(1.2*100))",
				"IF(2.0<=1.6,0.05,0.10) ",
				"IF(1.0<=1.6,0.05,0.10) ",
				"IF(1.6<=1.6,0.05,0.10) ",
				"ROUND(188888*(0.20),0)",
				"ROUND(188888*(0.20),0)+2500+188888*IF(36=24,0.07,0.11)",
				"ROUND(ROUND(188888*(0.20),0)+2500+188888*IF(36=24,0.07,0.11),0) ",
				"ROUNDUP(123456, -4)",
				"NUM_DIGIT(234.0000000)",
				"NUM_DIGIT(234.0)",
				"E_YUSHU(253219, 24)",
				"NUM_DIGIT((179999+(0)+(2500)+(24/12*(0+6700+2300)))/(24)) "};
		for (String string : str) {
			System.out.println("执行语句:  " + string +"  结果值：  " + testMyFormulaUtilss(string));
		}
	}
}

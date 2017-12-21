package com.pap.calculate.anysoft.formula.utilss;

import com.pap.calculate.anysoft.formula.DataProvider;
import com.pap.calculate.anysoft.formula.DefaultFunctionHelper;
import com.pap.calculate.anysoft.formula.Expression;
import com.pap.calculate.anysoft.formula.Parser;

public class MyFormulaUtilss {

	public static Object selfOperationResult(String expressStr) {
		DefaultFunctionHelper functionHelper = new DefaultFunctionHelper();
		Parser parser = new Parser(functionHelper);
		String formula = expressStr;
		Expression expr = parser.parse(formula);

		Object returnObject = (expr.getValue(new DataProvider() {
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
		return returnObject;
	}
}

package com.pap.calculate.anysoft.formula;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;



/**
 * Function
 * @author duanyy
 * @version 1.0.0
 */
abstract public class Function extends Expression{

	/**
	 * constructor
	 * @param _prototype the prototype of the function
	 */
	public Function(String _prototype) {
		super(Operator.OP_Extend);
		prototype = _prototype;
	}

	/**
	 * arguments
	 */
	protected Vector<Expression> args = new Vector<Expression>();
	
	/**
	 * prototype
	 */
	protected String prototype = "Function";
	/**
	 * to get argument list
	 * @return argument list
	 */
	public Expression [] arguments(){
		return args.toArray(new Expression[0]);
	}
	
	/**
	 * to get count of argument
	 * @return count
	 */
	public int getArgumentCount(){
		return args.size();
	}
	
	/**
	 * to get argument by index
	 * @param index index of argument
	 * @return argument
	 */
	public Expression getArgument(int index){
		return args.elementAt(index);
	}
	
	@Override
	public String getOperatorPrototype() {
		return prototype;
	}	
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(getOperatorPrototype());
		buffer.append("(");
		
		for (int i = 0 ; i < getArgumentCount() ; i ++){
			if (i != 0){
				buffer.append(",");
			}
			buffer.append(getArgument(i).toString());
		}
		
		buffer.append(")");
		return buffer.toString();
	}
	/**
	 * to add a argument to argument list
	 * @param arg the argument added
	 * @return this
	 */
	public Function addArgument(Expression arg){
		if (arg == null){
			throw new FormulaException("argument can not be null.");
		}
		checkArgument(arg);
		args.add(arg);
		return this;
	}
	
	/**
	 * to check argument before added
	 * @param arg the argument to be checked
	 * @throw FormulaException an exception when error occurs
	 */
	abstract public void checkArgument(Expression arg) throws FormulaException;
	
	/**
	 * Choice function
	 * 
	 * Make choice between 2 expressions with a condition expression. 
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * choice(bool_expr,expr1,expr2)
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:if bool_expr is true ,expr1 otherwise expr2.
	 * 
	 * @author duanyy
	 * @version 1.0.0
	 */
	public static class Choice extends Function{
		/**
		 * constructor
		 */
		public Choice() {
			super("choice");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 3){
				throw new FormulaException("choice function only supports 3 arguments.");
			}
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 3){
				throw new FormulaException("choice function need 3 arguments.we have " + getArgumentCount());
			}
			
			Expression boolExpr = getArgument(0);
			
			ExprValue boolValue = boolExpr.getValue(provider);
			if (boolValue.getBoolean()){
				return getArgument(1).getValue(provider);
			}else{
				return getArgument(2).getValue(provider);
			}
		}
	}
	
	/**
	 * Nvl
	 * 
	 * <br>
	 * if a expr is null,give a default value.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~
	 * nvl(expr,default_expr)
	 * ~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:expr if expr is not null,or default_expr
	 * @author duanyy
	 * @version 1.0.0
	 */
	public static class Nvl extends Function{

		public Nvl() {
			super("nvl");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("nvl function only supports 2 arguments.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("nvl function need 2 arguments.we have " + getArgumentCount());
			}
			
			ExprValue value = getArgument(0).getValue(provider);
			if (value == null){
				value = getArgument(1).getValue(provider);
			}
			return value;
		}
		
	}
	
	/**
	 * Parse string value into date value
	 * 
	 * <br>
	 * Parse string value into date value with a date pattern.The default pattern is 'yyyyMMddHHmmss'
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * to_date(str_expr[,pattern])
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:date_expr
	 * @author duanyy
	 * @version 1.0.0
	 */
	public static class Char2Date extends Function{
		public Char2Date() {
			super("to_date");
			// TODO Auto-generated constructor stub
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("to_date function only supports 1 or 2 arguments.");
			}			
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() < 1){
				throw new FormulaException("to_date function need at least 1 argument.we have " + getArgumentCount());
			}
			
			String pattern = "yyyyMMddHHmmss";
			if (getArgument(1) != null){
				pattern = getArgument(1).getValue(provider).getString();
			}
			
			String value = getArgument(0).getValue(provider).getString();
			return new ExprValue(DateUtil.parseDate(value, pattern));
		}
	}
	/**
	 * Format date value to string value
	 * 
	 * <br>Format date value to string value with a date pattern.The default pattern is 'yyyyMMddHHmmss'
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * to_char(date_expr[,pattern])
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:string_expr
	 * @author duanyy
	 * @version 1.0.0
	 */
	public static class Date2Char extends Function{
		public Date2Char() {
			super("to_char");
			// TODO Auto-generated constructor stub
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("to_char function only supports 1 or 2 arguments.");
			}			
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() < 1){
				throw new FormulaException("to_char function need at least 1 argument.we have " + getArgumentCount());
			}
			
			String pattern = "yyyyMMddHHmmss";
			if (getArgument(1) != null){
				pattern = getArgument(1).getValue(provider).getString();
			}
			
			Date value = getArgument(0).getValue(provider).getDate();
			
			return new ExprValue(DateUtil.formatDate(value, pattern));
		}		
	}
	
	/**
	 * Convert string value to long value
	 * 
	 * <br>Convert string value to long value.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~
	 * to_long(string_expr)
	 * ~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:long_expr
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class ToLong extends Function{

		public ToLong() {
			super("to_long");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("to_long function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("to_long function need 1 argument.");
			}
			
			String value = getArgument(0).getValue(provider).getString();
			return new ExprValue(Long.valueOf(value));
		}
	}
	
	/**
	 * Convert string value to double value
	 * 
	 * Convert string value to double value.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~
	 * to_double(string_expr)
	 * ~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:double_expr
	 * 
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class ToDouble extends Function{

		public ToDouble() {
			super("to_double");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("to_double function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("to_double function need 1 argument.");
			}
			
			String value = getArgument(0).getValue(provider).getString();
			BigDecimal bg = new BigDecimal(value);
			return new ExprValue(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
	}
	/**
	 * Convert other value to string value
	 * 
	 * <br>Convert other value to string value.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~
	 * to_string(expr)
	 * ~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:string_expr
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class ToString extends Function{

		public ToString() {
			super("to_string");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("to_string function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("to_string function need 1 argument.");
			}
			
			String value = getArgument(0).getValue(provider).toString();
			return new ExprValue(value);
		}
	}	
	/**
	 * Get string length
	 * 
	 * <br>Get string length.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~
	 * strlen(str_expr)
	 * ~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:long
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class StrLen extends Function{

		public StrLen() {
			super("strlen");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("strlen function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("strlen function need 1 argument.");
			}
			
			String value = getArgument(0).getValue(provider).toString();
			return new ExprValue(value.length());
		}
	}	
	/**
	 * Get sub string from source string
	 * 
	 * <br>Get sub string from source string
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * substr(src_str,start_offset,length)
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:substring
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class SubStr extends Function{

		public SubStr() {
			super("substr");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 3){
				throw new FormulaException("substr function only supports 3 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 3){
				throw new FormulaException("substr function need 3 argument.");
			}
			
			String string = getArgument(0).getValue(provider).getString();
			int startOffset = getArgument(1).getValue(provider).getInt();
			int length = getArgument(2).getValue(provider).getInt();
			
			startOffset = startOffset < 0 ? 0 : startOffset >= string.length()?string.length() - 1:startOffset; 
			length = string.length() - startOffset < length ? string.length() - startOffset:length;
			return new ExprValue(string.substring(startOffset, startOffset + length));
		}
	}
	
	/**
	 * Find index in source string when child string is matched.
	 * 
	 * <br>Find index in source string when child string is matched.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~
	 * instr(src_str,child_str)
	 * ~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:index of child string,-1 if child string is not matched.
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class InStr extends Function{

		public InStr() {
			super("instr");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("instr function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("substr function need 2 argument.");
			}
			
			String srcString = getArgument(0).getValue(provider).getString();
			String childString = getArgument(1).getValue(provider).getString();
			
			return new ExprValue(srcString.indexOf(childString));
		}
	}

	/**
	 * Test whether string matches the given regular expression.
	 * 
	 * Test whether string matches the given regular expression.
	 * <p>Syntax:</p>
	 * <p>
	 * ~~~~~~~~~~~~~~~~~~~~~~~~
	 * match(src_str,regex)
	 * ~~~~~~~~~~~~~~~~~~~~~~~~
	 * </p>
	 * <p>return:true when src_str matches regex.
	 * @author duanyy
	 * @version 1.0.0
	 */	
	public static class Match extends Function{

		public Match() {
			super("match");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("match function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("match function need 2 argument.");
			}
			
			String srcString = getArgument(0).getValue(provider).getString();
			String regex = getArgument(1).getValue(provider).getString();
			
			return new ExprValue(srcString.matches(regex));
		}
	}
	
	// alexgaoyh add
	public static class PMT extends Function{

		public PMT() {
			super("PMT");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 5){
				throw new FormulaException("match function only supports 5 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 5){
				throw new FormulaException("match function need 5 argument.");
			}
			
			String r = getArgument(0).getValue(provider).getString();
			String n = getArgument(1).getValue(provider).getString();
			String p = getArgument(2).getValue(provider).getString();
			String f = getArgument(3).getValue(provider).getString();
			String t = getArgument(4).getValue(provider).getString();
			Double rDouble = Double.parseDouble(r);
			Double nDouble = Double.parseDouble(n);
			Double pDouble = Double.parseDouble(p);
			Double fDouble = Double.parseDouble(f);
			boolean tBoolean = Boolean.parseBoolean(t);
			
			Double resultDouble = FinanceFunctionUtilss.PMT(rDouble, nDouble, pDouble, fDouble, tBoolean);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class FV extends Function{

		public FV() {
			super("FV");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 5){
				throw new FormulaException("match function only supports 5 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 5){
				throw new FormulaException("match function need 5 argument.");
			}
			
			String r = getArgument(0).getValue(provider).getString();
			String n = getArgument(1).getValue(provider).getString();
			String y = getArgument(2).getValue(provider).getString();
			String p = getArgument(3).getValue(provider).getString();
			String t = getArgument(4).getValue(provider).getString();
			
			Double rDouble = Double.parseDouble(r);
			Double nDouble = Double.parseDouble(n);
			Double yDouble = Double.parseDouble(y);
			Double pDouble = Double.parseDouble(p);
			boolean tBoolean = Boolean.parseBoolean(t);
			
			Double resultDouble = FinanceFunctionUtilss.FV(rDouble, nDouble, yDouble, pDouble, tBoolean);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class NPER extends Function{

		public NPER() {
			super("NPER");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 5){
				throw new FormulaException("match function only supports 5 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 5){
				throw new FormulaException("match function need 5 argument.");
			}
			
			String r = getArgument(0).getValue(provider).getString();
			String y = getArgument(1).getValue(provider).getString();
			String p = getArgument(2).getValue(provider).getString();
			String f = getArgument(3).getValue(provider).getString();
			String t = getArgument(4).getValue(provider).getString();
			
			Double rDouble = Double.parseDouble(r);
			Double yDouble = Double.parseDouble(y);
			Double pDouble = Double.parseDouble(p);
			Double fDouble = Double.parseDouble(f);
			boolean tBoolean = Boolean.parseBoolean(t);
			
			Double resultDouble = FinanceFunctionUtilss.NPER(rDouble, yDouble, pDouble, fDouble, tBoolean);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class PV extends Function{

		public PV() {
			super("PV");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 5){
				throw new FormulaException("match function only supports 5 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 5){
				throw new FormulaException("match function need 5 argument.");
			}
			
			String r = getArgument(0).getValue(provider).getString();
			String n = getArgument(1).getValue(provider).getString();
			String y = getArgument(2).getValue(provider).getString();
			String f = getArgument(3).getValue(provider).getString();
			String t = getArgument(4).getValue(provider).getString();
			
			Double rDouble = Double.parseDouble(r);
			Double nDouble = Double.parseDouble(n);
			Double yDouble = Double.parseDouble(y);
			Double fDouble = Double.parseDouble(f);
			boolean tBoolean = Boolean.parseBoolean(t);
			
			Double resultDouble = FinanceFunctionUtilss.PV(rDouble, nDouble, yDouble, fDouble, tBoolean);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class DB extends Function{

		public DB() {
			super("DB");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 5){
				throw new FormulaException("match function only supports 5 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 5){
				throw new FormulaException("match function need 5 argument.");
			}
			
			String cost = getArgument(0).getValue(provider).getString();
			String salvage = getArgument(1).getValue(provider).getString();
			String life = getArgument(2).getValue(provider).getString();
			String period = getArgument(3).getValue(provider).getString();
			String month = getArgument(4).getValue(provider).getString();
			
			Double costDouble = Double.parseDouble(cost);
			Double salvageDouble = Double.parseDouble(salvage);
			int lifeInt = Integer.parseInt(life);
			int periodInt = Integer.parseInt(period);
			int monthInt = Integer.parseInt(month);
			
			Double resultDouble = FinanceFunctionUtilss.DB(costDouble, salvageDouble, lifeInt, periodInt, monthInt);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class DDB extends Function{

		public DDB() {
			super("DDB");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 5){
				throw new FormulaException("match function only supports 5 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 5){
				throw new FormulaException("match function need 5 argument.");
			}
			
			String cost = getArgument(0).getValue(provider).getString();
			String salvage = getArgument(1).getValue(provider).getString();
			String life = getArgument(2).getValue(provider).getString();
			String period = getArgument(3).getValue(provider).getString();
			String factor = getArgument(4).getValue(provider).getString();
			
			Double costDouble = Double.parseDouble(cost);
			Double salvageDouble = Double.parseDouble(salvage);
			int lifeInt = Integer.parseInt(life);
			int periodInt = Integer.parseInt(period);
			Double factorDouble = Double.parseDouble(factor);
			
			Double resultDouble = FinanceFunctionUtilss.DDB(costDouble, salvageDouble, lifeInt, periodInt, factorDouble);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class RATE extends Function{

		public RATE() {
			super("RATE");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 6){
				throw new FormulaException("match function only supports 6 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 6){
				throw new FormulaException("match function need 6 argument.");
			}
			
			String nper = getArgument(0).getValue(provider).getString();
			String pmt = getArgument(1).getValue(provider).getString();
			String pv = getArgument(2).getValue(provider).getString();
			String fv = getArgument(3).getValue(provider).getString();
			String type = getArgument(4).getValue(provider).getString();
			String guess = getArgument(5).getValue(provider).getString();
			
			Double nperDouble = Double.parseDouble(nper);
			Double pmtDouble = Double.parseDouble(pmt);
			Double pvDouble = Double.parseDouble(pv);
			Double fvDouble = Double.parseDouble(fv);
			Double typeDouble = Double.parseDouble(type);
			Double guessDouble = Double.parseDouble(guess);
			
			Double resultDouble = FinanceFunctionUtilss.RATE(nperDouble, pmtDouble, pvDouble, 
					fvDouble, typeDouble, guessDouble);
			
			return new ExprValue(resultDouble);
		}
	}
	
	public static class EFFECT extends Function{

		public EFFECT() {
			super("EFFECT");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("match function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("match function need 2 argument.");
			}
			
			String nominal_rate = getArgument(0).getValue(provider).getString();
			String npery = getArgument(1).getValue(provider).getString();
			
			Double nominal_rateDouble = Double.parseDouble(nominal_rate);
			Double nperyDouble = Double.parseDouble(npery);
			
			Double resultDouble = FinanceFunctionUtilss.EFFECT(nominal_rateDouble, nperyDouble);
			
			return new ExprValue(resultDouble);
		}
	}
	
	//
	public static class IF extends Function{

		public IF() {
			super("IF");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 3){
				throw new FormulaException("match function only supports 3 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 3){
				throw new FormulaException("match function need 3 argument.");
			}
			
			boolean logical_test_boolean = getArgument(0).getValue(provider).getBoolean();
			String value_if_true = getArgument(1).getValue(provider).getString();
			String value_if_false = getArgument(2).getValue(provider).getString();
			
			Double resultDouble = Double.valueOf("0.00");
			if(logical_test_boolean) {
				resultDouble = Double.valueOf(value_if_true);
			} else {
				resultDouble = Double.valueOf(value_if_false);
			}
			return new ExprValue(resultDouble);
		}
	}
	
	//
	public static class DEVIDE extends Function{

		public DEVIDE() {
			super("DEVIDE");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("match function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("match function need 2 argument.");
			}
			
			String divisor = getArgument(0).getValue(provider).getString();
			String dividend = getArgument(1).getValue(provider).getString();
			
			BigDecimal divisorBigDecimal = new BigDecimal(divisor);
			BigDecimal dividendBigDecimal = new BigDecimal(dividend);
			
			BigDecimal returnBigDecimal = divisorBigDecimal.divide(dividendBigDecimal, 10, BigDecimal.ROUND_CEILING);
			String returnStr = returnBigDecimal.toString();
			Double returnDouble = Double.parseDouble(returnStr);
			return new ExprValue(returnDouble);
		}
	}
	
	//
	public static class ROUND extends Function{

		public ROUND() {
			super("ROUND");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("match function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("match function need 2 argument.");
			}
			
			// 数值
			String value = getArgument(0).getValue(provider).getString();
			// 小数位数
			String digits = getArgument(1).getValue(provider).getString();
			
			BigDecimal valueBig = new BigDecimal(value);
			int digitsInt = Integer.parseInt(digits);
			
			BigDecimal returnBigDecimal = valueBig.setScale(digitsInt, BigDecimal.ROUND_HALF_UP);
			String returnStr = returnBigDecimal.toString() + "";
			Double returnDouble = Double.parseDouble(returnStr);
			return new ExprValue(returnDouble);
		}
	}
	
	public static class ROUNDUP extends Function{

		public ROUNDUP() {
			super("ROUNDUP");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("match function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("match function need 2 argument.");
			}
			
			// 数值
			String value = getArgument(0).getValue(provider).getString();
			// 小数位数
			String digits = getArgument(1).getValue(provider).getString();
			
			BigDecimal valueBig = new BigDecimal(value);
			int digitsInt = Integer.parseInt(digits);
			
			BigDecimal returnBigDecimal = valueBig.setScale(digitsInt, BigDecimal.ROUND_UP);
			String returnStr = returnBigDecimal.floatValue() + "";
			Double returnDouble = Double.parseDouble(returnStr);
			return new ExprValue(returnDouble);
		}
	}
	
	/**
	 * 转换为整数
	 * @author alexgaoyh
	 *
	 */
	public static class TOINT extends Function{

		public TOINT() {
			super("TO_INT");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("TO_INT function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("TO_INT function need 1 argument.");
			}
			
			int value = getArgument(0).getValue(provider).getInt();
			return new ExprValue(value);
		}
	}
	
	/**
	 * 科学计数法相关
	 * @author alexgaoyh
	 *
	 */
	public static class ENUMBER extends Function{

		public ENUMBER() {
			super("E_NUMBER");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("E_NUMBER function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("E_NUMBER function need 1 argument.");
			}
			
			String value = getArgument(0).getValue(provider).toString();
			BigDecimal valueBig = new BigDecimal(value);
			String returnValue = valueBig.toPlainString();
			Double returnDouble = Double.parseDouble(returnValue);
			return new ExprValue(returnDouble);
		}
	}
	
	
	public static class NUMDIGIT extends Function{

		public NUMDIGIT() {
			super("NUMDIGIT");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 1){
				throw new FormulaException("NUMDIGIT function only supports 1 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 1){
				throw new FormulaException("NUMDIGIT function need 1 argument.");
			}
			
			String value = getArgument(0).getValue(provider).toString();
			String returnValue = new java.text.DecimalFormat("#.00").format(Double.parseDouble(value));
			Double returnDouble = Double.parseDouble(returnValue);
			return new ExprValue(returnDouble);
		}
	}
	
	public static class EYUSHU extends Function{

		public EYUSHU() {
			super("EYUSHU");
		}

		@Override
		public void checkArgument(Expression arg) throws FormulaException {
			if (getArgumentCount() > 2){
				throw new FormulaException("EYUSHU function only supports 2 argument.");
			}	
		}

		@Override
		public ExprValue getValue(DataProvider provider)
				throws FormulaException {
			if (getArgumentCount() != 2){
				throw new FormulaException("EYUSHU function need 2 argument.");
			}
			
			String before = getArgument(0).getValue(provider).toString();
			String end = getArgument(1).getValue(provider).toString();
			Float beforeFloat = Float.parseFloat(before);
			Float endFloat = Float.parseFloat(end);
			float size = (float)beforeFloat/endFloat;
			String returnValue = new java.text.DecimalFormat("#.00").format(size);
			Double returnDouble = Double.parseDouble(returnValue);
			return new ExprValue(returnDouble);
		}
	}
}

package com.pap.calculate.anysoft.formula;

import java.util.Hashtable;

/**
 * 函数对象辅助器
 * 
 * <br>
 * 函数对象辅助器维护了一个函数的映射表，并根据框架的要求生成相应函数的实例<br>
 * @author duanyy
 * 
 */
@SuppressWarnings("unchecked")
public class DefaultFunctionHelper implements FunctionHelper {

	/**
	 * 父节点
	 */
	protected FunctionHelper parent = null;

	/**
	 * 构造函数 
	 * @param _parent 父节点
	 */
	public DefaultFunctionHelper(FunctionHelper _parent) {
		parent = _parent;
	}

	/**
	 * 构造函数
	 */
	public DefaultFunctionHelper() {
		this(null);
	}

	/**
	 * 函数映射表
	 */
	protected Hashtable<String, Class<Function>> mappings = new Hashtable<String, Class<Function>>();

	/**
	 * 注册函数映射
	 * @param funcName 函数名
	 * @param funClass 对应的函数实现的类名
	 */
	public void addFunction(String funcName, Class<Function> funClass) {
		mappings.put(funcName, funClass);
	}

	/**
	 * 注销函数映射
	 * @param funcName 函数名
	 */
	public void removeFunction(String funcName) {
		mappings.remove(funcName);
	}

	@Override
	public Expression customize(String funcName) {
		try {
			Class<Function> found = static_mappings.get(funcName);
			if (found != null) {
				return found.newInstance();
			}
			found = mappings.get(funcName);
			if (found != null) {
				return found.newInstance();
			}
			if (parent != null) {
				return parent.customize(funcName);
			}
			throw new FormulaException("Can not find function :" + funcName);
		} catch (Exception ex) {
			throw new FormulaException("Can not create function :" + funcName);
		}
	}

	/**
	 * 内置静态的函数映射表
	 */
	protected static Hashtable<String, Class<Function>> static_mappings 
	= new Hashtable<String, Class<Function>>();
	
	/**
	 * 内置的函数
	 */
	static {
		try {
			static_mappings.put("choice", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$Choice"));
			static_mappings.put("nvl", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$Nvl"));
			static_mappings.put("to_date", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$Char2Date"));
			static_mappings.put("to_char", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$Date2Char"));
			static_mappings.put("to_string", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$ToString"));
			static_mappings.put("to_long", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$ToLong"));
			static_mappings.put("to_double", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$ToDouble"));
			static_mappings.put("substr", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$SubStr"));
			static_mappings.put("instr", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$InStr"));
			static_mappings.put("strlen", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$StrLen"));
			static_mappings.put("match", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$Match"));
			// alexgaoyh add finance function
			static_mappings.put("PMT", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$PMT"));
			static_mappings.put("FV", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$FV"));
			static_mappings.put("NPER", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$NPER"));
			static_mappings.put("PV", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$PV"));
			static_mappings.put("DB", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$DB"));
			static_mappings.put("DDB", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$DDB"));
			static_mappings.put("RATE", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$RATE"));
			static_mappings.put("EFFECT", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$EFFECT"));
			static_mappings.put("IF", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$IF"));
			static_mappings.put("DEVIDE", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$DEVIDE"));
			static_mappings.put("ROUND", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$ROUND"));
			static_mappings.put("ROUNDUP", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$ROUNDUP"));
			static_mappings.put("TOINT", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$TOINT"));
			static_mappings.put("E_NUMBER", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$ENUMBER"));
			static_mappings.put("NUM_DIGIT", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$NUMDIGIT"));
			static_mappings.put("E_YUSHU", (Class<Function>) Class
					.forName("com.pap.calculate.anysoft.formula.Function$EYUSHU"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
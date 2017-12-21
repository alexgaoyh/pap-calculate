package com.pap.calculate.anysoft.formula;

import org.apache.poi.ss.formula.functions.FinanceLib;

public class FinanceFunctionUtilss {
	
	
	public FinanceFunctionUtilss() {
	}

	/**
	 * 基于固定利率及等额分期付款方式，返回贷款的每期付款额。
	 * 
	 * @param r
	 *            必需。贷款利率
	 * @param n
	 *            必需。该项贷款的付款总数。
	 * @param p
	 *            必需。现值，或一系列未来付款的当前值的累积和，也称为本金。
	 * @param f
	 *            必须。未来值，或在最后一次付款后希望得到的现金余额，如果省略 fv，则假设其值为 0（零），也就是一笔贷款的未来值为 0。
	 * @param t
	 *            必须。数字 0（零）或 1，用以指示各期的付款时间是在期初还是期末。 0 或省略 期末 false 1 期初 true
	 * @return
	 */
	public static double PMT(double r, double n, double p, double f, boolean t) {
		return FinanceLib.pmt(r, n, p, f, t);
	}

	/**
	 * 基于固定利率及等额分期付款方式，返回某项投资的未来值。
	 * 
	 * @param r
	 *            必需。贷款利率
	 * @param n
	 *            必需。该项贷款的付款总数。
	 * @param y
	 *            必需。各期所应支付的金额，其数值在整个年金期间保持不变。通常，pmt 包括本金和利息，但不包括其他费用或税款。
	 * @param p
	 *            必需。现值，或一系列未来付款的当前值的累积和，也称为本金。
	 * @param t
	 *            必须。数字 0（零）或 1，用以指示各期的付款时间是在期初还是期末。 0 或省略 期末 1 期初
	 * 
	 */
	public static double FV(double r, double n, double y, double p, boolean t) {
		return FinanceLib.fv(r, n, y, p, t);
	}

	/**
	 * 基于固定利率及等额分期付款方式，返回某项投资的总期数。
	 * 
	 * @param r
	 *            必需。各期利率。
	 * @param y
	 *            必需。各期所应支付的金额，其数值在整个年金期间保持不变。通常，pmt 包括本金和利息，但不包括其他费用或税款。
	 * @param p
	 *            必需。现值，或一系列未来付款的当前值的累积和。
	 * @param f
	 *            必需。未来值，或在最后一次付款后希望得到的现金余额。如果省略 fv，则假设其值为 0（例如，一笔贷款的未来值即为 0）。
	 * @param t
	 *            必需。数字 0 或 1，用以指定各期的付款时间是在期初还是期末。 0 或省略 期末 1 期初
	 * @return
	 */
	public static double NPER(double r, double y, double p, double f, boolean t) {
		return FinanceLib.nper(r, y, p, f, t);
	}

	/**
	 * 基于固定利率及等额分期付款方式，返回某项投资的未来值。
	 * 
	 * @param r
	 *            各期利率
	 * @param n
	 *            总投资期，即该项投资的付款期总数
	 * @param y
	 *            必需。各期所应支付的金额，其数值在整个年金期间保持不变。通常，pmt 包括本金和利息，但不包括其他费用或税款。
	 * @param f
	 *            必需。未来值，或在最后一次付款后希望得到的现金余额。如果省略 fv，则假设其值为 0（例如，一笔贷款的未来值即为 0）。
	 * @param t
	 *            必需。数字 0 或 1，用以指定各期的付款时间是在期初还是期末。 0 或省略 期末 1 期初
	 */
	public static double PV(double r, double n, double y, double f, boolean t) {
		return FinanceLib.pv(r, n, y, f, t);
	}

	public static double DB(double cost, double salvage, int life, int period,
			int month) {
		double rate = 1 - Math.pow(salvage / cost, 1. / life);
		rate = round(rate, 3);
		double dep = 0;
		double total = 0;
		for (int i = 0; i < period; i++) {
			if (i == 0) {
				total = dep = cost * rate * month / 12;
			} else if (i == life) {
				dep = ((cost - total) * rate * (12 - month)) / 12;
			} else {
				dep = (cost - total) * rate;
				total += dep;
			}
		}
		return dep;
	}

	public static double DDB(double cost, double salvage, int life, int period,
			double factor) {
		double dep = 0;
		double total = 0;
		double rate = factor / life;
		for (int i = 0; i < period; i++) {
			dep = ((cost - salvage) - total) * rate;
			total += dep;
		}
		return dep;
	}

	public static double RATE(double nper, double pmt, double pv, double fv,
			double type, double guess) {

		int FINANCIAL_MAX_ITERATIONS = 20;
		double FINANCIAL_PRECISION = 0.0000001;

		double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
		double rate = guess;
		if (Math.abs(rate) < FINANCIAL_PRECISION) {
			y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
		} else {
			f = Math.exp(nper * Math.log(1 + rate));
			y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
		}
		y0 = pv + pmt * nper + fv;
		y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

		i = x0 = 0.0;
		x1 = rate;
		while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION)
				&& (i < FINANCIAL_MAX_ITERATIONS)) {
			rate = (y1 * x0 - y0 * x1) / (y1 - y0);
			x0 = x1;
			x1 = rate;

			if (Math.abs(rate) < FINANCIAL_PRECISION) {
				y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper
						+ fv;
			} else {
				f = Math.pow(1 + rate, nper);
				y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
			}

			y0 = y1;
			y1 = y;
			++i;
		}
		return rate;
	}

	public static double EFFECT(double nominal_rate, double npery) {
		return Math.pow((1 + (nominal_rate / npery)), npery) - 1;
	}
	
	private static double round(double value, int dps) {
		boolean n = value < 0;
		if (n)
			value *= -1;
		double r = ((double) Math.round(value * Math.pow(10, dps)))
				/ Math.pow(10, dps);
		if (n)
			r *= -1;
		return r;
	}
}

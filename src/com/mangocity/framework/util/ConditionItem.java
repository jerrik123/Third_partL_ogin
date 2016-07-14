package com.mangocity.framework.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 这个类的主要功能是封装查询条件
 * 
 * @author chenjun
 * @since 1.0
 * @see Serializable
 */
public class ConditionItem implements Serializable {

	private static final long serialVersionUID = 542004143155918676L;

	public static final String EQ = "=";

	public static final String GT = ">";

	public static final String LT = "<";

	public static final String GE = ">=";

	public static final String LE = "<=";

	public static final String NE = "<>";

	public static final String LK = "LIKE";

	public static final String IN = "IN";

	public static final String NL = "IS NULL";

	public static final String NN = "IS NOT NULL";

	public static final String OR = "OR";

	public static final String IN_SQL = " IN ";

	public static final String EQFORUNIT = "EQFORUNIT";

	public static final String DESC = "DESC";

	public static final String ASC = "ASC";

	private String name;

	private Object value;

	private String operator; // "=", ">", ">=", "<", "<=", "LIKE", "IN", "OR"

	private List<ConditionItem> leftCondition = new LinkedList<ConditionItem>();

	private List<ConditionItem> rightCondition = new LinkedList<ConditionItem>();

	private List<ConditionItem> restCondition = new LinkedList<ConditionItem>();

	/**
	 * 构造 "name = value" 条件，只传入name和value两个参数，默认operator为"="。
	 * 如name为user.name，value为cmb，则这条件为user.name = cmb。
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	public ConditionItem(String name, Object value) {
		this(name, value, ConditionItem.EQ);
	}

	/**
	 * 构造 "name operator value" 条件，只传入name,value和operator三个参数。
	 * 如name为user.name，value为cmb，operator为like，则这条件为user.name like '%cmb%'。
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 * @param operator
	 *            操作
	 */
	public ConditionItem(String name, Object value, String operator) {
		this.name = name;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * 构造 "leftCondition or rightCondition" 条件。 将两个ConditionItem拼装成or关系的查询条件
	 * 
	 * @param leftCondition
	 *            ConditionItem
	 * @param rightCondition
	 *            ConditionItem
	 */
	public ConditionItem(List<ConditionItem> leftCondition,
			List<ConditionItem> rightCondition) {
		this.leftCondition = leftCondition;
		this.rightCondition = rightCondition;
		this.operator = ConditionItem.OR;
	}

	/**
	 * 构造构造 "leftCondition or rightCondition or restCondition" 条件。
	 * 将三个ConditionItem拼装成or关系的查询条件
	 * 
	 * @param leftCondition
	 *            ConditionItem
	 * @param rightCondition
	 *            ConditionItem
	 * @param restCondition
	 *            ConditionItem
	 */
	public ConditionItem(List<ConditionItem> leftCondition,
			List<ConditionItem> rightCondition,
			List<ConditionItem> restCondition) {
		this.leftCondition = leftCondition;
		this.rightCondition = rightCondition;
		this.restCondition = restCondition;
		this.operator = ConditionItem.OR;
	}

	public List<ConditionItem> getRestCondition() {
		return restCondition;
	}

	public void setRestCondition(List<ConditionItem> restCondition) {
		this.restCondition = restCondition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<ConditionItem> getLeftCondition() {
		return leftCondition;
	}

	public void setLeftCondition(List<ConditionItem> leftCondition) {
		this.leftCondition = leftCondition;
	}

	public List<ConditionItem> getRightCondition() {
		return rightCondition;
	}

	public void setRightCondition(List<ConditionItem> rightCondition) {
		this.rightCondition = rightCondition;
	}
}

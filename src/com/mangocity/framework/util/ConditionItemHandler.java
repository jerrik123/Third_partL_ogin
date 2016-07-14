package com.mangocity.framework.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mangocity.util.DateTimeUtils;

/**
 * 查询条件项处理器
 */
public class ConditionItemHandler {

	private static String dateTemplate = "yyyy-MM-dd HH:mm:ss";
	
	private static String oracleDateTemplate ="yyyy-mm-dd hh24:mi:ss";

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			dateTemplate);

	public static final boolean isOracle = false;

	@SuppressWarnings("unchecked")
	public static StringBuffer parseWhereCondition(StringBuffer selectSql,
			List<ConditionItem> condition) {
		// 如果条件为空,则返回原sql
		if (condition == null) {
			return selectSql;
		}
		// where语句变量
		StringBuffer whereSql = new StringBuffer();
		// orderby字符串变量
		String orderbystr = "";
		// 条件迭代器
		Iterator<ConditionItem> iter = condition.iterator();
		boolean hasBegin = false;

		while (iter.hasNext()) {
			String sqlString = null;
			// 得到一个条件对象
			ConditionItem item = iter.next();
			// 条件名
			String name = item.getName();
			// 条件值
			Object objValue = item.getValue();

			// 如果值是null并且操作不是NL，NN和OR的话，跳过此condition的处理
			if (objValue == null
					&& !(item.getOperator().equals(ConditionItem.NL))
					&& !(item.getOperator().equals(ConditionItem.NN))
					&& !(item.getOperator().equals(ConditionItem.OR))) {
				continue;
			}

			// 加排序
			if (item.getOperator().equals(ConditionItem.DESC)
					|| item.getOperator().equals(ConditionItem.ASC)) {
				orderbystr = orderbystr + "  " + name + " " + "  "
						+ item.getOperator() + ",";
				continue;
			}

			// 对于值是空字符串的condition跳过处理
			if (objValue instanceof java.lang.String) {
				if (objValue != null && ((String) objValue).trim().equals("")) {
					continue;
				}
			}

			// 对可能有左条件、右条件或约束条件的OR条件处理
			StringBuffer orBuffer = null;
			if (item.getOperator().equals(ConditionItem.OR)) {
				orBuffer = new StringBuffer();
				// 左条件处理
				String leftSql = ConditionItemHandler.parseSingleCondition(item
						.getLeftCondition());
				if (leftSql != null && !"".equals(leftSql.trim())) {
					orBuffer.append("(");
					orBuffer.append(leftSql);
					orBuffer.append(")");
				}

				// 右条件处理
				String rightSql = ConditionItemHandler
						.parseSingleCondition(item.getRightCondition());
				if (rightSql != null && !"".equals(rightSql.trim())) {
					if (leftSql != null && !"".equals(leftSql.trim())) {
						orBuffer.append(ConditionItem.OR);
					}
					orBuffer.append("(");
					orBuffer.append(rightSql);
					orBuffer.append(")");
				}

				// 约束条件
				if (item.getRestCondition().size() > 0) {
					String restSql = ConditionItemHandler
							.parseSingleCondition(item.getRestCondition());

					if (restSql != null && !"".equals(restSql.trim())) {
						if ((leftSql != null && !"".equals(leftSql.trim()))
								|| (rightSql != null && !"".equals(rightSql
										.trim()))) {
							orBuffer.append(ConditionItem.OR);
						}
						orBuffer.append("(");
						orBuffer.append(restSql);
						orBuffer.append(")");
					}
				}

				if (orBuffer.length() == 0) {
					continue;
				}

			}

			// 对于value是空List对象的condition跳过处理
			if (objValue instanceof java.util.List) {
				List list = (List) objValue;
				if (list.size() == 0) {
					continue;
				}
			}

			// 判断whereSql是否已经包含"where"字符串，有的话添加"and"字符串，否则添加"where"字符串
			if (hasBegin) {
				whereSql.append(" AND ");
			} else {
				whereSql.append(" WHERE ");
			}

			// 对操作为OR的条件进行处理
			if (item.getOperator().equals(ConditionItem.OR)) {
				if (orBuffer != null && orBuffer.length() != 0) {
					whereSql.append("(");
					whereSql.append(orBuffer);
					whereSql.append(")");
					hasBegin = true;
					continue;
				}
			}

			// is null和is not null的处理
			if (item.getOperator().equals(ConditionItem.NL)
					|| item.getOperator().equals(ConditionItem.NN)) {
				whereSql
						.append(" (" + name + " " + item.getOperator() + " OR ");
				whereSql.append(name + " = '')");
			} else if (item.getOperator().equals(ConditionItem.IN_SQL)) {// in的处理
				whereSql.append(" " + name + " " + item.getOperator() + "("
						+ objValue + ") ");
			} else if (objValue instanceof java.lang.String) {// value是String的处理
				if (item.getOperator().equalsIgnoreCase("LIKE")) {// like的处理
					sqlString = "'%" + objValue.toString() + "%'";
					whereSql.append(name + " " + item.getOperator() + " "
							+ sqlString);
				} else if (item.getOperator().equalsIgnoreCase("EQFORUNIT")) {// eqforunit的处理，sqlString两边未加单引号
					sqlString = objValue.toString();
					whereSql.append(name + " " + ConditionItem.EQ + " "
							+ sqlString);
				} else {// 其他的情况
					sqlString = "'" + objValue.toString() + "'";
					whereSql.append(name + " " + item.getOperator() + " "
							+ sqlString);
				}
			} else if (objValue instanceof java.util.Date) {// value是Date类型的处理
				sqlString = dateFormat.format((Date) objValue);
				// oracle数据库的时候用下面的处理方式
				whereSql.append(" " + name + " " + item.getOperator() + " to_date('"
						+ sqlString + "', '" + oracleDateTemplate + "')");
			} else if (objValue instanceof java.util.List) {// value是List的处理
				List list = (List) objValue;

				whereSql.append(" " + name);
				whereSql.append(" " + item.getOperator() + " (");
				Iterator iter1 = list.iterator();
				boolean hasBegin1 = false;
				while (iter1.hasNext()) {
					if (hasBegin1) {
						whereSql.append(", ");
					}
					whereSql.append("'" + ((String) iter1.next()) + "'");
					hasBegin1 = true;
				}
				whereSql.append(")");
			} else {
				sqlString = objValue.toString();
				whereSql.append("  " + name + " " + item.getOperator() + " "
						+ sqlString);
			}
			hasBegin = true;
		}

		// orderby处理
		if (orderbystr != null && orderbystr.length() > 1) {
			orderbystr = orderbystr.substring(0, orderbystr.length() - 1);
			whereSql.append("  order by  " + orderbystr);// 有distinct
			// 就不支持order by
			// rownum

		}

		// 判断selectSql是否有"where"字符串，如果有的话在其后添加"and"和剩余的条件
		if (selectSql.toString().toUpperCase().indexOf("WHERE") > 0) {
			int whereIndex = whereSql.indexOf("WHERE");
			if (whereIndex > -1) {
				// selectSql - 'where' + 'and' + whereSql
				selectSql.append(" AND ");
				selectSql.append(whereSql.toString().substring(whereIndex + 5));
			} else {
				// selectSql
				return selectSql.append(whereSql.toString());
			}
		} else {
			// selectSql + whereSql
			return selectSql.append(whereSql.toString());
		}
		return selectSql;
	}

	@SuppressWarnings("unchecked")
	private static String parseSingleCondition(List<ConditionItem> condition) {
		if (condition == null) {
			return "";
		}
		StringBuffer sql = new StringBuffer();
		String orderbystr = "";
		Iterator<ConditionItem> iter = condition.iterator();
		boolean hasBegin = false;
		while (iter.hasNext()) {
			String sqlString = null;
			ConditionItem item = (ConditionItem) iter.next();
			String name = item.getName();
			Object objValue = item.getValue();
			if (objValue == null
					&& !(item.getOperator().equals(ConditionItem.NL))
					&& !(item.getOperator().equals(ConditionItem.NN))
					&& !(item.getOperator().equals(ConditionItem.OR))) {
				continue;
			}
			// 加排序
			if (item.getOperator().equals(ConditionItem.DESC)
					|| item.getOperator().equals(ConditionItem.ASC)) {
				orderbystr = orderbystr + "  " + name + "  "
						+ item.getOperator() + ",";
				continue;
			}
			if (objValue instanceof java.lang.String) {
				if (objValue != null && ((String) objValue).trim().equals("")) {
					continue;
				}
			}
			if (hasBegin) {
				sql.append(" AND ");
			}
			if (item.getOperator().equals(ConditionItem.NL)
					|| item.getOperator().equals(ConditionItem.NN)) {
				sql.append(" (" + name + " " + item.getOperator() + " OR ");
				sql.append(name + " = '')");
			} else if (objValue instanceof java.lang.String) {
				if (item.getOperator().equalsIgnoreCase(ConditionItem.LK)) {
					sqlString = "'%" + objValue.toString() + "%'";
					sql.append(name + " " + item.getOperator() + " "
							+ sqlString);
				} else if (item.getOperator().equalsIgnoreCase("EQFORUNIT")) {
					sqlString = objValue.toString();
					sql.append(name + " " + ConditionItem.EQ + " " + sqlString);
				} else {
					sqlString = "'" + objValue.toString() + "'";
					sql.append(name + " " + item.getOperator() + " "
							+ sqlString);
				}
			} else if (objValue instanceof java.util.Date) {
				sqlString = dateFormat.format((Date) objValue);
				sql.append(" " + name + " " + item.getOperator() + " to_date('"
						+ sqlString + "', '" + oracleDateTemplate + "')");
			} else if (objValue instanceof java.util.List) {
				List list = (List) objValue;
				if (list.size() == 0) {
					continue;
				}
				sql.append("  " + name + " IN (");
				Iterator iter1 = list.iterator();
				boolean hasBegin1 = false;
				while (iter1.hasNext()) {
					if (hasBegin1) {
						sql.append(", ");
					}
					sql.append("'" + ((String) iter1.next()) + "'");
					hasBegin1 = true;
				}
				sql.append(")");
			} else {
				sqlString = objValue.toString();
				sql.append("  " + name + " " + item.getOperator() + " "
						+ sqlString);
			}
			hasBegin = true;
		}
		if (orderbystr != null && orderbystr.length() > 1) {
			orderbystr = orderbystr.substring(0, orderbystr.length() - 1);
			sql.append("  order by  " + orderbystr);
		}
		return sql.toString();
	}

	public static void addTwoDate(List<ConditionItem> conditions,
			String stringDate1, String stringDate2, String columnName) {
		Date date1 = DateTimeUtils.parseDate(stringDate1);
		Date date2 = DateTimeUtils.parseDate(stringDate2);

		if (date1 != null || date2 != null) {
			Calendar calendar = Calendar.getInstance();

			if (date1 == null) {
				date1 = date2;
				calendar.setTime(date2);
			} else if (date2 == null) {
				calendar.setTime(date1);
			} else {
				calendar.setTime(date2);
			}
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
			date2 = calendar.getTime();

			conditions.add(new ConditionItem(columnName, date1,
					ConditionItem.GE));
			conditions.add(new ConditionItem(columnName, date2,
					ConditionItem.LT));
		}
	}

	public static void addTwoDate(List<ConditionItem> conditions, Date date1,
			Date date2, String columnName) {

		if (date1 != null || date2 != null) {
			if (date1 == null) {
				conditions.add(new ConditionItem(columnName, date2));
			} else if (date2 == null) {
				conditions.add(new ConditionItem(columnName, date1));
			} else {
				conditions.add(new ConditionItem(columnName, date1,
						ConditionItem.GE));
				conditions.add(new ConditionItem(columnName, date2,
						ConditionItem.LE));
			}
		}
	}

	// 调试
	public static void main(String[] args) {
		List<ConditionItem> conditList = new ArrayList<ConditionItem>();
		conditList.add(new ConditionItem("c.operator", "guagua",
				ConditionItem.EQ));
		conditList.add(new ConditionItem("c.id", "", ConditionItem.ASC));
		StringBuffer sf = new StringBuffer();
		parseWhereCondition(sf, conditList);
		System.out.println(sf.toString());
	}
}

package com.mangocity.framework.base.domain.entity;

import java.io.Serializable;
import java.util.Date;

/** 实体类的基类 */
public class Entity implements Serializable {

	private static final long serialVersionUID = 7216957352724158130L;

	/** id:数据的主键 */
	protected Long id;

	/** 版本戳,自增长，保存一次增一 */
	protected Integer version;

	/** 数据的名称 */
	protected String name;

	/** 数据的创建者 */
	protected String creator;

	/** 数据的操作者 */
	protected String operator;

	/** 数据的创建日期 */
	protected Date createDate;

	/** 数据的操作日期 */
	protected Date operateDate;

	/** 数据的状态（0：非激活状态 1：激活状态） */
	protected Integer activity = 1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getActivity() {
		return activity;
	}

	public void setActivity(Integer activity) {
		this.activity = activity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 若主键id相等，则视为相同对象
	 */
	@Override
	public boolean equals(Object obj) {
		if (id == null)
			return super.equals(obj);
		else if (obj.getClass() == this.getClass()) {
			return (id == (((Entity) obj).id));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (id == null)
			return super.hashCode();
		else
			return id.hashCode();
	}

}

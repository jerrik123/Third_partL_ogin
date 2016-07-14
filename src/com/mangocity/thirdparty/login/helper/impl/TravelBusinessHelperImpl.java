package com.mangocity.thirdparty.login.helper.impl;

import org.apache.commons.lang.StringUtils;

import com.mangocity.model.mbrship.MbrshipCategory;
import com.mangocity.services.mbrship.IMbrshipCategoryService;
import com.mangocity.thirdparty.login.helper.ItravelBusinessHelper;

public class TravelBusinessHelperImpl implements ItravelBusinessHelper {
	
	/**
	 * 调用EJB会籍类型服务接口
	 */
	private IMbrshipCategoryService mbrhisCateService;
	
	/**
	 * 查询是否是商旅会籍会籍
	 * @return
	 */
	public String getIsTravelBusiness(String categoryCd) throws Exception{
		MbrshipCategory mbrshipCategoryTemp = null;
		String isTravelbusinessStr = "N";
		try {
			mbrshipCategoryTemp = mbrhisCateService.mbrshipCategoryByCategoryCd(categoryCd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mbrshipCategoryTemp != null) {
			if (StringUtils.isNotEmpty(mbrshipCategoryTemp.getCategoryTyp()) && "2".equals(mbrshipCategoryTemp.getCategoryTyp())) {
				isTravelbusinessStr = "Y";
			}
		}	
		return isTravelbusinessStr;
	}

	public IMbrshipCategoryService getMbrhisCateService() {
		return mbrhisCateService;
	}

	public void setMbrhisCateService(IMbrshipCategoryService mbrhisCateService) {
		this.mbrhisCateService = mbrhisCateService;
	}

}

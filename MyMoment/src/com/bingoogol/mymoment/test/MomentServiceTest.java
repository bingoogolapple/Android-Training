package com.bingoogol.mymoment.test;

import junit.framework.Assert;
import android.test.AndroidTestCase;

import com.bingoogol.mymoment.domain.Moment;
import com.bingoogol.mymoment.service.MomentService;
import com.bingoogol.mymoment.util.DateUtil;

public class MomentServiceTest extends AndroidTestCase {
	public void testAddMoment() {
		MomentService momentService = new MomentService(getContext());
		Moment moment = new Moment();
		moment.setContent("测试内容斯蒂芬所的");
		moment.setImgPath("测试图片路径");
		moment.setPublishTime(DateUtil.getPublishTime());
		Assert.assertTrue(momentService.addMoment(moment));
	}
}

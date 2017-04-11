package com.service;

import java.util.HashMap;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

public class SendTemplateSMS {
	
	public static String validateCode="";
	public static boolean flag=true; 
	/**
	 * @param args
	 */
	public static boolean  sendMessage(String PhoneNumber) {
		HashMap<String, Object> result = null;

		// 初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

		// ******************************注释*********************************************
		// *初始化服务器地址和端�?*
		// *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		// *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
		// *******************************************************************************
		restAPI.init("sandboxapp.cloopen.com", "8883");

		// ******************************注释*********************************************
		// *初始化主帐号和主帐号令牌,对应官网�?��者主账号下的ACCOUNT SID和AUTH TOKEN *
		// *ACOUNT SID和AUTH TOKEN在登陆官网后，在“应�?管理控制台�?中查看开发�?主账号获�?
		// *参数顺序：第�?��参数是ACOUNT SID，第二个参数是AUTH TOKEN�?*
		// *******************************************************************************
		restAPI.setAccount("aaf98f89525ced9f01525d5c46070197",
				"98dfb4ae63734e90b7ece1ea37866093");

		// ******************************注释*********************************************
		// *初始化应用ID *
		// *测试�?��可使用�?测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID *
		// *应用ID的获取：登陆官网，在“应�?应用列表”，点击应用名称，看应用详情获取APP ID*
		// *******************************************************************************
		restAPI.setAppId("8a48b551525cdd3301525d5e7373017f");

		// ******************************注释****************************************************************
		// *调用发�?模板短信的接口发送短�?*
		// *参数顺序说明�?*
		// *第一个参�?是要发�?的手机号码，可以用�?号分隔，�?���?��支持100个手机号 *
		// *第二个参�?是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id�?�?*
		// *系统默认模板的内容为“�?云�?讯�?您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入�?*
		// *第三个参数是要替换的内容数组�?*
		// **************************************************************************************************

		// **************************************举例说明***********************************************************************
		// *假设您用测试Demo的APP ID，则�?��用默认模板ID 1，发送手机号�?3800000000，传入参数为6532�?，则调用方式�?
		// *
		// *result = restAPI.sendTemplateSMS("13800000000","1" ,new
		// String[]{"6532","5"}); *
		// *�?3800000000手机号收到的短信内容是：【云通讯】您使用的是云�?讯短信模板，您的验证码是6532，请�?分钟内正确输�?*
		// *********************************************************************************************************************
		validateCode=createRandom(true,5);
		result = restAPI.sendTemplateSMS(PhoneNumber, "1", new String[] {validateCode, "2" });

		System.out.println("SDKTestGetSubAccounts result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			
			return true;
		} else {
			// 异常返回输出错误码和错误信息
			return false;
			
		}
	}

	/**
	 * 创建指定数量的随机字符串
	 * 
	 * @param numberFlag 是否是数�?
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890"
				: "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);
		return retStr;
	}

}

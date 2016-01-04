/*
 * 作用于：/ccore下的jsp,html页面
 * 功能说明：封装一些在核心模块中，通用的，频繁使用的,逻辑方法；
 * 依赖文件：jquery,/ccore/resources/js/exam.js
 * 作者：hyq
 * 创建时间：2012-8-08
 */

/*
 * 验证码输入验证；
 * return: 如果确证成功，返回true;否则返回false;
 */
function checkCaptcha(){
	//如果验证码输入的值长度不为4,则提示不正确，且输入框重获焦点,否则验证正确
	if($("#captcha").val().length!=4){
		alert("验证码输入不正确!");
		$("#captcha").get(0).focus();		
		return false;		
	}else
		return true;
}


/*
 * 当用户在当前页按下回车时，自动提交；
 * submitButtonId: 页中提交按钮的ID
 */
function setDefaultSubmit(submitButtonId){
	$(document.body).keydown(function(ev){
		//当按的是回车键时，触发接交铵钮的click事件；
		if(ev.keyCode==13){
			$("#"+submitButtonId).get(0).click();
		}		
	});
}
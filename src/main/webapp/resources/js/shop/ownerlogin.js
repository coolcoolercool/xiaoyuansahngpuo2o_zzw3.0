/*
* 根据userType来区分登陆成功后,跳转的页面,比如用户登陆成功,就跳转到前端首页frondtend/index
* 商家登陆登陆成功,就跳转到商铺列表页面
* 管理员登陆成功就会跳转到管理界面
* */
$(function() {
	var loginUrl = '/o2o/shop/ownerlogincheck';
	var loginCount = 0;

	$('#submit').click(function() {
		var userName = $('#username').val();
		var password = $('#psw').val();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (loginCount >= 3) {
			if (!verifyCodeActual) {
				$.toast('请输入验证码！');
				return;
			} else {
				needVerify = true;
			}
		}
		$.ajax({
			url : loginUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual,
				needVerify : needVerify
			},
			success : function(data) {
				if (data.success) {
					$.toast('登录成功！');
					window.location.href = '/o2o/shopadmin/shoplist';
				} else {
					$.toast('登录失败！');
					loginCount++;
					if (loginCount >= 3) {
						$('#verifyPart').show();
					}
				}
			}
		});
	});

	$('#register').click(function() {
		window.location.href = '/o2o/shop/register';
	});
});
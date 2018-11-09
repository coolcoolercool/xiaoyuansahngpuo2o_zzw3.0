$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
			+ productId;

	$
			.getJSON(
					productUrl,
					function(data) {
						if (data.success) {
							var product = data.product;
							//商品的缩略图,这里添加了
							$('#product-img').attr('src', getContextPath() + product.imgAddr);

							//如果原价和优惠价不为空,则显示
							if(product.promotionPrice != undefined && product.normalPrice != undefined){
								$('#product-normal-price').text('原价为:' + product.normalPrice + '¥');
								$('#product-promotion-price').text('优惠价为:' + product.promotionPrice + '¥');
                            }

                            //商品更新时间
							$('#product-time').text(
									new Date(product.lastEditTime)
											.Format("yyyy-MM-dd"));
							//商品积分,如果购买了该商品,可以获得该商品的积分
							if(product.point != undefined){
								$('#product-point').text('购买可得:'+ product.point + '积分');
							}
							$('#product-name').text(product.productName);
							$('#product-desc').text(product.productDesc);
							var imgListHtml = '';
							//遍历商品详情图列表,并生成批量img标签
							product.productImgList.map(function(item, index) {
								imgListHtml += '<div> <img src="' + getContextPath()
										+ item.imgAddr + '"/></div>';
							});
							// 生成购买商品的二维码供商家扫描
							imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
									+ product.productId + '"/></div>';
							$('#imgList').html(imgListHtml);
						}
					});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});

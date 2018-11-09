/*
* 店铺商品上下架
* */

$(function() {
    var listUrl = '/o2o/shopadmin/getproductlist?pageIndex=1&pageSize=9999';
    var changeStuatusURL = '/o2o/shopadmin/changestatus';

    getList();

    function getList() {
        $.getJSON(listUrl, function(data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml = '';
                productList.map(function(item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-33">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-33">'
                        + item.point
                        + '</div>'
                        + '<div class="col-33">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="status" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(tempHtml);
            }
        });
    }


    /**
     * 下架操作
     */
    function changeStatus(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function() {
            $.ajax({
                url : changeStuatusURL,
                type : 'POST',
                data : {
                    productStr : JSON.stringify(product),
                    statusChange : true       /* 这里添加了statusChange变量传给后台 true 这里的作用是跳过验证码 暂时这个变量用不到了,这里选择重新写一个方法,直接没有验证码,而不是复用修改商品的代码,所以用不到这个变量了 todo */
                },
                dataType : 'json',
                success : function(data) {
                    if (data.success) {
                        $.toast(data.errMsg);
                        getList();
                    } else {
                        $.toast(data.errMsg);
                    }
                }
            });
        });
    }

    $('.product-wrap').on(
        'click',
        'a',
        function(e) {
            var target = $(e.currentTarget);
            if (target.hasClass('edit')) {
                window.location.href = '/o2o/shopadmin/productoperation?productId='
                    + e.currentTarget.dataset.id;
            } else if (target.hasClass('status')) {
                changeStatus(e.currentTarget.dataset.id,
                    e.currentTarget.dataset.status);
            } else if (target.hasClass('preview')) {
                // TODO
                window.location.href = '/o2o/frontend/productdetail?productId='
                    + e.currentTarget.dataset.id;
            }
        });

    $('#new').click(function() {
        window.location.href = '/o2o/shopadmin/productoperation';
    });
});

function changeVerifyCode(img){
    img.src="../Kaptcha?" + Math.floor(Math.random() * 1000)
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); /*正则表达式*/
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}

/**
 * 获取项目的ContextPath以便修正图片路由将其正常显示
 * @returns {string}
 */
function getContextPath(){
    return "/o2o/";
}

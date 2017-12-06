
var sku = {};
sku.top = function(obj) {
	var firstSku = $("#skuTable>tbody>tr:eq(1)");
	firstSku.find(".skuAction").html('<a href="#" onclick="sku.remove(this)">[删除]</a><a href="#" onclick="sku.top(this)">[T]</a>');
	
	$(obj).parent().parent().clone(true).insertAfter($("#skuTable>tbody>tr:eq(0)"));
	$(obj).parent().parent().remove();
	
	var firstSku = $("#skuTable>tbody>tr:eq(1)");
	firstSku.find(".skuAction").html('<a href="#" onclick="sku.add(this)">[增加]</a>');
}
sku.add = function(obj) {
	// 显示firstSkuTitle
	$(".firstSkuTitle").show();
	$(obj).parent().parent().after('<tr class="skuTr">' + $("#moreSkuTr").html() + '</tr>')
}
sku.remove = function(obj) {
	// 只剩下一个sku时，隐藏firstSkuTitle
	var len = $("#skuTable>tbody>tr").length;
	if (len == 4) {
		$(".firstSkuTitle").hide();
	}
	$(obj).parent().parent().remove();
}


var img = {};
img.html = '<td class="imgTd">\
	<table style="height:80px"><tr><td rowspan="2"><img class="uploadImg" onload="img.resize(this)"/></td><td class="glyphicon glyphicon-remove-circle" onclick="img.remove(this)"></td></tr>\
	<tr><td class="glyphicon glyphicon-step-backward leftTopImg" onclick="img.top(this)"></td></tr></table></td>';
img.fileChange = function(obj) {
	var f = obj.files;
	if (f.length == 0) return;
	
	this.loadImg(f, f.length, $(obj).parent().parent());
}
img.loadImg = function(f, n, imgTr) {
	n--;
	var td = $(this.html);
	td.appendTo(imgTr);
	var reader = new FileReader();
	reader.onload = function(e) {
		var imgObj = td.find("img");
		imgObj.prop("src", this.result);
		imgObj.data("file", f[n]);
		if (n > 0) img.loadImg(f, n, imgTr);
	}
	reader.readAsDataURL(f[n]);
}
img.resize = function(img) {
	if (img.height > img.width) $(img).css({height:80, width:80 * img.width/img.height});
	else $(img).css({width:80, height:80 * img.height/img.width});
}
img.remove = function(obj) {
	$(obj).parents(".imgTd").remove();
	this.refreshTop();
}
img.refreshTop = function() {
	$(".leftTopImg").show();
	$(".leftTopImg").first().hide();
}
img.top = function(obj) {
	var firstImgTd = $(obj).parents(".imgTr").find("td:eq(0)");
	$(obj).parents(".imgTd").clone(true).insertAfter(firstImgTd);
	$(obj).parents(".imgTd").remove();
	this.refreshTop();
}






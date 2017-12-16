 // 要想实现拖拽，首页需要阻止浏览器默认行为，一个四个事件。
$(document).on({
	dragleave:function(e){        //拖离
		e.preventDefault();
	},
    drop:function(e){            //拖后放
    	e.preventDefault();
	},
	dragenter:function(e){       //拖进
		e.preventDefault();
    },
	dragover:function(e){        //拖来拖去
		e.preventDefault();
	}
});

var refreshDrag = function() {
	var dragImg = document.querySelector('.dragImg'); //获得到框体
	$(".dragImg").on("drop",  function() {
		var imgTr = $(event.target).find(".imgTr");
	    var f = event.dataTransfer.files;
		if (f.length == 0) return;
		img.loadImg(f, f.length, imgTr);
	})
}

$(function() {
	refreshDrag();
});



var sku = {};
sku.REMOVE_HTML = '<a href="#" onclick="sku.remove(this)">[删除]</a><a href="#" onclick="sku.top(this)">[置顶]</a>';
sku.top = function(obj) {
	var firstSku = $("#skuTable>tbody>tr:eq(1)");
	firstSku.find(".skuAction").html(this.REMOVE_HTML);
	
	$(obj).parent().parent().clone(true).insertAfter($("#skuTable>tbody>tr:eq(0)"));
	$(obj).parent().parent().remove();
	
	var firstSku = $("#skuTable>tbody>tr:eq(1)");
	firstSku.find(".skuAction").html('<a href="#" onclick="sku.add(this)">[增加]</a>');
}
sku.add = function(obj) {
	// 显示firstSkuTitle
	$(".firstSkuTitle").show();
	
	var firstSku = $("#skuTable>tbody>tr:eq(1)");
	var newTr = $('<tr>' + firstSku.html() + '</tr>');
	newTr.find(".skuAction").html(this.REMOVE_HTML);
	newTr.find(".imgTr>td:gt(0)").remove();
	
	
	
	$("#skuTable").append(newTr);
	
	refreshDrag();
}
sku.remove = function(obj) {
	// 只剩下一个sku时，隐藏firstSkuTitle
	var len = $("#skuTable>tbody>tr").length;
	if (len == 3) {
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
	
	
	// 重新生成一个，防止在图片被删除时onchange不生效
	$(obj).prop("outerHTML", $(obj).prop("outerHTML"));
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






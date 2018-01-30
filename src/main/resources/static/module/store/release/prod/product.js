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



// sku >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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



// >>>>>>>>>>>>>>>>>>>img>>>>>>>>>>>>>>>>>>>>>>>>>>>>
var img = {};
img.zIndex = 1000;
img.click = function(obj) {	
	if (obj.width == 80) {
		$(obj).css({position:"fixed",left:$(obj).offset().left,top:$(obj).offset().top});
		$(obj).css({zIndex:++img.zIndex,width:$(obj).data("data-init-width"),height:$(obj).data("data-init-height")});
	}
	else {
		$(obj).css({position:""});
		$(obj).css({zIndex:1000,width:80,height:80});
	}
}
img.html = '<td class="imgTd">\
	<table style="height:80px"><tr><td rowspan="2" style="width:80px;"><img data-init-width="" data-init-height="" onclick="img.click(this)" class="uploadImg" onload="img.resize(this)"/></td><td class="glyphicon glyphicon-remove-circle" onclick="img.remove(this)"></td></tr>\
	<tr><td class="glyphicon glyphicon-step-backward leftTopImg" onclick="img.top(this)"></td></tr></table></td>';
img.fileChange = function(obj) {
	this.loadImg(obj.files, obj.files.length, $(obj).parent().parent());
	
	// 重新生成一个，防止在图片被删除时onchange不生效
	$(obj).prop("outerHTML", $(obj).prop("outerHTML"));
}
img.loadImg = function(f, n, imgTr) {
	n--;
	
	// data-max-size默认为1M,maxLength默认为1
	var maxSize =  $(imgTr).find("input").attr("data-max-size");
	maxSize = !maxSize ? 1024*1024 : new Number(maxSize);
	var maxLength = $(imgTr).find("input").attr("data-max-length");
	maxLength = !maxLength ? 1 : new Number(maxLength);
	
	var accept = $(imgTr).find("input").attr("accept").split(",");
	var isAccept = false;
	for (var i = 0; i < accept.length; i++) {
		var fName = f[n].name.split(".");
		isAccept = "." + fName[fName.length - 1].toLowerCase() == accept[i];
	}
	if (isAccept == false) {
		return alertWarning("后缀名必须为:" + accept);
	}
	if (f[n].size > maxSize) {
		var max = maxSize >= 1024*1024 ? (maxSize/1024/1024).toFixed(1) + "M" : (maxSize/1024).toFixed(1) + "K";
		return alertWarning("不能大于" + max);
	}
	if　($(imgTr).find(".imgTd").length　== maxLength) {
		return alertWarning("最多" + maxLength  + "个");
	}
	
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
	this.refreshTop();
}
img.resize = function(img) {
	if (img.width != img.height) {
		alertWarning("长宽不一样");
		$(img).parents(".imgTd").remove();
	}
	else {
		$(img).parents(".imgTd").show();
		$(img).data("data-init-width", img.width);
		$(img).data("data-init-height", img.height);
		$(img).css({width:80, height:80});
	}
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






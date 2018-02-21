
/*
document.onkeydown = function (e) {
    var ev = window.event || e;
    var code = ev.keyCode || ev.which;
    if (code == 116) { // F5
    	$(".menuSelected").click();
        ev.keyCode ? ev.keyCode = 0 : ev.which = 0;
        cancelBubble = true;
        return false;
    }
}*/

function logout() {
	var callback = function () {
		location.href = contextPath + "login/loginIndex";
	}
	confirm("确定退出？", callback);
}

function gotoIndex() {
	$(".menuSelected").removeClass("menuSelected");
	$("#content").attr("src", $("#content").data("homeSrc"));
}

function editPassword() {
	$(".menuSelected").removeClass("menuSelected");
	$("#content").attr("src", contextPath + "index/editPassword");
}

function toggleMenu() {
	if ($("#toggleId").text() == '<') {
		$("#menuDiv").hide();
		$("#topDiv,#iframeDiv").css("margin-left", "0px");
		$("#toggleId").text(">");
	}
	else {
		$("#menuDiv").show();
		$("#topDiv,#iframeDiv").css("margin-left", "200px");
		$("#toggleId").text("<");
	}
}

function iframeLoad() {
	$("#iframeLoading").data("isShowLoading", false);
	$("#iframeLoading").hide();
	$("#content").show();
}

function onMenu(obj, id, uri) {
	$(".menuItem").removeClass("menuSelected");
	$(obj).addClass("menuSelected");
	
	if (uri == "undefined") {
		alertDanger("没有找到uri!");
		return;
	}
	
	$("#content").attr("src", contextPath + uri.substring(1));
	$("#iframeLoading").data("isShowLoading", true);
	// n毫秒没有加载完页面才出现loading
	setTimeout(function() {
		if ($("#iframeLoading").data("isShowLoading")) {
			$("#iframeLoading").show();
			$("#content").hide();
		}
	}, 300);
}

function resizeIframe() {
	$("#iframeDiv").css("height", $(window).height() - 50);
}

$(function() {
	resizeIframe();
	$(window).resize(function(){resizeIframe()});
	
	// 存储首页的src,以便点击"首页"时找到链接
	$("#content").data("homeSrc", $("#content").attr("src"));
	
	if (!menu) return;
	for (var i = 0; i < menu.length; i++) {
		// 默认打开第一个目录
		var firstBlock = i == 0 ? 'style="display: block;"' : '';
		var firstOpen = i == 0 ? 'class="open"' : '';
		
		var li = $('<li ' + firstOpen + '><div class="link" ' + firstBlock + '><i class="fa fa-mobile"></i>' + menu[i].t + '<i class="fa fa-chevron-down"></i></div></li>');
		$("#accordion").append(li);
		
		var subLi = '';
		for (var j = 0; menu[i].n && j < menu[i].n.length; j++) {
			subLi += '<li><a class="menuItem" href="#" onclick="onMenu(this, ' + menu[i].n[j].id + ', \'' + menu[i].n[j].uri + '\')">' + menu[i].n[j].t + '</a></li>';
		}
		li.append('<ul class="submenu" ' + firstBlock + '>' + subLi + '</ul>');
	}
	
	var Accordion = function(el, multiple) {
		this.el = el || {};
		this.multiple = multiple || false;
		var links = this.el.find('.link');
		links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
	}
	Accordion.prototype.dropdown = function(e) {
		var $el = e.data.el;
		$this = $(this),
		$next = $this.next();

		$next.slideToggle();
		$this.parent().toggleClass('open');

		if (!e.data.multiple) {
			$el.find('.submenu').not($next).slideUp().parent().removeClass('open');
		}
	}
	var accordion = new Accordion($('#accordion'), false);
});


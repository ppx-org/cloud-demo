

## 当用户搜索不到商品后来进货了，要不要用猜你喜欢来显示?

## 同义词，搜索时用，比如羊城通可以搜到腾讯乘车码.

## mid（商户ID）为固定值？

## scan带参数?sid=1&c=A01
```javascript
var c = '1|A01';
document.write(window.btoa(c));
try {
	var ss = window.atob("MXxBMDE=");
	var para = ss.split("|");
	var sid = para[0];
	var code = para[1];
	alert("out:" + sid + "|" + code);
}
catch(e) {
	document.write("error.........1");
}
```

## 不带，sid和mid为默认值，从后端返回，（可直接在页面上写死?）
```javascript
onLaunch:function() {
	promoCode url scene date
}
```

## MongoDB中的数据导出为excel CSV 文件
mongoexport -d ppx-log-dev -c data_promo_entry -f promoCode,date,openid --type csv -o ./entry.csv

-d  标示 数据库
-c  标示  数据表
-f  需要提取的field用逗号分隔
-o  输出路径

## barcode https://github.com/demi520/wxapp-qrcode



































体验:
搜索维护时提示系统忙
图片服务出问题时，提示







# 产品标题
1. http://buy.ccb.com/
2. 
```javascript
var r1 = [];$(".prodname").each(function(){r1.push($(this).attr("title"))});
var r2 = [];$(".prolist_sr .fl strong").each(function(){r2.push($(this).text().replace("¥", ""))});
var r = [];
for (var i = 0; i < r1.length; i++) {
	r.push(r1[i] + "\t" + r2[i]);
}
console.log(r.join("\r\n"));
```
3. 复制到excel


4. 公式indirect相当于js的eval, &可以连接字符串
```
=INDIRECT("A" & RANDBETWEEN(2, 19))
```


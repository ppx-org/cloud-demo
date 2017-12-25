
var valu = {};
valu.count = function(r) {
	var prodIdMap = {};
	var groupN = {};
	var enoughYen = {};
	r.forEach(function(v) {
		prodIdMap[v.prodId] = true;
		
		if (!v.progId) return;

		if (!groupN[v.progId]) {
			groupN[v.progId] = v.num;
			enoughYen[v.progId] = v.price * v.n;
		}
		else {
			groupN[v.progId] += v.num;
			enoughYen[v.progId] += v.price * v.n;
		}
	});

	var compare = function(obj1, obj2) {
	    var val1 = obj1.progId;
	    var val2 = obj2.progId;
	    if (val1 < val2) {
	        return -1;
	    } else if (val1 > val2) {
	        return 1;
	    } else {
			var p1 = obj1.price;
			var p2 = obj2.price;
			if (p1 > p2) {
				return -1;
			} else if (p1 < p2) {
				return 1;
			}
	        return 0;
	    }            
	}
	r.sort(compare);

	
	var count2discount = 0;
	var countMoreAdd = 0;
	var countBuyFree = 0;

	var newR = r.map(function(v) {
		var poli = v.policy;
		var poli_1 = "";
		var poli_2 = "";
		var gN = groupN[v.progId];
		var eYen = enoughYen[v.progId];
		
		if (poli) {
			var item = poli.split(",");
			if (item.length == 1) {
				poli_1 = poli;
			}
			else if (item.length == 2) {
				poli_1 = item[0];
				poli_2 = item[1];
			}
		}
		
		
		if (poli == undefined) {
			v.itemPrice = v.price * v.num;
		}
		else if (poli.indexOf("S") == 0) {
			var p = new Number(poli_1.split(":")[1]);
			v.itemPrice = p * v.num;
		}
		else if (poli.indexOf("A") == 0) {
			var p = new Number(poli_1.split(":")[1]);
			v.itemPrice = p * v.num;
		}
		else if (poli.indexOf("%") == 0) {
			if (poli_2 == "" || gN == 1) {
				var d = new Number(poli_1.split(":")[1]);
				v.itemPrice = v.price * d * v.num;
			}
			else if (poli_2.indexOf("2+") == 0)  {
				d = new Number(poli_2.split(":")[1]);
				v.itemPrice = v.price * d * v.num;
			}
			else if (poli_2.indexOf("2") == 0) {
				var batch1D = new Number(poli_1.split(":")[1]);
				var batch2D = new Number(poli_2.split(":")[1]);			
				var batch1N = Math.floor(gN/2) - count2discount;
				count2discount += v.num;
				if (batch1N >= v.num) {
					v.itemPrice = v.price * v.num;
				}
				else if (batch1N <= 0) {
					v.itemPrice = v.price * batch2D;
				}
				else {
					v.itemPrice = v.price * batch1N * batch1D + v.price * (v.num - batch1N) * batch2D;
				}
			}
		}
		else if (poli.indexOf("+") == 0) {
			if (gN == 1) {
				v.itemPrice = v.price;
			}
			else {
				var batch1Y = new Number(poli_1.split(":")[1]);
				var batch1N = Math.floor(gN/2) - countMoreAdd;
				countMoreAdd += v.num;
				if (batch1N >= v.num) {
					v.itemPrice = v.price * v.num;
				}
				else if (batch1N <= 0) {
					v.itemPrice = batch1Y * v.num;
				}
				else {
					v.itemPrice = v.price * batch1N + batch1Y * (v.num - batch1N);
				}
			}
		}
		else if (poli_1.split(":")[1] == 'Y') {
			var y = new Number(poli_1.split(":")[0]);
			var n = new Number(poli_2.split(":")[0]);
			if (n < gN) {
				var avgP = y/n;
				v.itemPrice = avgP * v.num;
			}
			else {
				v.itemPrice = v.price * v.num;
			}
		}
		else if (poli_1.split(":")[0] == 'E' && poli_2.split(":")[0] == '-') {
			var e = poli_1.split(":")[1];
			var y = poli_2.split(":")[1];
			
			if (eYen >= e) {
				var avgMinus = Math.floor(eYen/e) * y / gN;
				v.itemPrice = (v.price - avgMinus) * v.num;
			}
			else {
				v.itemPrice = v.price * v.num;
			}
		}
		else if (poli_1.split(":")[0] == 'B') {
			var buyN = new Number(poli_1.split(":")[1]);
			var freeN = new Number(poli_2.split(":")[1]);
			
			var gFreeN = Math.floor(gN/buyN) * freeN;
			
			var batch1N = gN - gFreeN - countBuyFree;
			countBuyFree += v.n;
			if (buyN > gN) {
				v.itemPrice = v.price * v.num;
			}
			else if (batch1N <= 0) {
				v.itemPrice = 0;
			}
			else {
				if (batch1N >= v.num) {
					v.itemPrice = v.price * v.num;
				}
				else {
					v.itemPrice = v.price * batch1N;
				}
			}
		}
		else if (poli_1.split(":")[0] == 'D') {
			var dependProdId = poli_1.split(":")[1];
			var dependPrice = poli_2.split(":")[1];
			if (prodIdMap[dependProdId]) {
				v.itemPrice = dependPrice * v.num;
			}
			else {
				v.itemPrice = v.price * v.num;
			}
		}
		
		if (!v.itemPrice) v.itemPrice = 0;
		//v.itemPrice = new Number(v.itemPrice);
		return v;
	});




	var excludeChangeTotalPrice = 0;
	newR.forEach(function(v) {
		excludeChangeTotalPrice += v.itemPrice;
	})


	var resulltR = r.map(function(v) {
		var poli = v.policy;
		var poli_1 = "";
		var poli_2 = "";
		
		if (poli) {
			var item = poli.split(",");
			if (item.length == 1) {
				poli_1 = poli;
			}
			else if (item.length == 2) {
				poli_1 = item[0];
				poli_2 = item[1];
			}
		}
		
		if (poli_1.split(":")[0] == 'E' && poli_2.split(":")[0] == 'C') {
			var e = new Number(poli_1.split(":")[1]);
			var c = new Number(poli_2.split(":")[1]);
			if (excludeChangeTotalPrice >= e) {
				v.itemPrice = c * v.num;
			}
			else {
				v.itemPrice = v.price * v.num;
			}
		}
	});

	return newR;
}




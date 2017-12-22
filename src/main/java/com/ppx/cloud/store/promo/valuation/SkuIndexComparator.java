package com.ppx.cloud.store.promo.valuation;

import java.util.Comparator;

public class SkuIndexComparator implements Comparator<SkuIndex> {

	@Override
	public int compare(SkuIndex o1, SkuIndex o2) {
		if (o1.getProgId() < o2.getProgId()) {
			return -1;
		}
		else if (o1.getProgId() > o2.getProgId()) {
			return 1;
		}
		else {
			if (o1.getPrice() > o2.getPrice()) {
				return -1;
			}
			else if (o1.getPrice() < o2.getPrice()) {
				return 1;
			}
		}
		return 0;
	}
	
	
}

package test;

import java.util.ArrayList;
import java.util.List;

public class TestIntArray {
	static boolean isExist;
	public static void main(String args[]){
		int[] a = {1,2,3};
		int[] b = {1,2};
		int[] c = {4};
		int[] d = {5,6};
		int[] e = {1,3};
		int[] f = {1,2};
		
		List<int[]> list = new ArrayList<int[]>();
		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);
		
		
		System.out.println(isDataExist(list, a));
		System.out.println(isDataExist(list, b));
		System.out.println(isDataExist(list, c));
		System.out.println(isDataExist(list, d));
		System.out.println(isDataExist(list, e));
		System.out.println(isDataExist(list, f));
		
	}
	private static boolean isDataExist(List<int[]> x, int[] y){
		
		for(int[] datas : x){
			// first, whether length is different
			if(datas.length == y.length){
				//second, check every element
				for(int i=0; i<datas.length; i++){
					if(datas[i] != y[i])
						break;
					if(i == datas.length - 1){ // if every element is the same, return true
						return true;
					}
				}
				
			}			
		}
		return false;
	}
}

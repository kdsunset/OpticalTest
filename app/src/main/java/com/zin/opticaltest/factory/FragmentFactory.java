package com.zin.opticaltest.factory;

import android.support.v4.app.Fragment;

import com.zin.opticaltest.fragment.ModelPaperFrag;
import com.zin.opticaltest.fragment.RealPaperFrag;
import com.zin.opticaltest.fragment.SpecialPracticeFrag;

import java.util.HashMap;

public class FragmentFactory {
	private static HashMap<Integer, Fragment> hashMap = new HashMap<Integer, Fragment>();
	public static Fragment createFragment(int position) {
		Fragment baseFragment = hashMap.get(position);

		if(baseFragment !=null){
			//根据索引获取到了fragment对象,直接返回即可
			return baseFragment;
		}else{
			//在没有此索引指向fragment的时候创建逻辑
			switch (position) {
			case 0:
				baseFragment = new RealPaperFrag();
				break;
			case 1:
				baseFragment = new ModelPaperFrag();
				break;
			case 2:
				baseFragment = new SpecialPracticeFrag();
				break;
			/*case 3:
				baseFragment = new HotQuestionFrag();
				break;*/
			/*case 4:
				baseFragment = new RecommendFragment();
				break;
			case 5:
				baseFragment = new CategoryFragment();
				break;
			case 6:
				baseFragment = new HotFragment();
				break;*/
			}
			hashMap.put(position, baseFragment);
			return baseFragment;
		}
	}
}

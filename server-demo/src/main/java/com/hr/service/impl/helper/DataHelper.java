package com.hr.service.impl.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hr.domain.CardData;
import com.hr.domain.ResultData;

import lombok.Data;

/**
 * 数据处理类
 * 
 * @author chenlong
 * @email 1021773811@qq.com
 * @date 2018年7月25日上午10:42:46
 *
 */
public class DataHelper {

	public static List<ResultData> analyse(List<CardData> cardDataList) {

		// 结果集
		List<ResultData> resultDataList = new ArrayList<>();

		ListMultimap<User, CardData> cardDataListMultimap = ArrayListMultimap.create();
		if (cardDataList != null && cardDataList.size() > 0) {
			for (CardData cardData : cardDataList) {
				User user = new User();
				user.setName(cardData.getName());
				user.setDepartment(cardData.getDepartment());
				cardDataListMultimap.put(user, cardData);
			}
		}
		Map<User, Collection<CardData>> asMap = cardDataListMultimap.asMap();
		if (asMap != null && asMap.size() > 0) {
			asMap.forEach((user, list) -> {
				// 每个用户的打卡数据
				System.out.println(user + ":" + list);
				ResultData resultData = new ResultData();
				resultData.setName(user.getName());
				resultData.setDepartment(user.getDepartment());
				resultData.setEightPmCardList(new ArrayList<>());
				resultData.setTenPmCardList(new ArrayList<>());
				for (CardData cardData : list) {
					LocalDateTime cardTime = cardData.getCardTime();
					int hour = cardTime.getHour();

					// 22 以后
					if (hour >= 22) {
						resultData.getTenPmCardList().add(cardTime.toLocalDate());
					} else if (hour >= 20) {// 20以后
						resultData.getEightPmCardList().add(cardTime.toLocalDate());
					} else if (hour < 6) {// 前一天22 以后
						resultData.getTenPmCardList().add(cardTime.toLocalDate().plusDays(-1));
					}

				}

				// 22点以后的，从20以后里面移除。
				resultData.getEightPmCardList().removeAll(resultData.getTenPmCardList());
				
				// 去重
				resultData.setEightPmCardList(
						resultData.getEightPmCardList().stream().distinct().collect(Collectors.toList()));
				resultData.setTenPmCardList(
						resultData.getTenPmCardList().stream().distinct().collect(Collectors.toList()));

				resultData.setEightPmCardSize(resultData.getEightPmCardList().size());
				resultData.setTenPmCardSize(resultData.getTenPmCardList().size());
				resultDataList.add(resultData);

			});
		}

		return resultDataList;
	}

	@Data
	private static class User {
		private String name;
		private String department;
	}

}

package com.marcojan11.wonder.spider.apple;

import com.alibaba.fastjson.JSONObject;
import com.marcojan11.wonder.kernel.common.Dates;
import com.marcojan11.wonder.kernel.common.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class OrderTools {

    /**
     * R638 天津万象城
     * R637 天津大悦城
     * R579 天津恒隆
     *
     * R320 北京三里屯
     * R479 北京华贸
     * R645 北京朝阳大悦城
     * R448 北京王府井
     * R388 北京西单大悦城
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        Map<String, String> store_codes = new HashMap();

        //store_codes.put("R638","天津万象城");
        //store_codes.put("R637","天津大悦城");
        //store_codes.put("R579","天津恒隆");
        store_codes.put("R320","北京三里屯");
        store_codes.put("R479","北京华贸");
        store_codes.put("R448","北京王府井");
        store_codes.put("R645","北京朝阳大悦城");
        store_codes.put("R388","北京西单大悦城");

        Map<String, String> iphone_codes = new HashMap();
        //iphone_codes.put("MGC43CH/A","iPhone12 PM 256G 石墨色");
        iphone_codes.put("MGC53CH/A","iPhone12 PM 256G 银色");
        //iphone_codes.put("MGC63CH/A","iPhone12 PM 256G 石墨色");
        //iphone_codes.put("MGC73CH/A","iPhone12 PM 256G 海蓝色");
        //iphone_codes.put("MGGX3CH/A","iPhone12 PRO 128G 海蓝色");

        System.out.println("开始扫描苹果商店: " +store_codes.values().toString());
        for(;;){
            scan_apple_store(store_codes,iphone_codes);
            Thread.sleep(5000L);
        }

    }

    private static void scan_apple_store(Map store_codes, Map iphone_codes){

        try {
            String resp = HttpClient.getResponseByPostMethod("https://reserve-prime.apple.com/CN/zh_CN/reserve/G/availability.json", new HashMap());

            int i = 0;

            JSONObject apple = JSONObject.parseObject(resp);
            JSONObject stores = apple.getJSONObject("stores");

            if(stores==null) {
                System.out.println("暂未开放预约服务 - "+ Dates.formatDate(new Date(), Dates.CN_DATETIME_FORMAT));
                return;
            }

            Set<String> storeSet = store_codes.keySet();
            Iterator it_store = storeSet.iterator();

            while (it_store.hasNext()) {
                String store_code = (String) it_store.next();
                JSONObject prods = stores.getJSONObject(store_code);

                Set<String> iphone_code_set = iphone_codes.keySet();
                Iterator it_iphone = iphone_code_set.iterator();

                while (it_iphone.hasNext()) {
                    String iphone_code = (String) it_iphone.next();
                    JSONObject prod = prods.getJSONObject(iphone_code);
                    JSONObject availability = prod.getJSONObject("availability");
                    if (availability.getBoolean("contract") && availability.getBoolean("unlocked")) {
                        String txt = "[有货] " + store_codes.get(store_code) + " - " + iphone_codes.get(iphone_code);
                        i++;
                        JSONObject json_wx = new JSONObject();
                        json_wx.put("msgtype", "text");
                        JSONObject json_wx_content = new JSONObject();
                        json_wx_content.put("content", txt);
                        json_wx.put("text", json_wx_content);
                        System.out.println(Dates.formatDate(new Date(), Dates.CN_DATETIME_FORMAT) + txt);
                        //HttpClient4Apple.getResponseByPostMethod("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=33808bdf-603b-41c2-b6e6-5952710c81cf", "UTF-8", json_wx.toJSONString());
                    }
                }
            }
            //System.out.println("本次扫货（" + DateUtil.formatDate(new Date(), DateUtil.CN_DATETIME_FORMAT) + "）结束，共 - " + i + " 台。");
        }catch(Throwable e){
            log.info("error");
            log.error(e.getMessage());
        }
    }
}

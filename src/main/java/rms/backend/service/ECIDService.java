package rms.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rms.backend.mapper.ECIDMapper;

import java.util.*;

@Transactional
@Service("ECIDService")
public class ECIDService {

    @Autowired
    private ECIDMapper ecIDMapper;

    public List<Map<String, Object>> getECIDList(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        List<Map<String, Object>> list = ecIDMapper.getECIDList(map);
        return list;
    }

    public List<Map<String, Object>> getCodeList(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        List<Map<String, Object>> list = ecIDMapper.getCodeList(map);
        return list;
    }

    public int saveECID(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if (reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        int cnt = 0;
        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("itemData");

        for(int i=0; i<list.size(); i++) {
            Map<String, Object> itemMap = list.get(i);

// 등록자(수정자) 정보 필요함
            itemMap.put("CREATE_BY", "SA");    // 로그인ID
            itemMap.put("LAST_UPDATE_BY", "SA");    // 로그인ID

            // ADD
            if((Boolean)itemMap.get("ADD") == true) {
                if((Boolean)itemMap.get("MANAGE_YN") == true) {
                    itemMap.put("MANAGE_YN", "Y");
                } else {
                    itemMap.put("MANAGE_YN", "N");
                }
                if((Boolean)itemMap.get("USED_YN") == true) {
                    itemMap.put("USED_YN", "Y");
                } else {
                    itemMap.put("USED_YN", "N");
                }

                cnt += ecIDMapper.insert(itemMap);
                // 히스토리 저장
                itemMap.put("ACTION_TYPE", "C");
                itemMap.put("DESCRIPTION", "");
                this.insertHst(itemMap);

            // UPDATE
            } else if((Boolean)itemMap.get("MODIFY") == true) {
                if((Boolean)itemMap.get("MANAGE_YN") == true) {
                    itemMap.put("MANAGE_YN", "Y");
                } else {
                    itemMap.put("MANAGE_YN", "N");
                }
                if((Boolean)itemMap.get("USED_YN") == true) {
                    itemMap.put("USED_YN", "Y");
                } else {
                    itemMap.put("USED_YN", "N");
                }

                cnt += ecIDMapper.update(itemMap);
                // 히스토리 저장
                itemMap.put("ACTION_TYPE", "U");
                itemMap.put("DESCRIPTION", "");
                this.insertHst(itemMap);
            }
        }

        return cnt;
    }

    public int deleteECID(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        int cnt = 0;
        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("itemData");

        for(int i=0; i<list.size(); i++) {
            Map<String, Object> itemMap = list.get(i);
            if((Boolean)itemMap.get("DELETE") == true) {

                //EC SPEC삭제
                if(ecIDMapper.deleteECSPEC(itemMap) > 0) {

// 히스토리 저장
// EC_SPEC_HST_ECM
                }

// 등록자(수정자) 정보 필요함
                itemMap.put("CREATE_BY", "SA");    // 로그인ID


                cnt += ecIDMapper.delete(itemMap);
                // 히스토리 저장
                itemMap.put("ACTION_TYPE", "D");
                itemMap.put("DESCRIPTION", "Delete ECID");
                this.insertHst(itemMap);
            }
        }
        return cnt;
    }

    public void insertHst(Map<String, Object> itemMap ) throws Exception {

        String oldValue = "";
        if(itemMap.get("ACTION_TYPE").equals("U") ) {
            if((Boolean)itemMap.get("ORI_MANAGE_YN") == true) {
                itemMap.put("ORI_MANAGE_YN", "Y");
            } else {
                itemMap.put("ORI_MANAGE_YN", "N");
            }
            if((Boolean)itemMap.get("ORI_USED_YN") == true) {
                itemMap.put("ORI_USED_YN", "Y");
            } else {
                itemMap.put("ORI_USED_YN", "N");
            }

            if (!itemMap.get("ALIAS").equals(itemMap.get("ORI_ALIAS"))) {
                oldValue = "ALIAS=" + itemMap.get("ORI_ALIAS");
            }
            if(!itemMap.get("MANAGE_YN").equals(itemMap.get("ORI_MANAGE_YN"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "MANAGE_YN=" + itemMap.get("ORI_MANAGE_YN");
            }
            if(!itemMap.get("USED_YN").equals(itemMap.get("ORI_USED_YN"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "USED_YN=" + itemMap.get("ORI_USED_YN");
            }
            if(!itemMap.get("VALUE_TYPE").equals(itemMap.get("ORI_VALUE_TYPE"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "VALUE_TYPE=" + itemMap.get("ORI_VALUE_TYPE");
            }
            if(!itemMap.get("UNIT_TYPE").equals(itemMap.get("ORI_UNIT_TYPE"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "UNIT_TYPE=" + itemMap.get("ORI_UNIT_TYPE");
            }
            if(!itemMap.get("SPEC_OPTION").equals(itemMap.get("ORI_SPEC_OPTION"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "SPEC_OPTION=" + itemMap.get("ORI_SPEC_OPTION");
            }
            if(!itemMap.get("SPEC_UNIT").equals(itemMap.get("ORI_SPEC_UNIT"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "SPEC_UNIT=" + itemMap.get("ORI_SPEC_UNIT");
            }
            if(!itemMap.get("ENUM").equals(itemMap.get("ORI_ENUM"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "ENUM=" + itemMap.get("ORI_ENUM");
            }
            if(!itemMap.get("ENUM_VALUE").equals(itemMap.get("ORI_ENUM_VALUE"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "ENUM_VALUE=" + itemMap.get("ORI_ENUM_VALUE");
            }
        }

        itemMap.put("EC_RAWID", itemMap.get("RAWID"));
        itemMap.put("APPROVAL_RAWID", "");
        itemMap.put("OLD_VALUE", oldValue);
        ecIDMapper.insertHst(itemMap);
    }

}

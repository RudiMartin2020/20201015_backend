package rms.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rms.backend.mapper.ECToolRegistrationMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("ECToolRegistrationService")
public class ECToolRegistrationService {

    @Autowired
    private ECToolRegistrationMapper ecToolRegistrationMapper;

    public List<Map<String, Object>> getToolList(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        List<Map<String, Object>> list = ecToolRegistrationMapper.getToolList(map);
        return list;
    }

    public List<Map<String, Object>> getInLineEQPList(String reqText) throws Exception {

        List<Map<String, Object>> list = ecToolRegistrationMapper.getInLineEQPList(null);
        return list;
    }

    public int saveTool(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        int cnt = 0;
        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("itemData");

        List<Map<String, Object>> inLineEQPList = ecToolRegistrationMapper.getInLineEQPList(null);

        for(int i=0; i<list.size(); i++) {
            Map<String, Object> itemMap = list.get(i);

            // INLINE_EQP_ID  null이 아닌경우  INLINE_EQP_RAWID 구함
            itemMap.put("INLINE_EQP_RAWID", "");
            if(itemMap.get("INLINE_EQP_ID") != null && !((String)itemMap.get("INLINE_EQP_ID")).isEmpty()) {
                for(int j=0; j<inLineEQPList.size(); j++) {
                    if(inLineEQPList.get(j).get("EQP_ID").equals(itemMap.get("INLINE_EQP_ID"))) {
                        itemMap.put("INLINE_EQP_RAWID", inLineEQPList.get(j).get("EQP_RAWID"));
                        break;
                    }
                }
            }

// 등록자(수정자) 정보 필요함
            itemMap.put("CREATE_BY", "SA");    // 로그인ID
            itemMap.put("LAST_UPDATE_BY", "SA");    // 로그인ID

            // ADD
            if((Boolean)itemMap.get("ADD") == true) {
                if((Boolean)itemMap.get("USED_YN") == true) {
                    itemMap.put("USED_YN", "Y");
                } else {
                    itemMap.put("USED_YN", "N");
                }
                if((Boolean)itemMap.get("INTERLOCK_SEND_YN") == true) {
                    itemMap.put("INTERLOCK_SEND_YN", "Y");
                } else {
                    itemMap.put("INTERLOCK_SEND_YN", "N");
                }
                if((Boolean)itemMap.get("DOWNLOAD_YN") == true) {
                    itemMap.put("DOWNLOAD_YN", "Y");
                } else {
                    itemMap.put("DOWNLOAD_YN", "N");
                }
                if((Boolean)itemMap.get("VALIDATION_YN") == true) {
                    itemMap.put("VALIDATION_YN", "Y");
                } else {
                    itemMap.put("VALIDATION_YN", "N");
                }

                cnt += ecToolRegistrationMapper.insert(itemMap);

                // 히스토리 저장
                itemMap.put("ACTION_TYPE", "C");
                itemMap.put("UPLOAD_YN", "N");
                itemMap.put("APPROVAL_RAWID", "");
                itemMap.put("DESCRIPTION", "");
                itemMap.put("OLD_VALUE", "");
                itemMap.put("INLINE_EQP_RAWID", "");
                itemMap.put("EQP_RAWID", itemMap.get("RAWID"));
                ecToolRegistrationMapper.insertHst(itemMap);

            // UPDATE
            } else if((Boolean)itemMap.get("MODIFY") == true){
                if((Boolean)itemMap.get("USED_YN") == true) {
                    itemMap.put("USED_YN", "Y");
                } else {
                    itemMap.put("USED_YN", "N");
                }
                if((Boolean)itemMap.get("INTERLOCK_SEND_YN") == true) {
                    itemMap.put("INTERLOCK_SEND_YN", "Y");
                } else {
                    itemMap.put("INTERLOCK_SEND_YN", "N");
                }
                if((Boolean)itemMap.get("DOWNLOAD_YN") == true) {
                    itemMap.put("DOWNLOAD_YN", "Y");
                } else {
                    itemMap.put("DOWNLOAD_YN", "N");
                }
                if((Boolean)itemMap.get("VALIDATION_YN") == true) {
                    itemMap.put("VALIDATION_YN", "Y");
                } else {
                    itemMap.put("VALIDATION_YN", "N");
                }

                cnt += ecToolRegistrationMapper.update(itemMap);


                // 히스토리 저장
                if((Boolean)itemMap.get("ORI_USED_YN") == true) {
                    itemMap.put("ORI_USED_YN", "Y");
                } else {
                    itemMap.put("ORI_USED_YN", "N");
                }
                if((Boolean)itemMap.get("ORI_INTERLOCK_SEND_YN") == true) {
                    itemMap.put("ORI_INTERLOCK_SEND_YN", "Y");
                } else {
                    itemMap.put("ORI_INTERLOCK_SEND_YN", "N");
                }
                if((Boolean)itemMap.get("ORI_DOWNLOAD_YN") == true) {
                    itemMap.put("ORI_DOWNLOAD_YN", "Y");
                } else {
                    itemMap.put("ORI_DOWNLOAD_YN", "N");
                }
                if((Boolean)itemMap.get("ORI_VALIDATION_YN") == true) {
                    itemMap.put("ORI_VALIDATION_YN", "Y");
                } else {
                    itemMap.put("ORI_VALIDATION_YN", "N");
                }

                String oldValue = "";
                if(!itemMap.get("USED_YN").equals(itemMap.get("ORI_USED_YN"))) {
                    oldValue = "USED_YN=" + itemMap.get("ORI_USED_YN");
                }
                if(!itemMap.get("INTERLOCK_SEND_YN").equals(itemMap.get("ORI_INTERLOCK_SEND_YN"))) {
                    if(!oldValue.isEmpty()){
                        oldValue += "@#@";
                    }
                    oldValue += "INTERLOCK_SEND_YN=" + itemMap.get("ORI_INTERLOCK_SEND_YN");
                }
                if(!itemMap.get("DOWNLOAD_YN").equals(itemMap.get("ORI_DOWNLOAD_YN"))) {
                    if(!oldValue.isEmpty()){
                        oldValue += "@#@";
                    }
                    oldValue += "DOWNLOAD_YN=" + itemMap.get("ORI_DOWNLOAD_YN");
                }
                if(!itemMap.get("VALIDATION_YN").equals(itemMap.get("ORI_VALIDATION_YN"))) {
                    if(!oldValue.isEmpty()){
                        oldValue += "@#@";
                    }
                    oldValue += "VALIDATION_YN=" + itemMap.get("ORI_VALIDATION_YN");
                }
                if(itemMap.get("ORI_INLINE_EQP_ID") == null) {
                    itemMap.put("ORI_INLINE_EQP_ID", "");
                }
                if(!(itemMap.get("ORI_INLINE_EQP_ID")).equals(itemMap.get("INLINE_EQP_ID"))) {
                    if(!oldValue.isEmpty()){
                        oldValue += "@#@";
                    }
                    oldValue += "INLINE_EQP_ID=";
                    if(itemMap.get("ORI_INLINE_EQP_ID") != null) {
                        oldValue += itemMap.get("ORI_INLINE_EQP_ID");
                    }
                }

                itemMap.put("ACTION_TYPE", "U");
                itemMap.put("UPLOAD_YN", "N");
                itemMap.put("APPROVAL_RAWID", "");
                itemMap.put("DESCRIPTION", "");
                itemMap.put("OLD_VALUE", oldValue);
                ecToolRegistrationMapper.insertHst(itemMap);
            }
        }

        return cnt;
    }

    public List<Map<String, Object>> getInLineEQPCheckList(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("itemData");
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();

        // GetInLineEQPCheckList 확인
        for(int i=0; i<list.size(); i++) {
            Map<String, Object> itemMap = list.get(i);
            if(itemMap.get("INLINE_EQP_ID") != null && !((String)itemMap.get("INLINE_EQP_ID")).isEmpty()) {
                List<Map<String, Object>> eqpCheckList = ecToolRegistrationMapper.getInLineEQPCheckList(itemMap);

                if (eqpCheckList.size() > 0) {
                    retList.addAll(eqpCheckList);
                }
            }
        }

        return retList;
    }

    public int deleteTool(String reqText) throws Exception {

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
                if(ecToolRegistrationMapper.deleteECSPEC(itemMap) > 0) {
// 히스토리 저장
// EC_SPEC_HST_ECM
                }
                //ECID 삭제
                if(ecToolRegistrationMapper.deleteEC(itemMap) > 0) {
// 히스토리 저장
// EC_HST_ECM
                }
                cnt += ecToolRegistrationMapper.delete(itemMap);

                // 히스토리 저장
                if((Boolean)itemMap.get("USED_YN") == true) {
                    itemMap.put("USED_YN", "Y");
                } else {
                    itemMap.put("USED_YN", "N");
                }
                if((Boolean)itemMap.get("INTERLOCK_SEND_YN") == true) {
                    itemMap.put("INTERLOCK_SEND_YN", "Y");
                } else {
                    itemMap.put("INTERLOCK_SEND_YN", "N");
                }
                if((Boolean)itemMap.get("DOWNLOAD_YN") == true) {
                    itemMap.put("DOWNLOAD_YN", "Y");
                } else {
                    itemMap.put("DOWNLOAD_YN", "N");
                }
                if((Boolean)itemMap.get("VALIDATION_YN") == true) {
                    itemMap.put("VALIDATION_YN", "Y");
                } else {
                    itemMap.put("VALIDATION_YN", "N");
                }

// 등록자(수정자) 정보 필요함
                itemMap.put("CREATE_BY", "SA");    // 로그인ID

                itemMap.put("ACTION_TYPE", "D");
                itemMap.put("UPLOAD_YN", "N");
                itemMap.put("APPROVAL_RAWID", "");
                itemMap.put("DESCRIPTION", "");
                itemMap.put("OLD_VALUE", "");
                itemMap.put("INLINE_EQP_RAWID", "");
                ecToolRegistrationMapper.insertHst(itemMap);
            }
        }
        return cnt;
    }


}

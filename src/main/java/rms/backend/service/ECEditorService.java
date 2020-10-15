package rms.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rms.backend.mapper.ECEditorMapper;
import rms.backend.mapper.ECIDMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("ECEditorService")
public class ECEditorService {

    @Autowired
    private ECEditorMapper ecEditorMapper;
    @Autowired
    private ECIDMapper ecIDMapper;

    public List<Map<String, Object>> getECSPECList(String reqText) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if(reqText != null && !reqText.isEmpty()) {
            map = mapper.readValue(reqText, new TypeReference<Map<String, Object>>() {});
        }

        List<Map<String, Object>> list = ecEditorMapper.getECSPECList(map);
        return list;
    }

    public int saveECSPEC(String reqText) throws Exception {

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
            if(itemMap.get("SPEC_RAWID") == null || itemMap.get("SPEC_RAWID") == "" ) {

                itemMap.put("EC_RAWID", itemMap.get("RAWID"));
                cnt += ecEditorMapper.insert(itemMap);

                // 히스토리 저장
                itemMap.put("ACTION_TYPE", "C");
                itemMap.put("DESCRIPTION", "");
                itemMap.put("SPEC_RAWID", itemMap.get("RAWID"));
                this.insertHst(itemMap);

                // EC_MST_ECM 수정
                itemMap.put("RAWID", itemMap.get("EC_RAWID"));
                if((Boolean)itemMap.get("MANAGE_YN") == true) {
                    itemMap.put("MANAGE_YN", "Y");
                } else {
                    itemMap.put("MANAGE_YN", "N");
                }
                ecIDMapper.update(itemMap);
                // EC_MST_ECM 히스토리 저장해야됨....


            // UPDATE
            } else if(itemMap.get("SPEC_RAWID") != null && itemMap.get("SPEC_RAWID") != "") {

                cnt += ecEditorMapper.update(itemMap);

                // 히스토리 저장
                itemMap.put("ACTION_TYPE", "U");
                itemMap.put("DESCRIPTION", "");
                this.insertHst(itemMap);
            }
        }

        return cnt;
    }

    public int deleteECSPEC(String reqText) throws Exception {

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

// 등록자(수정자) 정보 필요함
                itemMap.put("CREATE_BY", "SA");    // 로그인ID
                itemMap.put("LAST_UPDATE_BY", "SA");    // 로그인ID

                if(itemMap.get("SPEC_RAWID") != null && itemMap.get("SPEC_RAWID") != "") {

                    //itemMap.put("EC_RAWID", itemMap.get("RAWID"));
                    //itemMap.put("RAWID", itemMap.get("SPEC_RAWID"));
                    if(ecEditorMapper.delete(itemMap) > 0){
                        cnt ++;

                        // 히스토리 저장
                        itemMap.put("ACTION_TYPE", "D");
                        itemMap.put("DESCRIPTION", "Delete EC SPEC");
                        this.insertHst(itemMap);

// changeMANAGEYN == true인경우
// 실제삭제하고, manage가 Y인경우 EC_MST_ECM 테이블 EC ID Master 변경함

                        if((Boolean)itemMap.get("changeMANAGEYN") == true) {
                            // ECID 수정
                            //itemMap.put("RAWID", itemMap.get("EC_RAWID"));
                            itemMap.put("MANAGE_YN", "N");
                            ecIDMapper.update(itemMap);
                            // EC_MST_ECM 히스토리 저장해야됨....
                        }
                    }
                }

            }
        }
        return cnt;
    }


    public void insertHst(Map<String, Object> itemMap ) throws Exception {

        String oldValue = "";
        if(itemMap.get("ACTION_TYPE").equals("U") ) {
            if((Boolean)itemMap.get("MANAGE_YN") == true) {
                itemMap.put("MANAGE_YN", "Y");
            } else {
                itemMap.put("MANAGE_YN", "N");
            }
            if((Boolean)itemMap.get("ORI_MANAGE_YN") == true) {
                itemMap.put("ORI_MANAGE_YN", "Y");
            } else {
                itemMap.put("ORI_MANAGE_YN", "N");
            }

            if(!itemMap.get("MANAGE_YN").equals(itemMap.get("ORI_MANAGE_YN"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "MANAGE_YN=" + itemMap.get("ORI_MANAGE_YN");
            }
            if(!itemMap.get("TARGET").equals(itemMap.get("ORI_TARGET"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "TARGET=" + itemMap.get("ORI_TARGET");
            }
            if(!itemMap.get("LSL").equals(itemMap.get("ORI_LSL"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "LSL=" + itemMap.get("ORI_LSL");
            }
            if(!itemMap.get("USL").equals(itemMap.get("ORI_USL"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "USL=" + itemMap.get("ORI_USL");
            }
            if(!itemMap.get("INTERLOCK_YN").equals(itemMap.get("ORI_INTERLOCK_YN"))) {
                if(!oldValue.isEmpty()){
                    oldValue += "@#@";
                }
                oldValue += "INTERLOCK_YN=" + itemMap.get("ORI_INTERLOCK_YN");
            }
        }

        itemMap.put("APPROVAL_RAWID", "");
        itemMap.put("OLD_VALUE", oldValue);
        ecEditorMapper.insertHst(itemMap);
    }

}

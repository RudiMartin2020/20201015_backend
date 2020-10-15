package rms.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ECIDMapper {

    List<Map<String, Object>> getECIDList(Map<String, Object> map) throws Exception;
    List<Map<String, Object>> getCodeList(Map<String, Object> map) throws Exception;
    int update(Map<String, Object> map) throws Exception;
    int insert(Map<String, Object> map) throws Exception;
    int delete(Map<String, Object> map) throws Exception;
    int deleteECSPEC(Map<String, Object> map) throws Exception;
    int insertHst(Map<String, Object> map) throws Exception;

}

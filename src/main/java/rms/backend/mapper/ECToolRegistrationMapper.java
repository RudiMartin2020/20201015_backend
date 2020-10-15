package rms.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ECToolRegistrationMapper {

    List<Map<String, Object>> getToolList(Map<String, Object> map) throws Exception;
    List<Map<String, Object>> getInLineEQPList(Map<String, Object> map) throws Exception;
    int insert(Map<String, Object> map) throws Exception;
    int update(Map<String, Object> map) throws Exception;
    List<Map<String, Object>> getInLineEQPCheckList(Map<String, Object> map) throws Exception;
    int delete(Map<String, Object> map) throws Exception;
    int deleteECSPEC(Map<String, Object> map) throws Exception;
    int deleteEC(Map<String, Object> map) throws Exception;
    int insertHst(Map<String, Object> map) throws Exception;
}

package bh.bhback.global.common.response.service;


import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.dto.ListResult;
import bh.bhback.global.common.response.dto.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

//결과 모델에 데이터를 넣어주는 Service

@Service // 해당 Class가 Service임을 명시합니다.
public class ResponseService {

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니디."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // 성공 결과만 처리하는 메소드 (데이터 반환 X, 성공했다는 결과만 리턴) (회원 삭제 시 삭제가 성공하면 성공 응답만 반환하는 용도로 사용 가능)
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        setFailResult(result, code, msg);
        return result;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    public void setFailResult(CommonResult result, int code, String msg) {
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
    }

    // 실패 결과만 처리하는 메소드


}
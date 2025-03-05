package com.mpole.imp.api.controller.unityClient;

import com.mpole.imp.api.database.repo.UserRepository;
import com.mpole.imp.api.dto.LoginDTO;
import com.mpole.imp.api.dto.Response;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login/check")
    public Mono<Response> loginCheck(@RequestBody LoginDTO loginDTO) {
        if (StringUtils.isEmpty(loginDTO.getId()) && StringUtils.isEmpty(loginDTO.getPassword())) {
            throw new RuntimeException("아이디 or 비밀번호가 잘못되었습니다. ");
        }

        // 0. 입력값에 ID, PASSWORD 가 있는지 체크를 한다.
        // 1. ID 기반으로 DB 에서 가져온다.
        // 2. 값이 있으면 입력받은 값과 DB 비밀 번호를 해싱해서 비교하는듯

        userRepository.findAll()
                .collectList()
                .flatMapIterable(users -> {
                        int i=0;
                    return users;
                })
                .doOnNext(user -> {
                    String fuck = decryptCharStr(user.getUserPw());
                    int i=0;
                }).subscribe();

        return userRepository.findAll()
                .collectList()
                .map(Response::success);
    }

    private static final int MULTIPLE_KEY = 8; // C++의 MULTIPLE_KEY와 동일하게 설정
    private static final int PW_ENCRYPT_KEY = 12345; // C++의 PW_ENCRYPT_KEY 예시 값

    public String decryptCharStr(String encryptedStr) {
        String result = "";
        int nlen = encryptedStr.length();

        // 1. 랜덤키를 찾는다.
        byte[] szDec = new byte[2048]; // 복호화된 사용자 비밀번호를 저장할 버퍼
        byte[] szKey = new byte[MULTIPLE_KEY]; // 랜덤키 버퍼
        int nKey = 0;

        // 첫 번째 복호화 (랜덤키를 가져옴)
        DecryptInCharStr(encryptedStr.substring(0, 8), szKey, PW_ENCRYPT_KEY);

        // byte 배열을 정수로 변환 (C++의 CopyMemory 대체)
        nKey = ByteBuffer.wrap(szKey).getInt();

        // 2. 랜덤키로 암호화 해제
        int startIdx = MULTIPLE_KEY * 2;
        int length = (nlen - startIdx) / 2;
        DecryptInCharStr(encryptedStr.substring(startIdx), szDec, nKey);

        // 문자열 변환 (C++의 CString 역할 대체)
        result = new String(szDec, StandardCharsets.UTF_8).trim();

        return result;
    }

    /**
     * 암호화 해제 함수 (C++의 DecryptInCharStr을 Java로 구현)
     */
    private void DecryptInCharStr(String encryptedStr, byte[] outputBuffer, int key) {
        byte[] encryptedBytes = encryptedStr.getBytes(StandardCharsets.UTF_8);
        int length = Math.min(encryptedBytes.length, outputBuffer.length);

        for (int i = 0; i < length; i++) {
            outputBuffer[i] = (byte) (encryptedBytes[i] ^ (key & 0xFF));
        }
    }
}

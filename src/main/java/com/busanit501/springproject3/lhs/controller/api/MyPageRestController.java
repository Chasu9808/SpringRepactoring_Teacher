package com.busanit501.springproject3.lhs.controller.api;

import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageRestController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = myPageService.getUserByUsername(username);
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 정보를 찾을 수 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도하세요.");
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUserField(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody Map<String, String> updates) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            boolean isUpdated = myPageService.updateUserField(username, updates);

            if (isUpdated) {
                return ResponseEntity.ok("수정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("업데이트 실패.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도하세요.");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody Map<String, String> passwordDetails) {
        if (userDetails != null) {
            String currentPassword = passwordDetails.get("currentPassword");
            String newPassword = passwordDetails.get("newPassword");
            String confirmNewPassword = passwordDetails.get("confirmNewPassword");

            if (currentPassword == null || newPassword == null || confirmNewPassword == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모든 필드를 입력해주세요.");
            }

            if (!newPassword.equals(confirmNewPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호가 일치하지 않습니다.");
            }

            boolean isChanged = myPageService.changePassword(userDetails.getUsername(), currentPassword, newPassword);
            if (isChanged) {
                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호가 올바르지 않습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도하세요.");
        }
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody Map<String, String> passwordDetails) {
        if (userDetails != null) {
            String password = passwordDetails.get("password");

            if (password == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 입력해주세요.");
            }

            boolean isDeleted = myPageService.verifyAndDeleteUser(userDetails.getUsername(), password);

            if (isDeleted) {
                return ResponseEntity.ok("계정이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도하세요.");
        }
    }
}

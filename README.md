# 해시스냅 Back-end 테스트 과제 - JWT 기반 인증 시스템

## 1. 프로젝트 개요

Spring Boot와 React를 활용한 JWT 기반 사용자 인증 시스템입니다. 실제 서비스에서 필요한 다양한 보안 요구사항들을 고려하여 개발했습니다.

### 주요 특징

- JWT를 활용한 Stateless 인증 구현
- Access Token과 Refresh Token의 분리 저장으로 인한 보안성 강화
- Spring Security를 활용한 체계적인 보안 설계
- 실제 서비스를 고려한 예외 처리 및 에러 핸들링

## 2. 실행 방법

### 2.1 요구사항

- Java 17 이상
- Node.js 18 이상
- MySQL (프로덕션 환경용)

### 2.2 백엔드 실행

```bash
# 1. 백엔드 디렉토리로 이동
cd backend

# 2. 애플리케이션 실행
./gradlew bootRun
```

### 2.3 프론트엔드 실행

```bash
# 1. 프론트엔드 디렉토리로 이동
cd frontend

# 2. 의존성 설치
npm install

# 3. 개발 서버 실행
npm run dev
```

## 3. 기술 스택

### 백엔드

- **Framework**: Spring Boot 3.5.4
- **Security**: Spring Security, JWT
- **Database**: JPA/Hibernate, H2(개발용), MySQL(운영용)
- **Build**: Gradle
- **Validation**: Jakarta Validation
- **Mapping**: MapStruct
- **Email**: JavaMailSender

### 프론트엔드

- **Framework**: React 18, TypeScript
- **State Management**: Zustand
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **Routing**: React Router 6

## 4. API 명세서

### 인증 관련 API

#### 회원가입

- **POST** `/api/auth/signup`
- **Request**

```json
{
  "email": "string",
  "password": "string",
  "nickname": "string",
  "name": "string",
  "phoneNumber": "string"
}
```

- **Response** (200 OK)

```json
{
  "status": 200,
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "id": "number",
    "email": "string",
    "nickname": "string",
    "name": "string",
    "phoneNumber": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

#### 로그인

- **POST** `/api/auth/login`
- **Request**

```json
{
  "email": "string",
  "password": "string"
}
```

- **Response** (200 OK)

```json
{
  "status": 200,
  "data": {
    "accessToken": "string",
    "userInfo": {
      "id": "number",
      "email": "string",
      "nickname": "string",
      "name": "string",
      "phoneNumber": "string",
      "createdAt": "datetime",
      "updatedAt": "datetime"
    }
  }
}
```

- **Cookie**
  - `refreshToken`: HTTP-Only 쿠키로 설정됨

#### 로그아웃

- **POST** `/api/auth/logout`
- **Response** (200 OK)

```json
{
  "status": 200,
  "message": "로그아웃이 완료되었습니다."
}
```

- **Cookie**
  - `refreshToken`: 삭제됨

#### 토큰 갱신

- **POST** `/api/auth/refresh`
- **Cookie 필요**
  - `refreshToken`: 기존 리프레시 토큰
- **Response** (200 OK)

```json
{
  "status": 200,
  "data": {
    "accessToken": "string"
  }
}
```

#### 비밀번호 재설정 요청

- **POST** `/api/auth/password/reset-request`
- **Request**

```json
{
  "email": "string"
}
```

- **Response** (200 OK)

```json
{
  "status": 200,
  "message": "비밀번호 재설정 이메일이 발송되었습니다."
}
```

#### 비밀번호 재설정

- **POST** `/api/auth/password/reset`
- **Request**

```json
{
  "token": 200,
  "newPassword": "string",
  "confirmPassword": "string"
}
```

- **Response** (200 OK)

```json
{
  "status": 200,
  "message": "비밀번호가 성공적으로 재설정되었습니다."
}
```

### 사용자 관리 API

#### 프로필 수정

- **PUT** `/api/auth/profile`
- **Headers**
  - `Authorization`: Bearer {accessToken}
- **Request**

```json
{
  "nickname": "string",
  "name": "string",
  "phoneNumber": "string"
}
```

- **Response** (200 OK)

```json
{
  "status": 200,
  "message": "프로필이 수정되었습니다.",
  "data": {
    "id": "number",
    "email": "string",
    "nickname": "string",
    "name": "string",
    "phoneNumber": "string",
    "updatedAt": "datetime"
  }
}
```

#### 비밀번호 변경

- **PUT** `/api/auth/password`
- **Headers**
  - `Authorization`: Bearer {accessToken}
- **Request**

```json
{
  "currentPassword": "string",
  "newPassword": "string",
  "confirmPassword": "string"
}
```

- **Response** (200 OK)

```json
{
  "status": 200,
  "message": "비밀번호가 변경되었습니다."
}
```

## 5. 구현 범위

### 인증 시스템

- ✅ 회원가입
- ✅ 로그인/로그아웃
- ✅ JWT 기반 인증
- ✅ Access Token / Refresh Token 관리
- ✅ 비밀번호 재설정 (이메일 인증)

### 사용자 관리

- ✅ 프로필 조회/수정
- ✅ 비밀번호 변경
- ✅ 회원 정보 유효성 검증

### 보안

- ✅ Spring Security 기반 보안 설정
- ✅ 비밀번호 암호화 (BCrypt)
- ✅ CORS 설정
- ✅ XSS 방지
- ✅ CSRF 보호

## 6. 특별히 신경 쓴 부분

### 6.1 토큰 관리 시스템

- Access Token과 Refresh Token을 분리하여 보안성 강화
- Refresh Token은 HTTP-Only 쿠키로 관리하여 XSS 공격 방지

```java
// Refresh Token을 HTTP-only Cookie로 설정
public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie refreshTokenCookie = new Cookie(AppConstants.Http.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(false); // 개발환경에서는 false, 운영환경에서는 true
    refreshTokenCookie.setPath("/auth");
    refreshTokenCookie.setMaxAge((int) (AppConstants.JWT.REFRESH_TOKEN_EXPIRATION_MS / 1000));
    response.addCookie(refreshTokenCookie);

    log.debug("Refresh Token Cookie 설정 완료");
}
```

### 6.2 예외 처리

- 계층별 Custom Exception 구현으로 명확한 에러 처리
- Global Exception Handler를 통한 일관된 에러 응답 포맷

```java
    // 사용자를 찾을 수 없음 예외 처리
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
    log.warn("사용자 조회 실패: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.<Void>error(404, ex.getMessage()));
}
```

### 6.3 이메일 인증 시스템

- 비동기 이메일 발송으로 응답 지연 최소화, 사용자 경험 향상
- 토큰 기반 인증 링크로 보안성 확보
- 만료 시간 자동 관리

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 */1 * * *") // 매 시간마다 실행
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        log.info("만료된 비밀번호 재설정 토큰 정리 시작: {}", now);

        try {
            passwordResetTokenRepository.deleteByExpiryDateBeforeAndUsedAtIsNull(now);
            log.info("만료된 비밀번호 재설정 토큰 정리 완료");
        } catch (Exception e) {
            log.error("만료된 비밀번호 재설정 토큰 정리 중 오류 발생", e);
        }
    }
}
```

### 6.4 비동기 처리 최적화

- @Async 어노테이션을 활용한 비동기 작업 처리
- ThreadPoolTaskExecutor 커스텀 설정으로 성능 최적화

```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 기본 실행 대기 중인 Thread 수
        executor.setMaxPoolSize(5); // 동시 동작하는 최대 Thread 수
        executor.setQueueCapacity(10); // Thread Pool Queue 크기
        executor.setThreadNamePrefix("EmailAsync-");
        executor.initialize();
        return executor;
    }
}
```


## 7. 추가 구현사항

### 7.1 [비로그인] 비밀번호 재설정 이메일 인증 기능

- 비로그인 상태에서 비밀번호 찾기 기능 구현
- 사용자 이메일로 임시 토큰이 포함된 재설정 링크 발송
- 토큰 유효시간 30분 설정 및 자동 만료 처리
- 비동기 이메일 발송 처리
  - @Async 어노테이션으로 비동기 처리
  - ThreadPool 설정으로 동시 처리 최적화
- 재설정 링크 클릭 시 새 비밀번호 설정 페이지로 이동

### 7.2 [로그인] 비밀번호 변경 기능

- 로그인 상태에서 현재 비밀번호 입력 후 새 비밀번호로 변경
- BCrypt를 사용한 안전한 비밀번호 암호화
- 비밀번호 유효성 검사 (6자 이상)
- 현재 비밀번호 검증 실패 시 에러 메시지 제공

### 7.3 [로그인] 회원정보 수정 기능

- 프로필 정보(닉네임, 이름, 전화번호) 수정 기능
- JWT 토큰으로 사용자 인증 후 수정 권한 확인
- 각 필드별 유효성 검사 (닉네임 중복 체크 등)
- 수정된 정보는 즉시 반영되며 수정 시간 기록
- 프로필 수정 히스토리 관리를 위한 updatedAt 필드 자동 업데이트

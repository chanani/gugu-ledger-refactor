# 회원(User) 도메인 기능 요구사항

## 개요

사용자 계정 관리, 인증/인가, 프로필 관리, 아이디/비밀번호 찾기 기능을 담당한다.

---

## 엔티티

### User

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long (PK) | 사용자 고유 ID |
| `userId` | String | 로그인 아이디 |
| `password` | String | BCrypt 암호화 비밀번호 |
| `email` | String | 이메일 주소 |
| `lastVisit` | LocalDateTime | 마지막 방문 시각 |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기능 요구사항

### 1. 회원가입

- **Endpoint**: `POST /api/v1/none/join`
- **인증**: 불필요

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `userId` | String | Y | 로그인 아이디 |
| `password` | String | Y | 비밀번호 (BCrypt 암호화 저장) |
| `email` | String | Y | 이메일 주소 |

**비즈니스 규칙**
- `userId` 중복 불가 (사전 중복 확인 API 활용 권장)
- `email` 중복 불가 (ACTIVE 상태 기준)
- 회원가입 완료 시 기본 카테고리 14개(지출) + 2개(수입) 자동 생성
  - 지출: 주거비, 식비, 교통, 의료/건강, 문화/여가, 쇼핑, 카페/간식, 술/유흥, 교육, 경조사, 여행, 통신, 세금/이자, 기타
  - 수입: 월급, 용돈

**응답**: `ApiResponse` (성공 메시지)

---

### 2. 로그인

- **Endpoint**: `POST /api/v1/none/login`
- **인증**: 불필요

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `userId` | String | Y | 로그인 아이디 |
| `password` | String | Y | 비밀번호 |

**비즈니스 규칙**
- 존재하지 않거나 DELETED 상태인 사용자는 로그인 불가
- 비밀번호 BCrypt 검증
- 로그인 성공 시 `lastVisit` 갱신
- Access Token(10h) + Refresh Token(24h) 발급

**응답**
```json
{
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
  }
}
```

---

### 3. 아이디 중복 확인

- **Endpoint**: `GET /api/v1/none/check/id`
- **인증**: 불필요

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `userId` | String | Y | 확인할 아이디 |

**응답**: `ApiResponse` (사용 가능 여부)

---

### 4. 이메일 중복 확인

- **Endpoint**: `GET /api/v1/none/check/email`
- **인증**: 불필요

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `email` | String | Y | 확인할 이메일 |

**비즈니스 규칙**
- ACTIVE 상태인 사용자의 이메일과 중복 여부 확인

**응답**: `ApiResponse`

---

### 5. 이메일 인증코드 발송

- **Endpoint**: `POST /api/v1/none/email-send`
- **인증**: 불필요

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `email` | String | Y | 인증코드를 받을 이메일 |

**비즈니스 규칙**
- 6자리 랜덤 인증코드 생성
- SMTP를 통해 이메일 발송
- 인증코드는 Redis에 저장 (TTL 설정)

**응답**: `ApiResponse`

---

### 6. 이메일 인증코드 확인

- **Endpoint**: `GET /api/v1/none/email-check`
- **인증**: 불필요

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `email` | String | Y | 인증 요청한 이메일 |
| `authNumber` | String | Y | 입력한 인증코드 |

**비즈니스 규칙**
- Redis에서 이메일 기반으로 저장된 인증코드와 비교
- 만료 또는 불일치 시 오류 반환

**응답**: `ApiResponse`

---

### 7. 아이디 찾기

- **Endpoint**: `GET /api/v1/none/find-id`
- **인증**: 불필요

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `email` | String | Y | 가입한 이메일 |

**비즈니스 규칙**
- 이메일로 사용자 조회 (ACTIVE 상태)
- 아이디 마스킹 처리 후 반환 (보안)

**응답**
```json
{ "data": { "userId": "ho***" } }
```

---

### 8. 비밀번호 재설정 (찾기)

- **Endpoint**: `PUT /api/v1/none/update-find-password`
- **인증**: 불필요

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `userId` | String | Y | 로그인 아이디 |
| `email` | String | Y | 가입한 이메일 |
| `password` | String | Y | 새 비밀번호 |

**비즈니스 규칙**
- `userId` + `email` 조합으로 사용자 확인 (ACTIVE 상태)
- 이메일 인증 완료 후 호출 권장
- 새 비밀번호 BCrypt 암호화 후 저장

**응답**: `ApiResponse`

---

### 9. 내 정보 조회

- **Endpoint**: `GET /api/v1/user/info`
- **인증**: 필요 (ROLE_USER)

**응답**
```json
{
  "data": {
    "userId": "hong123",
    "email": "hong@example.com"
  }
}
```

---

### 10. 내 정보 수정

- **Endpoint**: `PUT /api/v1/user/info`
- **인증**: 필요 (ROLE_USER)

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `email` | String | Y | 새 이메일 주소 |

**응답**: `ApiResponse`

---

### 11. 비밀번호 변경 (로그인 상태)

- **Endpoint**: `PUT /api/v1/user/update-password`
- **인증**: 필요 (ROLE_USER)

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `currentPassword` | String | Y | 현재 비밀번호 |
| `newPassword` | String | Y | 새 비밀번호 |

**비즈니스 규칙**
- 현재 비밀번호 BCrypt 검증 후 변경
- 현재 비밀번호 불일치 시 오류 반환

**응답**: `ApiResponse`

---

## 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| 1100 | 400 | 사용자를 찾을 수 없음 |
| 1101 | 400 | 아이디 중복 |
| 1102 | 400 | 이메일 중복 |
| 1103 | 400 | 비밀번호 불일치 |
| 1104 | 400 | 이메일 인증코드 불일치 또는 만료 |
| 1050 | 401 | 만료된 Access Token |
| 1051 | 403 | 유효하지 않은 Token |

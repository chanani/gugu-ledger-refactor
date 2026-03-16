# GuguBank API - 기능 요구사항 문서

개인 가계부 서비스 **GuguBank**의 도메인별 기능 요구사항 문서입니다.

## 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.3 |
| Auth | Spring Security + JWT (HS512) |
| ORM | Spring Data JPA + QueryDSL 5.0 |
| DB | MySQL |
| Cache | Redis |
| Docs | Swagger / OpenAPI 3.0 |
| File | SFTP + Thumbnailator (이미지 리사이징) |

## 공통 규칙

### API 구조

| 경로 | 설명 |
|------|------|
| `/api/v1/none/**` | 인증 불필요 (회원가입, 로그인, 이메일 인증 등) |
| `/api/v1/user/**` | 인증 필요 (`ROLE_USER`, JWT Bearer Token) |

### 응답 형식

**성공 응답**
```json
{ "code": 200, "status": 200, "message": "SUCCESS" }
```

**데이터 응답**
```json
{ "data": { ... } }
```

**에러 응답**
```json
{ "status": 400, "code": 1100, "message": "에러 메시지" }
```

### 공통 Enum

| Enum | 값 |
|------|----|
| `RecordType` | `ALL`, `DEPOSIT`(수입), `WITHDRAW`(지출) |
| `PriceType` | `CARD`(신용카드), `CHECK_CARD`(체크카드), `CASH`(현금), `BANK`(계좌이체) |
| `StatusType` | `ACTIVE`, `INACTIVE`, `DELETED` (소프트 삭제) |
| `BooleanYn` | `Y`, `N` |

### 인증

- **Access Token**: 유효시간 10시간 (HS512)
- **Refresh Token**: 유효시간 24시간
- 모든 보호 API는 `Authorization: Bearer <access_token>` 헤더 필요

## 도메인 문서 목록

| 도메인 | 파일 | 설명 |
|--------|------|------|
| 회원 | [requirements/user.md](./requirements/user.md) | 회원가입, 로그인, 프로필 관리, 이메일 인증 |
| 카테고리 | [requirements/category.md](./requirements/category.md) | 수입/지출 카테고리 관리, 순서 관리 |
| 아이콘 | [requirements/icon.md](./requirements/icon.md) | 카테고리 아이콘 목록 조회 |
| 거래내역 | [requirements/records.md](./requirements/records.md) | 수입/지출 내역 CRUD, 이미지 첨부, 달력/일별/월별 조회 |
| 즐겨찾기 | [requirements/records-favorite.md](./requirements/records-favorite.md) | 거래내역 템플릿(즐겨찾기) 관리 |
| 자산 | [requirements/assets.md](./requirements/assets.md) | 자산 그룹(통장, 지갑 등) 관리 |
| 자산 상세 | [requirements/assets-detail.md](./requirements/assets-detail.md) | 자산 기반 거래내역, 잔액 추적, 페이지네이션 |
| 그래프 | [requirements/graph.md](./requirements/graph.md) | 카테고리별 통계 및 시각화 데이터 |

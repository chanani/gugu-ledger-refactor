# 아이콘(Icon) 도메인 기능 요구사항

## 개요

카테고리에 적용할 수 있는 아이콘 이미지 목록을 관리한다. 시스템에서 사전에 등록된 아이콘을 사용자가 선택하여 카테고리에 적용한다.

---

## 엔티티

### Icon

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Integer (PK) | 아이콘 고유 ID |
| `path` | String | 아이콘 이미지 경로 |
| `name` | String | 아이콘 이름 |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기능 요구사항

### 1. 아이콘 목록 조회

- **Endpoint**: `GET /api/v1/user/icons`
- **인증**: 필요 (ROLE_USER)

**비즈니스 규칙**
- ACTIVE 상태인 아이콘만 반환

**응답**
```json
{
  "data": {
    "icons": [
      {
        "id": 1,
        "name": "식비",
        "path": "/icons/food.png"
      },
      {
        "id": 2,
        "name": "교통",
        "path": "/icons/transport.png"
      }
    ]
  }
}
```

---

## 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| 1150 | 400 | 아이콘을 찾을 수 없음 |

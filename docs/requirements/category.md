# 카테고리(Category) 도메인 기능 요구사항

## 개요

거래내역의 수입/지출을 분류하는 카테고리를 관리한다. 사용자별 독립적인 카테고리를 보유하며, 아이콘과 순서를 설정할 수 있다.

---

## 엔티티

### Category

| 필드       | 타입         | 설명                             |
|----------|------------|--------------------------------|
| `id`     | Long (PK)  | 카테고리 고유 ID                     |
| `user`   | User (FK)  | 소유 사용자                         |
| `type`   | RecordType | `DEPOSIT`(수입) / `WITHDRAW`(지출) |
| `name`   | String     | 카테고리 이름                        |
| `icon`   | Icon (FK)  | 연결된 아이콘                        |
| `orders` | Integer    | 표시 순서                          |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기본 카테고리 (회원가입 시 자동 생성)

| 타입            | 이름                                                                     |
|---------------|------------------------------------------------------------------------|
| WITHDRAW (지출) | 주거비, 식비, 교통, 의료/건강, 문화/여가, 쇼핑, 카페/간식, 술/유흥, 교육, 경조사, 여행, 통신, 세금/이자, 기타 |
| DEPOSIT (수입)  | 월급, 용돈                                                                 |

---

## 기능 요구사항

### 1. 카테고리 생성

- **Endpoint**: `POST /api/v1/user/categories`
- **인증**: 필요 (ROLE_USER)

**요청**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `name` | String | Y | 카테고리 이름 |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `iconId` | Integer | Y | 아이콘 ID |

**비즈니스 규칙**

- 현재 사용자의 같은 타입 카테고리 중 최댓값 `orders` + 1로 신규 순서 지정
- 해당 타입의 기존 카테고리 목록 마지막에 추가

**응답**: `ApiResponse`

---

### 2. 카테고리 목록 조회

- **Endpoint**: `GET /api/v1/user/categories`
- **인증**: 필요 (ROLE_USER)

**요청 파라미터**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `type` | RecordType | N | `DEPOSIT`, `WITHDRAW`, `ALL` (기본값: `ALL`) |

**비즈니스 규칙**

- `type=ALL`이면 수입/지출 카테고리 모두 반환
- `orders` 오름차순 정렬

**응답**

```json
{
  "data": {
    "categories": [
      {
        "id": 1,
        "name": "식비",
        "type": "WITHDRAW",
        "iconId": 3,
        "iconPath": "/icons/food.png",
        "orders": 1
      }
    ]
  }
}
```

---

### 3. 카테고리 단건 조회

- **Endpoint**: `GET /api/v1/user/categories/{categoryId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**

| 변수 | 타입 | 설명 |
|------|------|------|
| `categoryId` | Long | 카테고리 ID |

**비즈니스 규칙**

- ACTIVE 상태인 카테고리만 조회 가능

**응답**

```json
{
  "data": {
    "id": 1,
    "name": "식비",
    "type": "WITHDRAW",
    "iconId": 3,
    "iconPath": "/icons/food.png",
    "orders": 2
  }
}
```

---

### 4. 카테고리 수정

- **Endpoint**: `PUT /api/v1/user/categories/{categoryId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**

| 변수 | 타입 | 설명 |
|------|------|------|
| `categoryId` | Long | 카테고리 ID |

**요청**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `name` | String | Y | 카테고리 이름 |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `iconId` | Integer | Y | 아이콘 ID |

**비즈니스 규칙**

- ACTIVE 상태인 카테고리만 수정 가능

**응답**: `ApiResponse`

---

### 5. 카테고리 삭제

- **Endpoint**: `DELETE /api/v1/user/categories/{categoryId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `categoryId` | Long | 카테고리 ID |

**비즈니스 규칙**

- 소프트 삭제 (`status = DELETED`)

**응답**: `ApiResponse`

---

### 6. 카테고리 순서 일괄 변경

- **Endpoint**: `PUT /api/v1/user/categories-order`
- **인증**: 필요 (ROLE_USER)

**요청** (배열)

```json
[
  {
    "categoryId": 3,
    "orders": 1
  },
  {
    "categoryId": 1,
    "orders": 2
  },
  {
    "categoryId": 5,
    "orders": 3
  }
]
```

| 필드           | 타입      | 필수 | 설명      |
|--------------|---------|----|---------|
| `categoryId` | Long    | Y  | 카테고리 ID |
| `orders`     | Integer | Y  | 변경할 순서  |

**비즈니스 규칙**

- 요청 배열에 포함된 카테고리의 `orders` 일괄 업데이트
- 순서 범위 내의 카테고리만 대상으로 처리

**응답**: `ApiResponse`

---

## 에러 코드

| 코드   | HTTP | 설명            |
|------|------|---------------|
| 1200 | 400  | 카테고리를 찾을 수 없음 |

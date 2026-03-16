# 즐겨찾기(RecordsFavorite) 도메인 기능 요구사항

## 개요

자주 사용하는 거래내역을 템플릿으로 저장하여 빠르게 재사용할 수 있다. 자산, 카테고리, 금액, 결제 수단 등을 미리 저장해두고 거래내역 등록 시 불러올 수 있다.

---

## 엔티티

### RecordsFavorite

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long (PK) | 즐겨찾기 고유 ID |
| `user` | User (FK) | 소유 사용자 |
| `category` | Category (FK) | 거래 카테고리 |
| `assets` | Assets (FK, nullable) | 연결된 자산 |
| `title` | String | 즐겨찾기 이름 |
| `type` | RecordType | `DEPOSIT`(수입) / `WITHDRAW`(지출) |
| `price` | Long | 금액 |
| `priceType` | PriceType | 결제 수단 |
| `monthly` | Integer | 할부 개월 수 |
| `memo` | String | 메모 |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기능 요구사항

### 1. 즐겨찾기 생성

- **Endpoint**: `POST /api/v1/user/records-favorite`
- **인증**: 필요 (ROLE_USER)

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | Y | 즐겨찾기 이름 |
| `categoryId` | Long | Y | 카테고리 ID |
| `assetsId` | Long | N | 자산 ID |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `price` | Long | Y | 금액 |
| `priceType` | PriceType | Y | 결제 수단 |
| `monthly` | Integer | N | 할부 개월 수 |
| `memo` | String | N | 메모 |

**응답**: `ApiResponse`

---

### 2. 즐겨찾기 목록 조회

- **Endpoint**: `GET /api/v1/user/records-favorite`
- **인증**: 필요 (ROLE_USER)

**비즈니스 규칙**
- 현재 사용자의 즐겨찾기만 반환
- 최신 생성 순 정렬 (`createdAt DESC`)
- ACTIVE 상태만 반환

**응답**
```json
{
  "data": {
    "favorites": [
      {
        "id": 1,
        "title": "월급",
        "categoryId": 15,
        "categoryName": "월급",
        "assetsId": 1,
        "assetsName": "신한은행",
        "type": "DEPOSIT",
        "price": 3000000,
        "priceType": "BANK",
        "monthly": 0,
        "memo": "매월 25일"
      }
    ]
  }
}
```

---

### 3. 즐겨찾기 단건 조회

- **Endpoint**: `GET /api/v1/user/records-favorite/{recordsFavoriteId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsFavoriteId` | Long | 즐겨찾기 ID |

**응답**
```json
{
  "data": {
    "id": 1,
    "title": "월급",
    "categoryId": 15,
    "categoryName": "월급",
    "assetsId": 1,
    "assetsName": "신한은행",
    "type": "DEPOSIT",
    "price": 3000000,
    "priceType": "BANK",
    "monthly": 0,
    "memo": "매월 25일"
  }
}
```

---

### 4. 즐겨찾기 수정

- **Endpoint**: `PUT /api/v1/user/records-favorite/{recordsFavoriteId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsFavoriteId` | Long | 즐겨찾기 ID |

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | Y | 즐겨찾기 이름 |
| `categoryId` | Long | Y | 카테고리 ID |
| `assetsId` | Long | N | 자산 ID |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `price` | Long | Y | 금액 |
| `priceType` | PriceType | Y | 결제 수단 |
| `monthly` | Integer | N | 할부 개월 수 |
| `memo` | String | N | 메모 |

**응답**: `ApiResponse`

---

### 5. 즐겨찾기 삭제

- **Endpoint**: `DELETE /api/v1/user/records-favorite/{recordsFavoriteId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsFavoriteId` | Long | 즐겨찾기 ID |

**비즈니스 규칙**
- 소프트 삭제 (`status = DELETED`)

**응답**: `ApiResponse`

---

## 사용 시나리오

1. 사용자가 자주 사용하는 거래(예: 월급 입금, 정기 구독료)를 즐겨찾기로 저장
2. 거래내역 등록 화면에서 즐겨찾기 목록 조회
3. 원하는 즐겨찾기 선택 시 해당 정보(카테고리, 금액, 결제 수단 등)가 자동으로 입력 폼에 채워짐
4. 날짜만 변경 후 바로 등록

---

## 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| 1301 | 400 | 즐겨찾기를 찾을 수 없음 |
| 1302 | 400 | 즐겨찾기 접근 권한 없음 |

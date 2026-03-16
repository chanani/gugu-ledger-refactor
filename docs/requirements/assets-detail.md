# 자산 상세(AssetsDetail) 도메인 기능 요구사항

## 개요

특정 자산(Assets)에 연결된 거래 내역을 관리한다. 각 거래 후 잔액을 추적하며, 거래내역(Records)과 양방향 연동된다. 무한 스크롤 기반 페이지네이션으로 대용량 내역을 조회한다.

---

## 엔티티

### AssetsDetail

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long (PK) | 자산 상세 고유 ID |
| `user` | User (FK) | 소유 사용자 |
| `assets` | Assets (FK) | 연결된 자산 |
| `category` | Category (FK) | 거래 카테고리 |
| `record` | Records (FK, nullable) | 연결된 거래내역 |
| `type` | RecordType | `DEPOSIT`(수입) / `WITHDRAW`(지출) |
| `priceType` | PriceType | 결제 수단 |
| `price` | Long | 금액 (지출은 음수) |
| `balance` | Long | 해당 거래 후 잔액 |
| `useDate` | LocalDate | 거래 날짜 |
| `active` | BooleanYn | 거래내역 목록 표시 여부 (`Y`/`N`) |
| `memo` | String | 메모 |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기능 요구사항

### 1. 자산 상세 내역 생성

- **Endpoint**: `POST /api/v1/user/assets-detail`
- **인증**: 필요 (ROLE_USER)

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `assetsId` | Long | Y | 자산 ID |
| `categoryId` | Long | Y | 카테고리 ID |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `priceType` | PriceType | Y | 결제 수단 |
| `price` | Long | Y | 금액 (양수 입력) |
| `useDate` | String | Y | 거래 날짜 (`YYYY-MM-DD`) |
| `memo` | String | N | 메모 |

**비즈니스 규칙**
- 자산 상세 생성 시 대응되는 `Records` 엔티티도 자동 생성
- 거래 후 잔액(`balance`) = 이전 잔액 + 금액 (지출은 음수 적용)
- 연결된 자산(`Assets`)의 `balance` 자동 갱신

**응답**: `ApiResponse`

---

### 2. 자산 상세 내역 목록 조회 (무한 스크롤)

- **Endpoint**: `GET /api/v1/user/assets-details`
- **인증**: 필요 (ROLE_USER)

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `assetsId` | Long | Y | 자산 ID |
| `page` | Integer | Y | 페이지 번호 (1부터 시작) |
| `size` | Integer | Y | 페이지 크기 (예: 10) |
| `type` | RecordType | N | 거래 타입 필터 (`DEPOSIT`/`WITHDRAW`) |
| `priceType` | PriceType | N | 결제 수단 필터 |
| `startDate` | String | N | 조회 시작 날짜 (`YYYY-MM-DD`) |
| `endDate` | String | N | 조회 종료 날짜 (`YYYY-MM-DD`) |

**비즈니스 규칙**
- `Slice` 기반 페이지네이션 (다음 페이지 존재 여부만 반환, 전체 개수 미반환)
- 무한 스크롤 UI에 최적화
- `active = Y`인 항목만 기본 표시
- 최신 거래 순 정렬 (useDate DESC)

**응답**
```json
{
  "data": {
    "hasNext": true,
    "details": [
      {
        "id": 5,
        "assetsId": 1,
        "assetsName": "신한은행",
        "categoryName": "식비",
        "type": "WITHDRAW",
        "priceType": "CARD",
        "price": -15000,
        "balance": 2485000,
        "useDate": "2026-03-15",
        "memo": "점심"
      }
    ]
  }
}
```

---

### 3. 자산 상세 내역 단건 조회

- **Endpoint**: `GET /api/v1/user/assets-details/{assetsDetailId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `assetsDetailId` | Long | 자산 상세 ID |

**응답**
```json
{
  "data": {
    "id": 5,
    "assetsId": 1,
    "assetsName": "신한은행",
    "categoryId": 2,
    "categoryName": "식비",
    "recordId": 10,
    "type": "WITHDRAW",
    "priceType": "CARD",
    "price": -15000,
    "balance": 2485000,
    "useDate": "2026-03-15",
    "active": "Y",
    "memo": "점심"
  }
}
```

---

### 4. 자산 상세 내역 수정

- **Endpoint**: `PUT /api/v1/user/assets-detail/{assetsDetailId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `assetsDetailId` | Long | 자산 상세 ID |

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `categoryId` | Long | Y | 카테고리 ID |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `priceType` | PriceType | Y | 결제 수단 |
| `price` | Long | Y | 금액 |
| `useDate` | String | Y | 거래 날짜 |
| `memo` | String | N | 메모 |

**비즈니스 규칙**
- 연결된 `Records` 엔티티도 함께 갱신
- 기존 금액 원복 후 새 금액으로 자산 잔액 재계산

**응답**: `ApiResponse`

---

### 5. 자산 상세 내역 삭제

- **Endpoint**: `DELETE /api/v1/user/assets-detail/{assetsDetailId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `assetsDetailId` | Long | 자산 상세 ID |

**비즈니스 규칙**
- 소프트 삭제 (`status = DELETED`)
- 연결된 `Records`도 함께 소프트 삭제
- 자산 잔액에서 해당 금액 역산하여 갱신

**응답**: `ApiResponse`

---

## Records와의 연동 관계

```
Records (거래내역)
    ↕ 양방향 연동
AssetsDetail (자산 상세)
    ↓ 잔액 갱신
Assets (자산)
```

- **Records → AssetsDetail**: 거래내역 등록 시 자산 연동(`assetsId` 포함)하면 AssetsDetail 자동 생성
- **AssetsDetail → Records**: AssetsDetail 독립 생성 시 Records 자동 생성
- **삭제 연동**: 한쪽 삭제 시 연결된 상대방도 소프트 삭제

---

## 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| 1250 | 400 | 자산을 찾을 수 없음 |
| 1251 | 400 | 자산 접근 권한 없음 |

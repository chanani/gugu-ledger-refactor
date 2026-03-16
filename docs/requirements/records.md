# 거래내역(Records) 도메인 기능 요구사항

## 개요

수입/지출 거래내역을 등록하고 관리한다. 이미지 첨부, 메모, 할부 정보를 지원하며 일별/월별/달력 형태로 조회할 수 있다. 자산(Assets)과 연동하여 잔액을 자동 갱신한다.

---

## 엔티티

### Records

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long (PK) | 거래내역 고유 ID |
| `user` | User (FK) | 소유 사용자 |
| `category` | Category (FK) | 거래 카테고리 |
| `assets` | Assets (FK, nullable) | 연결된 자산 |
| `type` | RecordType | `DEPOSIT`(수입) / `WITHDRAW`(지출) |
| `price` | Long | 금액 (지출은 음수로 저장) |
| `priceType` | PriceType | 결제 수단 (`CARD`, `CHECK_CARD`, `CASH`, `BANK`) |
| `monthly` | Integer | 할부 개월 수 (일시불: 0 또는 null) |
| `memo` | String | 메모 |
| `useDate` | LocalDate | 거래 날짜 |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기능 요구사항

### 1. 거래내역 등록

- **Endpoint**: `POST /api/v1/user/records`
- **인증**: 필요 (ROLE_USER)
- **Content-Type**: `multipart/form-data`

**요청 (form-data)**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `categoryId` | Long | Y | 카테고리 ID |
| `assetsId` | Long | N | 연결할 자산 ID (없으면 자산 연동 없음) |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `price` | Long | Y | 금액 (양수 입력, 지출은 서버에서 음수 변환) |
| `priceType` | PriceType | Y | 결제 수단 |
| `monthly` | Integer | N | 할부 개월 수 |
| `memo` | String | N | 메모 |
| `useDate` | String | Y | 거래 날짜 (`YYYY-MM-DD`) |
| `images` | MultipartFile[] | N | 첨부 이미지 (복수 허용) |

**비즈니스 규칙**
- 이미지는 업로드 시 자동 리사이징 후 SFTP 서버에 저장
- `assetsId` 입력 시 해당 자산의 `balance` 자동 갱신
- 자산 연동 시 `AssetsDetail` 레코드 자동 생성
- 지출(`WITHDRAW`)은 금액을 음수로 변환하여 저장

**응답**: `ApiResponse`

---

### 2. 거래내역 단건 조회

- **Endpoint**: `GET /api/v1/user/records/{recordsId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsId` | Long | 거래내역 ID |

**응답**
```json
{
  "data": {
    "id": 10,
    "categoryId": 2,
    "categoryName": "식비",
    "assetsId": 1,
    "assetsName": "신한은행",
    "type": "WITHDRAW",
    "price": 15000,
    "priceType": "CARD",
    "monthly": 0,
    "memo": "점심",
    "useDate": "2026-03-15",
    "images": [
      { "id": 1, "path": "/uploads/image1.jpg" }
    ]
  }
}
```

---

### 3. 일별 거래내역 조회

- **Endpoint**: `GET /api/v1/user/records-current`
- **인증**: 필요 (ROLE_USER)

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `currentDate` | String | Y | 조회 날짜 (`YYYY-MM-DD`) |

**비즈니스 규칙**
- 지정 날짜의 모든 거래내역 반환
- ACTIVE 상태만 조회

**응답**
```json
{
  "data": {
    "records": [
      {
        "id": 10,
        "categoryName": "식비",
        "type": "WITHDRAW",
        "price": 15000,
        "priceType": "CARD",
        "memo": "점심",
        "useDate": "2026-03-15"
      }
    ]
  }
}
```

---

### 4. 월별 거래내역 조회

- **Endpoint**: `GET /api/v1/user/records`
- **인증**: 필요 (ROLE_USER)

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `yearMonth` | String | Y | 조회 년월 (`YYYY-MM`) |

**비즈니스 규칙**
- 해당 월의 거래내역을 날짜별로 그룹화하여 반환
- 날짜별 수입 합계, 지출 합계 포함

**응답**
```json
{
  "data": {
    "records": [
      {
        "useDate": "2026-03-15",
        "totalDeposit": 500000,
        "totalWithdraw": 35000,
        "items": [
          {
            "id": 10,
            "categoryName": "식비",
            "type": "WITHDRAW",
            "price": 15000,
            "memo": "점심"
          }
        ]
      }
    ]
  }
}
```

---

### 5. 달력 조회 (월별 수입/지출 합계)

- **Endpoint**: `GET /api/v1/user/records-calendar`
- **인증**: 필요 (ROLE_USER)

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `yearMonth` | String | Y | 조회 년월 (`YYYY-MM`) |

**비즈니스 규칙**
- 달력 UI 렌더링을 위해 날짜별 수입/지출 합계만 반환
- 상세 거래 내용은 미포함

**응답**
```json
{
  "data": {
    "calendar": [
      {
        "date": "2026-03-15",
        "totalDeposit": 500000,
        "totalWithdraw": 35000
      },
      {
        "date": "2026-03-16",
        "totalDeposit": 0,
        "totalWithdraw": 12000
      }
    ]
  }
}
```

---

### 6. 거래내역 수정

- **Endpoint**: `PUT /api/v1/user/records/{recordsId}`
- **인증**: 필요 (ROLE_USER)
- **Content-Type**: `multipart/form-data`

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsId` | Long | 거래내역 ID |

**요청 (form-data)**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `categoryId` | Long | Y | 카테고리 ID |
| `assetsId` | Long | N | 자산 ID |
| `type` | RecordType | Y | `DEPOSIT` / `WITHDRAW` |
| `price` | Long | Y | 금액 |
| `priceType` | PriceType | Y | 결제 수단 |
| `monthly` | Integer | N | 할부 개월 수 |
| `memo` | String | N | 메모 |
| `useDate` | String | Y | 거래 날짜 |
| `deleteImageIds` | Long[] | N | 삭제할 이미지 ID 목록 |
| `images` | MultipartFile[] | N | 새로 추가할 이미지 |

**비즈니스 규칙**
- 기존 이미지 중 `deleteImageIds`에 포함된 이미지 삭제
- 새 이미지 추가 업로드
- 자산 연동 변경 시 기존 잔액 원복 후 새 잔액 적용
- 연결된 `AssetsDetail` 정보도 함께 갱신

**응답**: `ApiResponse`

---

### 7. 메모 수정

- **Endpoint**: `PUT /api/v1/user/records-memo/{recordsId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsId` | Long | 거래내역 ID |

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `memo` | String | Y | 새 메모 내용 |

**비즈니스 규칙**
- 메모만 단독으로 빠르게 수정하는 경량 API

**응답**: `ApiResponse`

---

### 8. 거래내역 삭제

- **Endpoint**: `DELETE /api/v1/user/records/{recordsId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `recordsId` | Long | 거래내역 ID |

**비즈니스 규칙**
- 소프트 삭제 (`status = DELETED`)
- 연결된 `AssetsDetail`도 함께 소프트 삭제
- 자산 잔액에서 해당 금액 차감 반영

**응답**: `ApiResponse`

---

## 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| 1300 | 400 | 거래내역을 찾을 수 없음 |

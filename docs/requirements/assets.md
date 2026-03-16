# 자산(Assets) 도메인 기능 요구사항

## 개요

사용자의 자산 그룹(통장, 지갑, 카드 등)을 관리한다. 각 자산의 잔액은 거래내역(AssetsDetail) 등록/수정/삭제에 따라 자동 갱신된다. 총 자산 계산 시 포함 여부를 개별 자산마다 설정할 수 있다.

---

## 엔티티

### Assets

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long (PK) | 자산 고유 ID |
| `user` | User (FK) | 소유 사용자 |
| `name` | String | 자산 이름 (예: 신한은행, 현금) |
| `color` | String | 표시 색상 (HEX 코드, 예: `#FF5733`) |
| `balance` | Long | 현재 잔액 |
| `orders` | Integer | 표시 순서 |
| `totalActive` | BooleanYn | 총 자산 합계에 포함 여부 (`Y`/`N`) |

> BaseEntity 상속: `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `status`

---

## 기능 요구사항

### 1. 자산 생성

- **Endpoint**: `POST /api/v1/user/assets`
- **인증**: 필요 (ROLE_USER)

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `name` | String | Y | 자산 이름 |
| `color` | String | Y | 색상 HEX 코드 |
| `balance` | Long | Y | 초기 잔액 |
| `totalActive` | BooleanYn | Y | 총 자산 포함 여부 (`Y`/`N`) |

**비즈니스 규칙**
- 초기 잔액은 사용자가 직접 입력
- 이후 잔액은 거래내역(AssetsDetail) 기반으로 자동 계산

**응답**: `ApiResponse`

---

### 2. 자산 목록 조회

- **Endpoint**: `GET /api/v1/user/assets`
- **인증**: 필요 (ROLE_USER)

**비즈니스 규칙**
- ACTIVE 상태인 자산만 반환
- `orders` 오름차순 정렬
- `totalActive = Y`인 자산의 잔액 합산하여 총 자산 금액 함께 반환

**응답**
```json
{
  "data": {
    "totalBalance": 3500000,
    "assets": [
      {
        "id": 1,
        "name": "신한은행",
        "color": "#0064FF",
        "balance": 2500000,
        "orders": 1,
        "totalActive": "Y"
      },
      {
        "id": 2,
        "name": "현금",
        "color": "#4CAF50",
        "balance": 1000000,
        "orders": 2,
        "totalActive": "Y"
      }
    ]
  }
}
```

---

### 3. 자산 단건 조회

- **Endpoint**: `GET /api/v1/user/assets/{assetsId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `assetsId` | Long | 자산 ID |

**응답**
```json
{
  "data": {
    "id": 1,
    "name": "신한은행",
    "color": "#0064FF",
    "balance": 2500000,
    "orders": 1,
    "totalActive": "Y"
  }
}
```

---

### 4. 자산 수정

- **Endpoint**: `PUT /api/v1/user/assets/{assetsId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `assetsId` | Long | 자산 ID |

**요청**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `name` | String | Y | 자산 이름 |
| `color` | String | Y | 색상 HEX 코드 |
| `totalActive` | BooleanYn | Y | 총 자산 포함 여부 |

**비즈니스 규칙**
- 잔액(`balance`)은 수정 불가 (거래내역에 의해서만 변경)
- ACTIVE 상태인 자산만 수정 가능

**응답**: `ApiResponse`

---

### 5. 자산 삭제

- **Endpoint**: `DELETE /api/v1/user/assets/{assetsId}`
- **인증**: 필요 (ROLE_USER)

**경로 변수**
| 변수 | 타입 | 설명 |
|------|------|------|
| `assetsId` | Long | 자산 ID |

**비즈니스 규칙**
- 소프트 삭제 (`status = DELETED`)

**응답**: `ApiResponse`

---

## 잔액 자동 갱신 규칙

| 이벤트 | 동작 |
|--------|------|
| AssetsDetail 생성 | 거래 금액만큼 잔액 증가/감소 |
| AssetsDetail 수정 | 기존 금액 원복 후 새 금액 반영 |
| AssetsDetail 삭제 | 해당 금액만큼 잔액 역산 적용 |
| Records 생성 (자산 연동) | AssetsDetail 자동 생성 → 잔액 갱신 |
| Records 삭제 | 연결된 AssetsDetail 삭제 → 잔액 역산 |

---

## 에러 코드

| 코드 | HTTP | 설명 |
|------|------|------|
| 1250 | 400 | 자산을 찾을 수 없음 |
| 1251 | 400 | 자산 접근 권한 없음 |

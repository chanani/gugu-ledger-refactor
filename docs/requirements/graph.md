# 그래프/통계(Graph) 도메인 기능 요구사항

## 개요

사용자의 수입/지출 거래내역을 카테고리별로 집계하여 차트/그래프 렌더링에 필요한 통계 데이터를 제공한다.

---

## 기능 요구사항

### 1. 카테고리별 통계 조회

- **Endpoint**: `GET /api/v1/user/graphs`
- **인증**: 필요 (ROLE_USER)

**요청 파라미터**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `yearMonth` | String | Y | 조회 년월 (`YYYY-MM`) |
| `type` | RecordType | Y | `DEPOSIT`(수입) / `WITHDRAW`(지출) |

**비즈니스 규칙**
- 해당 월의 거래내역을 카테고리별로 그룹화하여 합계 계산
- 전체 합계 대비 각 카테고리의 비율(%) 계산
- ACTIVE 상태의 거래내역만 대상
- 금액 내림차순 정렬

**응답**
```json
{
  "data": {
    "totalAmount": 850000,
    "type": "WITHDRAW",
    "yearMonth": "2026-03",
    "categories": [
      {
        "categoryId": 2,
        "categoryName": "식비",
        "iconPath": "/icons/food.png",
        "amount": 320000,
        "ratio": 37.6
      },
      {
        "categoryId": 3,
        "categoryName": "교통",
        "iconPath": "/icons/transport.png",
        "amount": 150000,
        "ratio": 17.6
      },
      {
        "categoryId": 5,
        "categoryName": "문화/여가",
        "iconPath": "/icons/leisure.png",
        "amount": 120000,
        "ratio": 14.1
      }
    ]
  }
}
```

---

## 응답 필드 설명

| 필드 | 타입 | 설명 |
|------|------|------|
| `totalAmount` | Long | 해당 월 전체 합계 금액 |
| `type` | RecordType | 조회한 타입 (`DEPOSIT`/`WITHDRAW`) |
| `yearMonth` | String | 조회 년월 |
| `categories[].categoryId` | Long | 카테고리 ID |
| `categories[].categoryName` | String | 카테고리 이름 |
| `categories[].iconPath` | String | 카테고리 아이콘 경로 |
| `categories[].amount` | Long | 카테고리별 합계 금액 |
| `categories[].ratio` | Double | 전체 대비 비율 (%) |

---

## 사용 시나리오

1. 사용자가 월별 지출 분석 화면 진입
2. 해당 월(`yearMonth`)과 타입(`WITHDRAW`) 선택
3. API 호출 → 카테고리별 도넛 차트 또는 막대 그래프 렌더링
4. 수입 분석 시 `type=DEPOSIT`으로 동일하게 조회

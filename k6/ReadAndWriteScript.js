import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    scenarios: {
        // 읽기 요청 70%
        read_traffic: {
            executor: 'constant-vus',
            vus: 90,  // 가상 사용자 70명
            duration: '30s',
            exec: 'readOperation',
        },
        // 쓰기 요청 30%
        write_traffic: {
            executor: 'constant-vus',
            vus: 10,  // 가상 사용자 30명
            duration: '30s',
            exec: 'writeOperation',
        },
    },
};

const token = 'eyJhbGciOiJIUzUxMiJ9.eyJpZCI6NSwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6IkFVVEgiLCJpc3MiOiJ3d3cuZ3VndS1iYWNrLmtyIiwiaWF0IjoxNzY0OTE4ODYzLCJleHAiOjE3NjQ5NTQ4NjN9.nPQkXZttJJNP6BbT94eRl9n6sgXqS6c2gcTgGz3hiTYawFxWe1LN98lJnjHBGy2nxYkAk37yc0YlCiVQtdbSGA';

const params = {
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
    },
};

// 읽기 작업 (Slave로 가는 요청)
export function readOperation() {
    http.get('http://localhost:11101/api/v1/user/categories/1', params);
    sleep(0.1);
}

// 쓰기 작업 (Master로 가는 요청)
export function writeOperation() {
    const payload = JSON.stringify({
        "name": "기타",
        "type": "WITHDRAW",
        "icon": 0
    });

    http.post('http://localhost:11101/api/v1/user/categories', payload, params);
    sleep(0.1);
}
// 실행 방법
// k6 run --vus 인원수 --duration 10s 파일명
// k6 run --vus 30 --duration 10s script.js
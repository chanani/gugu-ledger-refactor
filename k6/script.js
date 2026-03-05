import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
    const token = 'eyJhbGciOiJIUzUxMiJ9.eyJpZCI6NSwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInN1YiI6IkFVVEgiLCJpc3MiOiJ3d3cuZ3VndS1iYWNrLmtyIiwiaWF0IjoxNzY0OTE4ODYzLCJleHAiOjE3NjQ5NTQ4NjN9.nPQkXZttJJNP6BbT94eRl9n6sgXqS6c2gcTgGz3hiTYawFxWe1LN98lJnjHBGy2nxYkAk37yc0YlCiVQtdbSGA';

    const params = {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    };

    http.get('http://localhost:11101/api/v1/user/categories/1', params);
}

// 실행 방법
// k6 run --vus 인원수 --duration 10s 파일명
// k6 run --vus 30 --duration 10s script.js
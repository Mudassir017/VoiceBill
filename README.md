# VoiceBill

Smart voice-based billing and invoice management system.

## Stack
- Frontend: React + Vite + Tailwind CSS v4
- Backend: Spring Boot 4.1 (Java 25) + MySQL
- Auth: JWT in HttpOnly cookie

## Prerequisites
- Java 25, Node 22, MySQL 8

## Setup
1. Create MySQL database `voicebill` (auto-created) and user with password.
2. Backend: set env vars DB_PASSWORD (and optionally DB_USERNAME, DB_URL, JWT_SECRET, OPENAI_API_KEY). Run:
   `mvn -o spring-boot:run` in `backend/` (use cached Maven at `~/.m2/wrapper/dists/...`).
3. Frontend: `npm install` then `npm run dev` in `frontend/`.

## Endpoints
- Health: GET /api/health, /api/ready
- Auth: /api/auth/register, /api/auth/login, /api/auth/logout, /api/auth/me
- Products: /api/products
- Customers: /api/customers
- Invoices: /api/invoices, /api/invoices/{id}/pdf
- Dashboard: /api/dashboard/summary, /api/dashboard/recent
- Voice: /api/voice/interpret

## Notes
- Spring AI not integrated (offline); voice uses rule-based interpretation.
- PDF uses minimal generator (OpenPDF unavailable offline).
- QR deferred (ZXing unavailable).

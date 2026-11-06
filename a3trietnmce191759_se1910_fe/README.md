# Casa Hotel frontend

## Run locally

```sh
npm install
npm run dev
```

By default, the frontend calls `http://localhost:8080/api/v1`. Set
`VITE_API_BASE_URL` to override the API base URL (include `/api/v1`).

## Screens

- `/` — public room listing
- `/login/customer` and `/register` — customer access
- `/login/staff` — staff access using staff records in the backend database
- `/profile`, `/booking`, and `/history` — authenticated customer features
- `/staff` — customer and room CRUD, plus booking status management

Axios reads the stored access token for every request and adds it as a Bearer
token through a request interceptor. API calls are grouped by feature in
`src/services`.

The current backend supports staff booking listing and status changes, but does
not expose staff booking create/delete endpoints; the dashboard only offers
operations supported by those APIs.

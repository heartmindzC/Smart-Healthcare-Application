# API Gateway Service

API Gateway for Smart Healthcare System using Spring Cloud Gateway.

## Port
- Default: 8085

## Routes

All routes are prefixed with `/api`:

- `/api/users/**` → User Service (port 8080)
- `/api/patients/**` → Patient Service (port 8081)
- `/api/doctors/**` → Doctor Service (port 8082)
- `/api/ehr/**` → EHR Service (port 8083)

## Example Usage

```
GET http://localhost:8085/api/ehr/patient/1
GET http://localhost:8085/api/users/findUserByUserId/user123
GET http://localhost:8085/api/patients/get-patient/1
```


